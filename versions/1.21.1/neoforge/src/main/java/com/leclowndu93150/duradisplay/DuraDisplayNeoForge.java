package com.leclowndu93150.duradisplay;

import com.leclowndu93150.duradisplay.compat.CompatRegistry;
import com.leclowndu93150.duradisplay.compat.GTCompatSupplier;
import com.leclowndu93150.duradisplay.compat.NeoForgeEnergyCompat;
import com.leclowndu93150.duradisplay.config.Config;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

@Mod(value = DuraDisplay.MOD_ID, dist = Dist.CLIENT)
public class DuraDisplayNeoForge {

    private static final Lazy<KeyMapping> TOGGLE_KEY = Lazy.of(() -> new KeyMapping(
            "duradisplay.keybinds.toggle",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_M,
            "key.category.duradisplay.keybinds"
    ));

    public DuraDisplayNeoForge(IEventBus modBus) {
        DuraDisplay.init();
        if (ModList.get().isLoaded("gtceu")) {
            CompatRegistry.registerPlatform(new GTCompatSupplier());
        }
        CompatRegistry.registerPlatform(new NeoForgeEnergyCompat());
        modBus.addListener(this::registerKeyMappings);
        NeoForge.EVENT_BUS.addListener(this::onKeyInput);
    }

    private void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE_KEY.get());
    }

    private void onKeyInput(InputEvent.Key event) {
        if (TOGGLE_KEY.get().consumeClick()) {
            Config.toggle();
        }
    }
}
