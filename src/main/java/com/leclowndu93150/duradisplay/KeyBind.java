package com.leclowndu93150.duradisplay;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.leclowndu93150.duradisplay.Main.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeyBind {

    public static KeyMapping KeyMappingDura;

    public static void init() {
        KeyMappingDura = registerKey("toggle_display", KeyMapping.CATEGORY_GAMEPLAY, InputConstants.KEY_B);
    }


    private static KeyMapping registerKey(String name, String category, int keycode) {
        final var key = new KeyMapping("key." + Main.MODID + "." + "toggle_display", keycode, category);
        ClientRegistry.registerKeyBinding(key);
        return key;
    }

    @Mod.EventBusSubscriber(modid = Main.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeClient {
        public static boolean modEnabled = true;
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                while (KeyMappingDura.consumeClick()) {
                    modEnabled = !modEnabled;
                }
            }
        }
    }

}
