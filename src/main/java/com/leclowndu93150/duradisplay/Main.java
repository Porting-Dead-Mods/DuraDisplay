package com.leclowndu93150.duradisplay;

import com.leclowndu93150.duradisplay.api.CustomDisplayItem;
import com.leclowndu93150.duradisplay.compat.BuiltinCompat;
import com.leclowndu93150.duradisplay.compat.HTCompat;
import com.leclowndu93150.duradisplay.renderer.DuraDisplay;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.IItemDecorator;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(Main.MODID)
public class Main {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "duradisplay";

    public static final List<BuiltinCompat.CompatSupplier> BUILTIN_COMPATS = new ArrayList<>();

    public Main() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Only register items if running in-dev
        if (SharedConstants.IS_RUNNING_IN_IDE) {
            DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
            ITEMS.register("test_item", TestItem::new);
            ITEMS.register(modEventBus);
        }
        registerCompats();
    }

    private static void registerCompats() {
        // NOTE: The order this is registered in determines its priority
        // Custom
        registerCompat(itemStack -> {
            if (itemStack.getItem() instanceof CustomDisplayItem item) {
                return new BuiltinCompat(item.getPercentage(itemStack), item.getColor(itemStack), item.shouldDisplay(itemStack));
            }
            return null;
        });
        // Energy
        registerCompat(itemStack -> {
            LazyOptional<IEnergyStorage> energyStorage = itemStack.getCapability(ForgeCapabilities.ENERGY);
            if (energyStorage.isPresent()) {
                IEnergyStorage energyStorage1 = energyStorage.orElseThrow(NullPointerException::new);
                return new BuiltinCompat(
                        ((double) energyStorage1.getEnergyStored() / (double) energyStorage1.getMaxEnergyStored()) * 100D,
                        itemStack.getItem().getBarColor(itemStack),
                        itemStack.isBarVisible()
                );
            }
            return null;
        });
        // Damage
        registerCompat(itemStack -> {
            if (itemStack.isDamageableItem()) {
                int damage = itemStack.getDamageValue();
                int maxDamage = itemStack.getMaxDamage();
                double durabilityPercentage = ((double) (maxDamage - damage) / (double) maxDamage) * 100D;
                return new BuiltinCompat(durabilityPercentage, itemStack.getBarColor(), itemStack.isBarVisible());
            }
            return null;
        });
        // Hardcore torches
        registerCompat(itemStack -> {
            if (ModList.get().isLoaded("hardcore_torches")) {
                HTCompat compat = HTCompat.from(itemStack);
                return compat == null ? null : compat.registerCompat();
            }
            return null;
        });
        // Bundle
        registerCompat(itemStack -> {
            if (itemStack.getItem() instanceof BundleItem) {
                double percentage = BundleItem.getFullnessDisplay(itemStack) * 100D;
                return new BuiltinCompat(percentage, itemStack.getBarColor(), itemStack.isBarVisible());
            }
            return null;
        });
    }

    private static void registerCompat(BuiltinCompat.CompatSupplier supplier) {
        BUILTIN_COMPATS.add(supplier);
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onRegisterItemDecorations(final RegisterItemDecorationsEvent event) {
            for (Item item : BuiltInRegistries.ITEM) {
                System.out.println("Registering the item decoration");
                event.register(item, new DuraDisplay());
            }
        }
    }
}
