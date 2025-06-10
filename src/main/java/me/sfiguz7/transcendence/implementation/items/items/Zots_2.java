package me.sfiguz7.transcendence.implementation.items.items;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import me.sfiguz7.transcendence.lists.TEItems;
import me.sfiguz7.transcendence.lists.TERecipeType;
import org.bukkit.inventory.ItemStack;

public class Zots_2 extends SlimefunItem {

    public Zots_2(Type type) {
        super(TEItems.transcendence, type.slimefunItem, TERecipeType.ZOT_OVERLOADER, type.recipe);
    }

    public enum Type {
        UP(TEItems.ZOT_UP_2
        ),
        DOWN(TEItems.ZOT_DOWN_2
        ),
        LEFT(TEItems.ZOT_LEFT_2
        ),
        RIGHT(TEItems.ZOT_RIGHT_2
        ),
        ALPHA(TEItems.ZOT_ALPHA_2
        ),
        BETA(TEItems.ZOT_BETA_2
        ),
        GAMMA(TEItems.ZOT_GAMMA_2
        ),
        DELTA(TEItems.ZOT_DELTA_2
        ),
        OMEGA(TEItems.ZOT_OMEGA_2
        );
        private final SlimefunItemStack slimefunItem;
        private final ItemStack[] recipe;

        Type(SlimefunItemStack slimefunItem) {
            this.slimefunItem = slimefunItem;

            this.recipe = new ItemStack[] {
                null, null, null,
                null, null, null,
                null, null, null};
        }
    }
}
