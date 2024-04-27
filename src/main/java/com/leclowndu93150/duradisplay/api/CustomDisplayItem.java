package com.leclowndu93150.duradisplay.api;

//import com.leclowndu93150.duradisplay.TestItem;

import net.minecraft.world.item.ItemStack;

/**
 * This is the CustomDisplayItem interface. It allows
 * your item to have a custom percentage display like
 * there is for energy and durability.
 * For an example look at {//@link TestItem}
 */
public interface CustomDisplayItem {
    /**
     * This method determines whether the percentage amount should even be displayed.
     * If you have already set up your item bar, you dont have to override this method
     * since it will use the {@link ItemStack#isBarVisible()} by default
     *
     * @param stack The itemstack that is currently being rendered
     * @return whether the bar should or should not be rendered
     */
    default boolean shouldDisplay(ItemStack stack) {
        return stack.isBarVisible();
    }

    /**
     * This is the percentage displayed on the item.
     * therefore it should be a value in the range of
     * 1 - 100.
     *
     * @param stack The itemstack that is currently being rendered
     * @return the percentage to be displayed
     */
    int getPercentage(ItemStack stack);

    /**
     * This method is the color of the percentage. The
     * color is represented as a hexadecimal number.
     * If you already implemented the getBarColor() method
     * for your item, you can also use that instead of overriding
     * this method
     *
     * @param stack The itemstack that is currently being rendered
     * @return the color of the percentage text as a hexadecimal int
     */
    default int getColor(ItemStack stack) {
        return stack.getBarColor();
    }
}
