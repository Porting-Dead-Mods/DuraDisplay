package com.leclowndu93150.duradisplay.compat;

import com.github.wolfiewaffle.hardcore_torches.config.Config;
import com.github.wolfiewaffle.hardcore_torches.item.LanternItem;
import com.github.wolfiewaffle.hardcore_torches.item.OilCanItem;
import com.github.wolfiewaffle.hardcore_torches.item.TorchItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public record HTCompat(ItemStack itemStack) {
    public BuiltinCompat registerCompat() {
        // TODO: in 1.20.5+, replace this with a pattern switch
        int stored, maxStored;
        Item item1 = itemStack.getItem();
        if (item1 instanceof TorchItem item) {
            stored = TorchItem.getFuel(itemStack);
            maxStored = item.getMaxFuel();
        } else if (item1 instanceof LanternItem item) {
            stored = LanternItem.getFuel(itemStack);
            maxStored = item.getMaxFuel();
        } else if (item1 instanceof OilCanItem) {
            stored = OilCanItem.getFuel(itemStack);
            maxStored = Config.maxCanFuel.get();
        } else {
            stored = 0;
            maxStored = 0;
        }
        return new BuiltinCompat(((double) stored / (double) maxStored) * 100D,0xe3bb56, itemStack.isBarVisible());
    }

    public static @Nullable HTCompat from(ItemStack itemStack) {
        Item item = itemStack.getItem();
        if (item instanceof TorchItem || item instanceof LanternItem || item instanceof OilCanItem)
            return new HTCompat(itemStack);
        return null;
    }
}
