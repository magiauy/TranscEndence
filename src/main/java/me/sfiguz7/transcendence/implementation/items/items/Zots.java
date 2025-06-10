package me.sfiguz7.transcendence.implementation.items.items;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import me.sfiguz7.transcendence.lists.TEItems;
import me.sfiguz7.transcendence.lists.TERecipeType;
import org.bukkit.inventory.ItemStack;

public class Zots extends SlimefunItem {

    public Zots(Type type) {
        super(TEItems.transcendence, type.slimefunItem, TERecipeType.NANOBOT_CRAFTER, type.recipe);
    }

    public enum Type {
        UP(TEItems.ZOT_UP, TEItems.QUIRP_UP
        ),
        DOWN(TEItems.ZOT_DOWN, TEItems.QUIRP_DOWN
        ),
        LEFT(TEItems.ZOT_LEFT, TEItems.QUIRP_LEFT
        ),
        RIGHT(TEItems.ZOT_RIGHT, TEItems.QUIRP_RIGHT
        ),
        ALPHA(TEItems.ZOT_ALPHA, TEItems.QUIRP_ALPHA
        ),
        BETA(TEItems.ZOT_BETA, TEItems.QUIRP_BETA
        ),
        GAMMA(TEItems.ZOT_GAMMA, TEItems.QUIRP_GAMMA
        ),
        DELTA(TEItems.ZOT_DELTA, TEItems.QUIRP_DELTA
        ),
        OMEGA(TEItems.ZOT_OMEGA, TEItems.QUIRP_OMEGA
        );
        
        

        private final SlimefunItemStack slimefunItem;
        private final ItemStack[] recipe;

        Type(SlimefunItemStack slimefunItem, ItemStack quirp) {
            this.slimefunItem = slimefunItem;
            SlimefunItemStack stableBlock = TEItems.STABLE_BLOCK;

            this.recipe = new ItemStack[] {
                quirp, quirp, quirp,
                quirp, stableBlock, quirp,
                quirp, quirp, quirp};
        }
    }
}
