package com.portingdeadmods.duradisplay;

import com.portingdeadmods.duradisplay.compat.BuiltinCompat;
import com.portingdeadmods.duradisplay.compat.GTCompat;
import com.portingdeadmods.duradisplay.renderer.DuraDisplayRenderer;
import com.mojang.serialization.Codec;
import net.minecraft.SharedConstants;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(DuraDisplay.MODID)
public class DuraDisplay {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "duradisplay";
    public static Logger LOGGER = LogManager.getLogger(MODID);

    public static final List<BuiltinCompat.CompatSupplier> BUILTIN_COMPATS = Collections.synchronizedList(new ArrayList<>());

    private static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, MODID);
    protected static Supplier<DataComponentType<Integer>> TEST_DATA;

    public DuraDisplay() {
        registerCompats();
    }

    private static void registerCompats() {
        // NOTE: The order this is registered in determines its priority
        // Gregtech
        registerCompat(itemStack -> {
            if (ModList.get().isLoaded("gtceu")) {
                GTCompat compat = GTCompat.from(itemStack);
                return compat == null ? null : compat.registerCompat();
            }
            return null;
        });
        // Energy
        registerCompat(itemStack -> {
            @Nullable IEnergyStorage energyStorage = itemStack.getCapability(Capabilities.EnergyStorage.ITEM);
            if (energyStorage != null) {
                return Collections.singletonList(new BuiltinCompat(
                        ((double) energyStorage.getEnergyStored() / (double) energyStorage.getMaxEnergyStored()) * 100D,
                        itemStack.getItem().getBarColor(itemStack),
                        itemStack.isBarVisible()
                ));
            }
            return null;
        });
        // Durability
        registerCompat(itemStack -> {
            if (itemStack.isDamageableItem()) {
                if (ModList.get().isLoaded("gtceu") && GTCompat.from(itemStack) != null) {
                    int damage = itemStack.getDamageValue();
                    int maxDamage = itemStack.getMaxDamage();
                    double durabilityPercentage = ((double) (maxDamage - damage) / (double) maxDamage) * 100D;
                    return Collections.singletonList(new BuiltinCompat(durabilityPercentage, itemStack.getBarColor(), itemStack.isBarVisible()));
                }
            }
            return null;
        });
        // Hardcore torches
        /*
        registerCompat(itemStack -> {
            if (ModList.get().isLoaded("hardcore_torches")) {
                HTCompat compat = HTCompat.from(itemStack);
                return compat == null ? null : compat.registerCompat();
            }
            return null;
        });
         */
        // Bundle
        registerCompat(itemStack -> {
            if (itemStack.getItem() instanceof BundleItem) {
                double percentage = BundleItem.getFullnessDisplay(itemStack) * 100D;
                return Collections.singletonList(new BuiltinCompat(percentage, itemStack.getBarColor(), itemStack.isBarVisible()));
            }
            return null;
        });
    }

    private static void registerCompat(BuiltinCompat.CompatSupplier supplier) {
        BUILTIN_COMPATS.add(supplier);
    }

    public static <T> Supplier<DataComponentType<T>> registerDataComponentType(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENTS.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onRegisterItemDecorations(final RegisterItemDecorationsEvent event) {
            for (Item item : BuiltInRegistries.ITEM) {
                event.register(item, DuraDisplayRenderer.INSTANCE);
            }
        }
    }
}
