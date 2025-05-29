package com.leclowndu93150.duradisplay.platform.services;

import com.leclowndu93150.duradisplay.compat.BuiltinCompat;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.ServiceLoader;

public interface IPlatformHelper {
    IPlatformHelper INSTANCE = ServiceLoader.load(IPlatformHelper.class).findFirst().orElseThrow();

    /**
     * Register platform-specific compat suppliers (GT, Energy, etc.)
     */
    void registerPlatformCompats();

    /**
     * Check if a mod is loaded
     */
    boolean isModLoaded(String modid);

    /**
     * Get platform-specific compat for an item (used for GT/Energy on NeoForge)
     */
    List<BuiltinCompat> getPlatformCompat(ItemStack itemStack);

    void registerKeybinds();
}