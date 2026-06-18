package com.leclowndu93150.duradisplay;

import com.leclowndu93150.duradisplay.compat.CompatRegistry;
import com.leclowndu93150.duradisplay.compat.FabricEnergyCompat;
import com.leclowndu93150.duradisplay.config.Config;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class DuraDisplayFabricClient implements ClientModInitializer {

    private static final KeyMapping TOGGLE_KEY = new KeyMapping(
            "duradisplay.keybinds.toggle",
            GLFW.GLFW_KEY_M,
            KeyMapping.Category.register(Identifier.parse("duradisplay:keybinds"))
    );

    @Override
    public void onInitializeClient() {
        DuraDisplay.init();
        if (FabricLoader.getInstance().isModLoaded("team_reborn_energy")) {
            CompatRegistry.registerPlatform(new FabricEnergyCompat());
        }
        KeyMappingHelper.registerKeyMapping(TOGGLE_KEY);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (TOGGLE_KEY.consumeClick()) {
                Config.toggle();
            }
        });
    }
}
