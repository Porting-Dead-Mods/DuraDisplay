package com.leclowndu93150.duradisplay.platform;

import com.leclowndu93150.duradisplay.compat.BuiltinCompat;
import com.leclowndu93150.duradisplay.platform.services.IPlatformHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;

import java.util.List;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public void registerPlatformCompats() {
    }

    @Override
    public boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }

    @Override
    public List<BuiltinCompat> getPlatformCompat(ItemStack itemStack) {
        return null;
    }

    @Override
    public void registerKeybinds() {

    }
}


