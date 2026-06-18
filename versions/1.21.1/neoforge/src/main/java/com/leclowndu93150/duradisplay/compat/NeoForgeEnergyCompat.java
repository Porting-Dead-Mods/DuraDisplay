package com.leclowndu93150.duradisplay.compat;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.Collections;
import java.util.List;

public final class NeoForgeEnergyCompat implements BuiltinCompat.Supplier {
    @Override
    public List<BuiltinCompat> compat(ItemStack stack) {
        IEnergyStorage storage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (storage == null) {
            return null;
        }
        int capacity = storage.getMaxEnergyStored();
        if (capacity <= 0) {
            return null;
        }
        double percentage = ((double) storage.getEnergyStored() / (double) capacity) * 100D;
        return Collections.singletonList(new BuiltinCompat(percentage, stack.getBarColor(), stack.isBarVisible()));
    }
}
