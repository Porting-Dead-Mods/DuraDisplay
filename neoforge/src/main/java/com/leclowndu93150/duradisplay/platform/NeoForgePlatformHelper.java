package com.leclowndu93150.duradisplay.platform;

import com.leclowndu93150.duradisplay.DuraDisplayCommon;
import com.leclowndu93150.duradisplay.compat.BuiltinCompat;
import com.leclowndu93150.duradisplay.compat.GTCompat;
import com.leclowndu93150.duradisplay.platform.services.IPlatformHelper;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public void registerPlatformCompats() {
        DuraDisplayCommon.registerCompat(itemStack -> {
            if (isModLoaded("gtceu")) {
                GTCompat compat = GTCompat.from(itemStack);
                return compat == null ? null : compat.registerCompat();
            }
            return null;
        });

        DuraDisplayCommon.registerCompat(itemStack -> {
            @Nullable IEnergyStorage energyStorage = itemStack.getCapability(Capabilities.EnergyStorage.ITEM);
            if (energyStorage != null) {
                return Collections.singletonList(new BuiltinCompat(
                        ((double) energyStorage.getEnergyStored() / (double) energyStorage.getMaxEnergyStored()) * 100D,
                        itemStack.getItem().getBarColor(itemStack),
                        itemStack.isBarVisible()
                ));
            }
            return null;
        });
    }

    @Override
    public boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }

    @Override
    public List<BuiltinCompat> getPlatformCompat(ItemStack itemStack) {
        if (isModLoaded("gtceu")) {
            GTCompat compat = GTCompat.from(itemStack);
            if (compat != null) {
                return compat.registerCompat();
            }
        }

        @Nullable IEnergyStorage energyStorage = itemStack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (energyStorage != null) {
            return Collections.singletonList(new BuiltinCompat(
                    ((double) energyStorage.getEnergyStored() / (double) energyStorage.getMaxEnergyStored()) * 100D,
                    itemStack.getItem().getBarColor(itemStack),
                    itemStack.isBarVisible()
            ));
        }

        return null;
    }

    @Override
    public void registerKeybinds() {

    }
}
