package com.leclowndu93150.duradisplay;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(value = DuraDisplay.MOD_ID)
public class DuraDisplayForge {

    public DuraDisplayForge() {
        DuraDisplay.init();
        if (FMLEnvironment.dist == Dist.CLIENT) {
            DuraDisplayForgeClient.bootstrap();
        }
    }
}
