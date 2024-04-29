package com.leclowndu93150.duradisplay;

import net.minecraftforge.fml.ModList;

public final class Utils {
    public static void ifGTLoaded(ModLoadedAction action) {
        if (ModList.get().isLoaded("gtceu")) {
            action.modIsLoadedAction();
        }
    }

    @FunctionalInterface
    public interface ModLoadedAction {
        void modIsLoadedAction();
    }
}
