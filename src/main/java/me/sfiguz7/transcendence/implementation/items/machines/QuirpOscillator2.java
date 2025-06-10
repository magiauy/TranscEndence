package me.sfiguz7.transcendence.implementation.items.machines;

import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import io.github.mooy1.infinityexpansion.items.materials.Materials;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.sfiguz7.transcendence.implementation.items.items.Polarizer;
import me.sfiguz7.transcendence.implementation.utils.interfaces.TEInventoryBlock;
import me.sfiguz7.transcendence.lists.TEItems;
import me.sfiguz7.transcendence.lists.TERecipeType;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class QuirpOscillator2 extends SimpleSlimefunItem<BlockTicker> implements TEInventoryBlock, EnergyNetComponent {

    private static final int ENERGY_CONSUMPTION = 512;
    private final ItemStack[] advancedQuirps = {
        TEItems.QUIRP_ALPHA,    // Gold/Yellow
        TEItems.QUIRP_BETA,     // Cyan  
        TEItems.QUIRP_GAMMA,    // Gray
        TEItems.QUIRP_DELTA,    // Silver
        TEItems.QUIRP_OMEGA     // Purple
    };
    private final int[] chancesDefault = {20, 20, 20, 20, 20}; // Equal 20% chance for each
    
    private final int[] border = {
        0, 1, 2, 3, 4, 5, 6, 7, 8,
        12, 13,
        21, 22,
        30, 31,
        36, 37, 38, 39, 40, 41, 42, 43, 44
    };
    private final int[] inputBorder = {};
    private final int[] outputBorder = {
        14, 15, 16, 17,
        23, 24, 25, 26,
        32, 33, 34, 35
    };
    private final int[] polarizerBorder = {
        9, 10, 11,
        18, 20,
        27, 28, 29
    };
    private final int polarizerSlot = 19;
    private int decrement = 20;

    public QuirpOscillator2() {
        super(TEItems.transcendence, TEItems.QUIRP_OSCILLATOR_2, TERecipeType.NANOBOT_CRAFTER,
            new ItemStack[] {
                Materials.MAGSTEEL_PLATE, TEItems.QUIRP_OSCILLATOR, Materials.MAGSTEEL_PLATE,
                Materials.MACHINE_CIRCUIT, setAmount(Materials.VOID_INGOT.clone(), 32), Materials.MACHINE_CIRCUIT,
                Materials.MAGSTEEL_PLATE, Materials.INFINITE_INGOT, Materials.MAGSTEEL_PLATE
            }
        );

        createPreset(this, this::constructMenu);
    }

    private static ItemStack setAmount(ItemStack item, int amount) {
        item.setAmount(amount);
        return item;
    }

    private void constructMenu(BlockMenuPreset preset) {
        for (int i : border) {
            preset.addItem(i, new CustomItemStack(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "),
                ChestMenuUtils.getEmptyClickHandler());
        }

        for (int i : inputBorder) {
            preset.addItem(i, new CustomItemStack(new ItemStack(Material.CYAN_STAINED_GLASS_PANE), " "),
                ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : outputBorder) {
            preset.addItem(i, new CustomItemStack(new ItemStack(Material.ORANGE_STAINED_GLASS_PANE), " "),
                ChestMenuUtils.getEmptyClickHandler());
        }
        for (int i : polarizerBorder) {
            preset.addItem(i, new CustomItemStack(new ItemStack(Material.PURPLE_STAINED_GLASS_PANE), " "),
                ChestMenuUtils.getEmptyClickHandler());
        }

        for (int i : getOutputSlots()) {
            preset.addMenuClickHandler(i, new ChestMenu.AdvancedMenuClickHandler() {

                @Override
                public boolean onClick(Player p, int slot, ItemStack cursor, ClickAction action) {
                    return false;
                }

                @Override
                public boolean onClick(InventoryClickEvent e, Player p, int slot, ItemStack cursor,
                                       ClickAction action) {
                    return cursor == null || cursor.getType() == Material.AIR;
                }
            });
        }
    }

    @Override
    public int[] getInputSlots() {
        return new int[] {};
    }

    @Override
    public int[] getOutputSlots() {
        return new int[] {24, 25};
    }

    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public int getCapacity() {
        return 2048;
    }

    @Override
    public BlockTicker getItemHandler() {
        return new BlockTicker() {

            @Override
            // Fires first!! The method tick() fires after this
            public void uniqueTick() {
                // Needed to keep track of all oscillators at once,
                // All it does is set back to max (for now 10, will be customizable)
                // when it reaches the lowest possible (AKA 1)
                if (decrement == 1) {
                    decrement = 10;
                    return;
                }
                decrement--;
            }

            @Override
            public void tick(Block b, SlimefunItem sf, Config data) {

                if (b.getWorld().getEnvironment() != World.Environment.THE_END) {
                    return;
                }

                // We only act once per decrement cycle, when decrement got to
                // lowest and has been reset
                if (decrement != 10) {
                    return;
                }

                BlockMenu menu = BlockStorage.getInventory(b);
                ItemStack output = getAdvancedQuirp(menu);

                if (getCharge(b.getLocation()) >= ENERGY_CONSUMPTION) {

                    if (!menu.fits(output, getOutputSlots())) {
                        return;
                    }

                    removeCharge(b.getLocation(), ENERGY_CONSUMPTION);
                    menu.pushItem(output, getOutputSlots());
                }
            }

            private ItemStack getAdvancedQuirp(BlockMenu menu) {
                int index = ThreadLocalRandom.current().nextInt(100);
                int accruedchance = 0;
                int[] chances = getChances(menu);
                for (int i = 0; i < advancedQuirps.length; i++) {
                    accruedchance += chances[i];
                    if (index < accruedchance) {
                        return advancedQuirps[i].clone();
                    }
                }
                //Never reached
                return new ItemStack(Material.AIR);
            }

            private int[] getChances(BlockMenu menu) {
                ItemStack pol = menu.getItemInSlot(polarizerSlot);
                if (SlimefunUtils.isItemSimilar(pol, TEItems.VERTICAL_POLARIZER, true)) {
                    // Vertical polarizer affects Alpha/Gamma (up/down energy flow)
                    return new int[]{35, 15, 35, 10, 5}; // Alpha, Beta, Gamma, Delta, Omega
                }
                if (SlimefunUtils.isItemSimilar(pol, TEItems.HORIZONTAL_POLARIZER, true)) {
                    // Horizontal polarizer affects Beta/Delta (side energy flow)
                    return new int[]{10, 35, 15, 35, 5}; // Alpha, Beta, Gamma, Delta, Omega
                }
                return chancesDefault;
            }

            @Override
            public boolean isSynchronized() {
                return true;
            }
        };
    }
}
