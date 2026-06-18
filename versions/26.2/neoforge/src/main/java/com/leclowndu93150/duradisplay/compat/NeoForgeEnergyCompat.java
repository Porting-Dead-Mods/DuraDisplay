package com.leclowndu93150.duradisplay.compat;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;

import java.util.Collections;
import java.util.List;

public final class NeoForgeEnergyCompat implements BuiltinCompat.Supplier {
    @Override
    public List<BuiltinCompat> compat(ItemStack stack) {
        EnergyHandler handler = ItemAccess.forStack(stack).getCapability(Capabilities.Energy.ITEM);
        if (handler == null) {
            return null;
        }
        long capacity = handler.getCapacityAsLong();
        if (capacity <= 0) {
            return null;
        }
        double percentage = ((double) handler.getAmountAsLong() / (double) capacity) * 100D;
        return Collections.singletonList(new BuiltinCompat(percentage, stack.getBarColor(), stack.isBarVisible()));
    }
}
