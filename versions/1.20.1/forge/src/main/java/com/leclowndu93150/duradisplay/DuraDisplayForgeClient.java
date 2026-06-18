package com.leclowndu93150.duradisplay;

import com.leclowndu93150.duradisplay.compat.BuiltinCompat;
import com.leclowndu93150.duradisplay.compat.CompatRegistry;
import com.leclowndu93150.duradisplay.compat.GTCompatSupplier;
import com.leclowndu93150.duradisplay.config.Config;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

import java.util.Collections;

public final class DuraDisplayForgeClient {

    private static final Lazy<KeyMapping> TOGGLE_KEY = Lazy.of(() -> new KeyMapping(
            "duradisplay.keybinds.toggle",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_M,
            "key.category.duradisplay.keybinds"
    ));

    private DuraDisplayForgeClient() {
    }

    public static void bootstrap() {
        if (ModList.get().isLoaded("gtceu")) {
            CompatRegistry.registerPlatform(new GTCompatSupplier());
        }
        CompatRegistry.registerPlatform(stack -> {
            IEnergyStorage storage = stack.getCapability(ForgeCapabilities.ENERGY).orElse(null);
            if (storage == null) {
                return null;
            }
            int capacity = storage.getMaxEnergyStored();
            if (capacity <= 0) {
                return null;
            }
            double percentage = ((double) storage.getEnergyStored() / (double) capacity) * 100D;
            return Collections.<BuiltinCompat>singletonList(new BuiltinCompat(percentage, stack.getBarColor(), stack.isBarVisible()));
        });
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(DuraDisplayForgeClient::registerKeyMappings);
        MinecraftForge.EVENT_BUS.addListener(DuraDisplayForgeClient::onKeyInput);
    }

    private static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE_KEY.get());
    }

    private static void onKeyInput(InputEvent.Key event) {
        if (TOGGLE_KEY.get().consumeClick()) {
            Config.toggle();
        }
    }
}
