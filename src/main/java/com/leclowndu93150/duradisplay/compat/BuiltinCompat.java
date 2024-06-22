package com.leclowndu93150.duradisplay.compat;

import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public record BuiltinCompat(double percentage, int color, boolean active) {
    @FunctionalInterface
    public interface CompatSupplier {
        @Nullable
        List<BuiltinCompat> compat(ItemStack itemStack);
    }
}