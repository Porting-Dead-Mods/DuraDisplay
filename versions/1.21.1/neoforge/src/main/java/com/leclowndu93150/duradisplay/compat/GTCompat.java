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

import java.util.ArrayList;
import java.util.List;

public record GTCompat(ItemStack stack) {
    public static final int COLOR_BAR_ENERGY = FastColor.ARGB32.color(255, 47, 155, 237);
    public static final int COLOR_BAR_DURABILITY = FastColor.ARGB32.color(255, 37, 199, 4);

    public static @Nullable GTCompat from(ItemStack stack) {
        if (stack.getItem() instanceof IGTTool || stack.getItem() instanceof IComponentItem) {
            return new GTCompat(stack);
        }
        return null;
    }

    public List<BuiltinCompat> build() {
        if (stack.getItem() instanceof IGTTool tool) {
            List<BuiltinCompat> compats = new ArrayList<>();
            if (!stack.has(DataComponents.UNBREAKABLE)) {
                int damage = stack.getDamageValue();
                int maxDamage = stack.getMaxDamage();
                double percentage = ((double) (maxDamage - damage) / (double) maxDamage) * 100D;
                compats.add(new BuiltinCompat(percentage, COLOR_BAR_DURABILITY, true));
            }
            if (tool.isElectric()) {
                long energy = tool.getCharge(stack);
                long maxEnergy = tool.getMaxCharge(stack);
                double percentage = ((double) energy / (double) maxEnergy) * 100D;
                compats.add(new BuiltinCompat(percentage, COLOR_BAR_ENERGY, true));
            }
            return compats;
        }
        if (stack.getItem() instanceof IComponentItem item) {
            List<BuiltinCompat> compats = new ArrayList<>();
            IDurabilityBar bar = null;
            for (IItemComponent component : item.getComponents()) {
                if (component instanceof IDurabilityBar durabilityBar) {
                    bar = durabilityBar;
                }
            }
            if (bar != null) {
                int damage = stack.getDamageValue();
                int maxDamage = stack.getMaxDamage();
                double percentage = ((double) (maxDamage - damage) / (double) maxDamage) * 100D;
                compats.add(new BuiltinCompat(percentage, COLOR_BAR_DURABILITY, true));
            }
            IElectricItem electricItem = GTCapabilityHelper.getElectricItem(stack);
            if (electricItem != null) {
                long energy = electricItem.getCharge();
                long maxEnergy = electricItem.getMaxCharge();
                double percentage = ((double) energy / (double) maxEnergy) * 100D;
                compats.add(new BuiltinCompat(percentage, COLOR_BAR_ENERGY, true));
            }
            return compats;
        }
        return null;
    }
}
