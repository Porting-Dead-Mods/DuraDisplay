package com.leclowndu93150.duradisplay.platform;

import com.leclowndu93150.duradisplay.compat.BuiltinCompat;
import com.leclowndu93150.duradisplay.config.Config;
import com.leclowndu93150.duradisplay.platform.services.IPlatformHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public void registerPlatformCompats() {
    }

    @Override
    public boolean isModLoaded(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }

    @Override
    public List<BuiltinCompat> getPlatformCompat(ItemStack itemStack) {
        return null;
    }

    private static final KeyMapping TOGGLE_KEY = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "duradisplay.keybinds.toggle",
            GLFW.GLFW_KEY_M,
            KeyMapping.Category.register(ResourceLocation.parse("duradisplay:keybinds"))
    ));

    @Override
    public void registerKeybinds() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (TOGGLE_KEY.consumeClick()) {
                Config.toggle();
            }
        });
    }
}
