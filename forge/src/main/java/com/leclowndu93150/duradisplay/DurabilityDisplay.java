package com.leclowndu93150.duradisplay;

import com.leclowndu93150.duradisplay.config.Config;
import com.leclowndu93150.duradisplay.platform.services.IPlatformHelper;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

@Mod(Constants.MOD_ID)
public class DurabilityDisplay {

    public DurabilityDisplay() {
        DuraDisplayCommon.init();

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::registerKeyMappings);

        MinecraftForge.EVENT_BUS.addListener(this::onKeyInput);
    }

    private static final KeyMapping TOGGLE_KEY = new KeyMapping(
            "duradisplay.keybinds.toggle",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_M,
            "duradisplay.keybinds.category"
    );

    public void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE_KEY);
    }

    public void onKeyInput(InputEvent.Key event) {
        if (TOGGLE_KEY.consumeClick()) {
            Config.toggle();
        }
    }
}
