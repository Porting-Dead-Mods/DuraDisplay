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

    private static KeyMapping registerKey() {
        final var key = new KeyMapping("key." + Main.MODID + "." + "toggle_display", InputConstants.KEY_B, KeyMapping.CATEGORY_MISC);
        ClientRegistry.registerKeyBinding(key);
        return key;
    }
    @Mod.EventBusSubscriber(modid = Main.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeClient {

        public static boolean modEnabled = true;
        public static final KeyMapping KEY = registerKey();

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                while (KEY.consumeClick()) {
                    modEnabled = !modEnabled;
                }
            }
        }
    }

}
