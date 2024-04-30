package com.leclowndu93150.duradisplay;

import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.capability.IPlatformEnergyStorage;
import com.leclowndu93150.duradisplay.api.CustomDisplayItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import javax.annotation.Nullable;
import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Main.MODID)
public class Main
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "duradisplay";

    public Main()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Only register items if running in-dev
        if (SharedConstants.IS_RUNNING_IN_IDE)
        {
            DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
            ITEMS.register("test_item", TestItem::new);
            ITEMS.register(modEventBus);
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientEvents
    {
        @SubscribeEvent
        public static void onRegisterItemDecorations(final RegisterItemDecorationsEvent event)
        {
            for (Item item : BuiltInRegistries.ITEM)
            {
                    if (item instanceof CustomDisplayItem customDisplayItem)
                    {
                        // Item has custom display behavior, so we pass the
                        // customDisplayItem to the duradisplay
                        event.register(item, new DuraDisplay(customDisplayItem, DuraDisplay.DisplayType.CUSTOM));
                    }
                    else if (item.canBeDepleted())
                    {
                        // Item has durability, so we pass null and therefore use
                        // builtin behavior
                        event.register(item, new DuraDisplay(null, DuraDisplay.DisplayType.DURABILITY));
                    }
                    else
                    {
                        // Item has energy or is a regular item, so we pass null and therefore use
                        // builtin behavior
                        event.register(item, new DuraDisplay(null, DuraDisplay.DisplayType.ENERGY));
                    }
            }
        }
    }

    private record DuraDisplay(@Nullable CustomDisplayItem customDisplayItem, DisplayType type) implements IItemDecorator {
        public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xPosition, int yPosition) {
            if (!stack.isEmpty() && stack.isBarVisible()) {
                LazyOptional<IEnergyStorage> energyStorage = stack.getCapability(ForgeCapabilities.ENERGY);
                IPlatformEnergyStorage euStorage = GTCapabilityHelper.getPlatformEnergyItem(stack);
                DisplayType type = type();
                // Give energystorage a higer prio than durability
                if (energyStorage.isPresent()) {
                    type = DisplayType.ENERGY;
                }

                if(euStorage != null){
                    type = DisplayType.GREGTECH;
                }

                switch (type) {
                    case DURABILITY:
                        if (stack.isDamaged()) {
                            int damage = stack.getDamageValue();
                            int maxDamage = stack.getMaxDamage();
                            double durabilityPercentage = ((double) (maxDamage - damage) / (double) maxDamage) * 100D;
                            renderText(guiGraphics, font, String.format("%.0f%%", durabilityPercentage), xPosition, yPosition, stack.getItem().getBarColor(stack)); // Default color white
                        }
                        break;
                    case ENERGY:
                        if (energyStorage.isPresent()) {
                            energyStorage.ifPresent(es -> {
                                System.out.println("Found energy item: " + stack.getItem());
                                int energyStored = es.getEnergyStored();
                                int maxEnergyStorage = es.getMaxEnergyStored();
                                double energyPercentage = ((double) energyStored / (double) maxEnergyStorage) * 100D;
                                renderText(guiGraphics, font, String.format("%.0f%%", energyPercentage), xPosition, yPosition, 0x34D8EB); // Custom color for energy display
                            });
                        } else {
                            int l = stack.getBarWidth();
                            int i = stack.getBarColor();
                            int j = xPosition + 2;
                            int k = yPosition + 13;
                            guiGraphics.fill(RenderType.guiOverlay(), j, k, j + 13, k + 2, -16777216);
                            guiGraphics.fill(RenderType.guiOverlay(), j, k, j + l, k + 1, i | 0xFF000000);
                        }
                        break;
                    case GREGTECH:
                                long energyStored = euStorage.getAmount();
                                long maxEnergyStorage = euStorage.getCapacity();
                                double energyPercentage = ((double) energyStored / (double) maxEnergyStorage) * 100D;
                                renderText(guiGraphics, font, String.format("%.0f%%", energyPercentage), xPosition, yPosition, 0x34D8EB); // Custom color for energy display
                        break;
                    case CUSTOM:
                        if (customDisplayItem != null && customDisplayItem.shouldDisplay(stack)) {
                            double energyPercentage = customDisplayItem.getPercentage(stack);
                            int color = customDisplayItem.getColor(stack); // Get color dynamically
                            renderText(guiGraphics, font, String.format("%.0f%%", energyPercentage), xPosition, yPosition, color); // Use custom color
                        }
                        break;
                }
            }
            return false;
        }

        private void renderText(GuiGraphics guiGraphics, Font font, String text, int xPosition, int yPosition, int color) {
            PoseStack poseStack = guiGraphics.pose();
            int stringWidth = font.width(text);
            int x = ((xPosition + 8) * 2 + 1 + stringWidth / 2 - stringWidth);
            int y = (yPosition * 2) + 22;
            poseStack.pushPose();
            poseStack.scale(0.5F, 0.5F, 0.5F);
            poseStack.translate(0.0D, 0.0D, 500.0D);
            MultiBufferSource.BufferSource multibuffersource$buffersource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
            font.drawInBatch(text, x, y, color, true, poseStack.last().pose(), multibuffersource$buffersource, Font.DisplayMode.NORMAL, 0, 15728880, false);
            multibuffersource$buffersource.endBatch();
            poseStack.popPose();
        }

        private enum DisplayType {
            DURABILITY,
            ENERGY,
            CUSTOM,
            GREGTECH,
        }
    }
}
