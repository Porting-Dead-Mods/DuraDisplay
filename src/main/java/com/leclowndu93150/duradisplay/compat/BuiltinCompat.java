package com.leclowndu93150.duradisplay.compat;

import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public record BuiltinCompat(double percentage, int color, boolean active) {
    @FunctionalInterface
    public interface CompatSupplier {
        @Nullable
        BuiltinCompat compat(ItemStack itemStack);
    }
}