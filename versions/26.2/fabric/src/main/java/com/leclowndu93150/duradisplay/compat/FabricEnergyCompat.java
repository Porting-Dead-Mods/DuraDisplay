package com.leclowndu93150.duradisplay.compat;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.minecraft.world.item.ItemStack;
import team.reborn.energy.api.EnergyStorage;

import java.util.Collections;
import java.util.List;

public final class FabricEnergyCompat implements BuiltinCompat.Supplier {
    @Override
    public List<BuiltinCompat> compat(ItemStack stack) {
        EnergyStorage storage = ContainerItemContext.withConstant(stack).find(EnergyStorage.ITEM);
        if (storage == null) {
            return null;
        }
        long capacity = storage.getCapacity();
        if (capacity <= 0) {
            return null;
        }
        double percentage = ((double) storage.getAmount() / (double) capacity) * 100D;
        return Collections.singletonList(new BuiltinCompat(percentage, stack.getBarColor(), stack.isBarVisible()));
    }
}
