package com.leclowndu93150.duradisplay;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import static com.leclowndu93150.duradisplay.Main.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeyBind {
    public static final Lazy<KeyMapping> DURA_MAPPING = Lazy.of(() ->new KeyMapping(
         "key." + MODID + ".toggle_display", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_M, "key.duradisplay.misc"));

    @SubscribeEvent
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(DURA_MAPPING.get());
    }

    @Mod.EventBusSubscriber(modid = Main.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeClient {
        public static boolean modEnabled = true;
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                while (DURA_MAPPING.get().consumeClick()) {
                    modEnabled = !modEnabled;
                }
            }
        }
    }

}
