package com.leclowndu93150.duradisplay.compat;

import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CompatRegistry {
    private static final List<BuiltinCompat.Supplier> SUPPLIERS = Collections.synchronizedList(new ArrayList<>());
    private static final List<BuiltinCompat.Supplier> PLATFORM_SUPPLIERS = Collections.synchronizedList(new ArrayList<>());

    private CompatRegistry() {
    }

    public static void register(BuiltinCompat.Supplier supplier) {
        SUPPLIERS.add(supplier);
    }

    public static void registerPlatform(BuiltinCompat.Supplier supplier) {
        PLATFORM_SUPPLIERS.add(supplier);
        SUPPLIERS.add(supplier);
    }

    public static List<BuiltinCompat.Supplier> suppliers() {
        return SUPPLIERS;
    }

    public static List<BuiltinCompat> platformCompat(ItemStack stack) {
        for (BuiltinCompat.Supplier supplier : PLATFORM_SUPPLIERS) {
            List<BuiltinCompat> compats = supplier.compat(stack);
            if (compats != null && !compats.isEmpty()) {
                return compats;
            }
        }
        return null;
    }
}
