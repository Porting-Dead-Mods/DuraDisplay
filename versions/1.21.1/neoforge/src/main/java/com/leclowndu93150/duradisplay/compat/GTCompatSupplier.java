package com.leclowndu93150.duradisplay.compat;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class GTCompatSupplier implements BuiltinCompat.Supplier {
    @Override
    public List<BuiltinCompat> compat(ItemStack stack) {
        GTCompat compat = GTCompat.from(stack);
        if (compat == null) {
            return null;
        }
        List<BuiltinCompat> result = compat.build();
        return result == null || result.isEmpty() ? null : result;
    }
}
