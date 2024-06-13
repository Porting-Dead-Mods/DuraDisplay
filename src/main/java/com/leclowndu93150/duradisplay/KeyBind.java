package com.leclowndu93150.duradisplay;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

import static com.leclowndu93150.duradisplay.Main.MODID;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeyBind {
    public static final Lazy<KeyMapping> DURA_MAPPING = Lazy.of(() ->new KeyMapping(
         "Toggle/Disable Display", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_M, "DuraDisplay"));

    @SubscribeEvent
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(DURA_MAPPING.get());
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
    public static class ForgeClient {
        public static boolean modEnabled = true;
        @SubscribeEvent
        public static void onClientTick(InputEvent.Key event) {
            if (DURA_MAPPING.get().consumeClick()) {
                    modEnabled = !modEnabled;
                }
        }
    }

}
