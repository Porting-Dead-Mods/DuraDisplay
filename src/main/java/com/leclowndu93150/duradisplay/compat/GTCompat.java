package com.leclowndu93150.duradisplay.compat;

import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.capability.IElectricItem;
import com.gregtechceu.gtceu.api.item.IComponentItem;
import com.gregtechceu.gtceu.api.item.IGTTool;
import com.gregtechceu.gtceu.api.item.component.IDurabilityBar;
import com.gregtechceu.gtceu.api.item.component.IItemComponent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public record GTCompat(ItemStack itemStack) {
    public static final int COLOR_BAR_ENERGY = FastColor.ARGB32.color(255, 0, 101, 178);
    public static final int COLOR_BAR_DURABILITY = FastColor.ARGB32.color(255, 20, 124, 0);
    public static final int COLOR_BAR_DEPLETED = FastColor.ARGB32.color(255, 122, 0, 0);

    public static @Nullable GTCompat from(ItemStack itemStack) {
        if (itemStack.getItem() instanceof IGTTool || itemStack.getItem() instanceof IComponentItem) {
            return new GTCompat(itemStack);
        }
        return null;
    }

    public List<BuiltinCompat> registerCompat() {
        if (itemStack.getItem() instanceof IGTTool tool) {
            List<BuiltinCompat> compats = new ArrayList<>();
            if (!itemStack.has(DataComponents.UNBREAKABLE)) {
                int damage = itemStack.getDamageValue();
                int maxDamage = itemStack.getMaxDamage();
                double durabilityPercentage = ((double) (maxDamage - damage) / (double) maxDamage) * 100D;
                compats.add(new BuiltinCompat(durabilityPercentage, COLOR_BAR_DURABILITY, true));
            }

            if (tool.isElectric()) {
                long energy = tool.getCharge(itemStack);
                long maxEnergy = tool.getMaxCharge(itemStack);
                double energyPercentage = ((double) energy / (double) maxEnergy) * 100D;
                compats.add(new BuiltinCompat(energyPercentage, COLOR_BAR_ENERGY, true));
            }
            return compats;
        } else if (itemStack.getItem() instanceof IComponentItem item) {
            List<BuiltinCompat> compats = new ArrayList<>();
            IDurabilityBar bar = null;

            for (IItemComponent component : item.getComponents()) {
                if (component instanceof IDurabilityBar durabilityBar) {
                    bar = durabilityBar;
                }
            }

            if (bar != null) {
                int damage = itemStack.getDamageValue();
                int maxDamage = itemStack.getMaxDamage();
                double durabilityPercentage = ((double) (maxDamage - damage) / (double) maxDamage) * 100D;
                compats.add(new BuiltinCompat(durabilityPercentage, COLOR_BAR_DURABILITY, true));
            }

            IElectricItem electricItem = GTCapabilityHelper.getElectricItem(itemStack);
            if (electricItem != null) {
                long energy = electricItem.getCharge();
                long maxEnergy = electricItem.getMaxCharge();
                double energyPercentage = ((double) energy / (double) maxEnergy) * 100D;
                compats.add(new BuiltinCompat(energyPercentage, COLOR_BAR_ENERGY, true));
            }

            return compats;
        }
        return null;
    }
}
