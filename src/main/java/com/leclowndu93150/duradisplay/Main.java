package com.leclowndu93150.duradisplay;

import com.leclowndu93150.duradisplay.api.CustomDisplayItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.earlydisplay.ElementShader;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.IItemDecorator;
import net.neoforged.neoforge.client.ItemDecoratorHandler;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.registries.DeferredRegister;
import javax.annotation.Nullable;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(Main.MODID)
public class Main
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "duradisplay";

    public Main(IEventBus modEventbus)
    {
        // Only register items if running in-dev
        if (SharedConstants.IS_RUNNING_IN_IDE)
        {
            DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, MODID);
            ITEMS.register("test_item", TestItem::new);
            ITEMS.register(modEventbus);
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
                        // Item has energy, so we pass null and therefore use
                        // builtin behavior
                        event.register(item, new DuraDisplay(null, DuraDisplay.DisplayType.ENERGY));
                    }
            }
        }
    }

    private record DuraDisplay(@Nullable CustomDisplayItem customDisplayItem, DisplayType type) implements IItemDecorator
    {
        public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xPosition, int yPosition)
        {
            if (!stack.isEmpty())
            {
                IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
                if (energyStorage != null && type == DisplayType.ENERGY)
                {
                    PoseStack poseStack = guiGraphics.pose();

                    int energyStored = energyStorage.getEnergyStored();
                    int maxEnergyStorage = energyStorage.getMaxEnergyStored();

                    double energyPercentage = ((double) energyStored / (double) maxEnergyStorage) * 100D;

                    String formattedPercentage = String.format("%.0f%%", energyPercentage);

                    int stringWidth = font.width(formattedPercentage);
                    int x = ((xPosition + 8) * 2 + 1 + stringWidth / 2 - stringWidth);
                    int y = (yPosition * 2) + 22;

                    int color = 0x34D8EB;
                    poseStack.pushPose();
                    poseStack.scale(0.5F, 0.5F, 0.5F);
                    poseStack.translate(0.0D, 0.0D, 750.0F);
                    MultiBufferSource.BufferSource multibuffersource$buffersource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
                    font.drawInBatch(formattedPercentage, x, y, color, true, poseStack.last().pose(), multibuffersource$buffersource, Font.DisplayMode.NORMAL, 0, 15728880, false);
                    multibuffersource$buffersource.endBatch();
                    poseStack.popPose();
                    return true;
                }

                if (customDisplayItem != null && customDisplayItem.shouldDisplay(stack) && type == DisplayType.CUSTOM)
                {
                    PoseStack poseStack = guiGraphics.pose();

                    double energyPercentage = customDisplayItem.getPercentage(stack);

                    String formattedPercentage = String.format("%.0f%%", energyPercentage);

                    int stringWidth = font.width(formattedPercentage);
                    int x = ((xPosition + 8) * 2 + 1 + stringWidth / 2 - stringWidth);
                    int y = (yPosition * 2) + 22;

                    int color = customDisplayItem.getColor(stack);
                    poseStack.pushPose();
                    poseStack.scale(0.5F, 0.5F, 0.5F);
                    poseStack.translate(0.0D, 0.0D, 750.0F);
                    MultiBufferSource.BufferSource multibuffersource$buffersource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
                    font.drawInBatch(formattedPercentage, x, y, color, true, poseStack.last().pose(), multibuffersource$buffersource, Font.DisplayMode.NORMAL, 0, 15728880, false);
                    multibuffersource$buffersource.endBatch();
                    poseStack.popPose();
                    return true;
                }

                if (stack.isDamaged() && energyStorage == null && type == DisplayType.DURABILITY)
                {
                    PoseStack poseStack = guiGraphics.pose();

                    int damage = stack.getDamageValue();
                    int maxDamage = stack.getMaxDamage();

                    double durabilityPercentage = ((double) (maxDamage - damage) / (double) maxDamage) * 100D;

                    String formattedPercentage = String.format("%.0f%%", durabilityPercentage);

                    int stringWidth = font.width(formattedPercentage);
                    int x = ((xPosition + 8) * 2 + 1 + stringWidth / 2 - stringWidth);
                    int y = (yPosition * 2) + 22;

                    int color = stack.getItem().getBarColor(stack);

                    poseStack.pushPose();
                    poseStack.scale(0.5F, 0.5F, 0.5F);
                    poseStack.translate(0.0D, 0.0D, 750.0F);
                    MultiBufferSource.BufferSource multibuffersource$buffersource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
                    font.drawInBatch(formattedPercentage, x, y, color, true, poseStack.last().pose(), multibuffersource$buffersource, Font.DisplayMode.NORMAL, 0, 15728880, false);
                    multibuffersource$buffersource.endBatch();
                    poseStack.popPose();
                    return true;

                }
            }
            return true;
        }

        private enum DisplayType {
            DURABILITY,
            ENERGY,
            CUSTOM,
        }

    }

}
