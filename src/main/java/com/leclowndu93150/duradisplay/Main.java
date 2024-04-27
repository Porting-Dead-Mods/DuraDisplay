package com.leclowndu93150.duradisplay;

import com.leclowndu93150.duradisplay.api.CustomDisplayItem;
import com.mojang.serialization.Codec;
import net.minecraft.SharedConstants;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(Main.MODID)
public class Main {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "duradisplay";
    private static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, MODID);
    protected static Supplier<DataComponentType<Integer>> TEST_DATA;

    public Main(IEventBus modEventbus) {
        // Only register items if running in-dev
        if (SharedConstants.IS_RUNNING_IN_IDE) {
            DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, MODID);
            TEST_DATA = registerDataComponentType("test_data",
                    builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
            ITEMS.register("test_item", TestItem::new);
            ITEMS.register(modEventbus);
            DATA_COMPONENTS.register(modEventbus);
        }
    }

    public static <T> Supplier<DataComponentType<T>> registerDataComponentType(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENTS.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onRegisterItemDecorations(final RegisterItemDecorationsEvent event) {
            for (Item item : BuiltInRegistries.ITEM) {
                if (item instanceof CustomDisplayItem customDisplayItem) {
                    // Item has custom display behavior, so we pass the
                    // customDisplayItem to the duradisplay
                    System.out.println("Found custom display item: "+item);
                    event.register(item, new DuraDisplay(customDisplayItem, DuraDisplay.DisplayType.CUSTOM));
                } else if (item.getDefaultInstance().isDamageableItem()) {
                    // Item has durability, so we pass null and therefore use
                    // builtin behavior
                    event.register(item, new DuraDisplay(null, DuraDisplay.DisplayType.DURABILITY));
                } else {
                    // Item has energy or is a regular item, so we pass null and therefore use
                    // builtin behavior
                    event.register(item, new DuraDisplay(null, DuraDisplay.DisplayType.ENERGY));
                }
            }
        }
    }
}
