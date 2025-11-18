package com.leclowndu93150.duradisplay;


import com.leclowndu93150.duradisplay.config.Config;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

@Mod(Constants.MOD_ID)
public class DurabilityDisplay {

    public DurabilityDisplay(IEventBus eventBus) {
        Constants.LOG.info("Hello NeoForge world!");
        DuraDisplayCommon.init();

        IEventBus modBus = ModLoadingContext.get().getActiveContainer().getEventBus();
        modBus.addListener(this::registerKeyMappings);

        NeoForge.EVENT_BUS.addListener(this::onKeyInput);
    }

    private static final Lazy<KeyMapping> TOGGLE_KEY = Lazy.of(() -> new KeyMapping(
            "duradisplay.keybinds.toggle",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_M,
            KeyMapping.Category.register(ResourceLocation.parse("duradisplay:keybinds"))
    ));

    public void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE_KEY.get());
    }

    public void onKeyInput(InputEvent.Key event) {
        if (TOGGLE_KEY.get().consumeClick()) {
            Config.toggle();
        }
    }
}
