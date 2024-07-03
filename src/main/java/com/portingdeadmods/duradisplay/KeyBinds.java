package com.portingdeadmods.duradisplay;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

import static com.portingdeadmods.duradisplay.DuraDisplay.MODID;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeyBinds {
    public static final Lazy<KeyMapping> DURA_MAPPING = Lazy.of(() -> new KeyMapping(
            "duradisplay.keybinds.toggle", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_M, "duradisplay.keybinds.category"));

    @SubscribeEvent
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(DURA_MAPPING.get());
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
    public static class ForgeClient {
        public static boolean modEnabled = true;

        @SubscribeEvent
        public static void onKeyPressed(InputEvent.Key event) {
            if (DURA_MAPPING.get().consumeClick()) {
                modEnabled = !modEnabled;
            }
        }
    }

}
