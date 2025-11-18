package com.leclowndu93150.duradisplay.platform;

import com.leclowndu93150.duradisplay.DuraDisplayCommon;
import com.leclowndu93150.duradisplay.compat.BuiltinCompat;
import com.leclowndu93150.duradisplay.compat.GTCompat;
import com.leclowndu93150.duradisplay.platform.services.IPlatformHelper;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;

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
            EnergyHandler handler = ItemAccess.forStack(itemStack).getCapability(Capabilities.Energy.ITEM);
            if (handler != null) {
                return Collections.singletonList(new BuiltinCompat(
                        ((double) handler.getAmountAsInt() / (double) handler.getCapacityAsInt()) * 100D,
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

        EnergyHandler handler = ItemAccess.forStack(itemStack).getCapability(Capabilities.Energy.ITEM);
        if (handler != null) {
            return Collections.singletonList(new BuiltinCompat(
                    ((double) handler.getAmountAsInt() / (double) handler.getCapacityAsInt()) * 100D,
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
