package com.leclowndu93150.duradisplay;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.IItemDecorator;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(Main.MODID)
public class Main
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "duradisplay";

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientEvents
    {
        @SubscribeEvent
        public static void onRegisterItemDecorations(final RegisterItemDecorationsEvent event) {
            DuraDisplay duradisplay = new DuraDisplay();
            BuiltInRegistries.ITEM.stream().filter(Item::canBeDepleted).forEach(item -> event.register(item,duradisplay));
        }
    }

    private static class DuraDisplay implements IItemDecorator {

        public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xPosition, int yPosition) {
            if (!stack.isEmpty() && stack.isDamaged()) {
                PoseStack poseStack = guiGraphics.pose();

                int damage = stack.getDamageValue();
                int maxDamage = stack.getMaxDamage();

                double durabilityPercentage = ((double) (maxDamage - damage) / (double) maxDamage) * 100.0;

                String formattedPercentage = String.format("%.0f%%", durabilityPercentage);

                int stringWidth = font.width(formattedPercentage);
                int x = ((xPosition + 8) * 2 + 1 + stringWidth / 2 - stringWidth);
                int y = (yPosition * 2) + 18;

                int color = stack.getItem().getBarColor(stack);

                poseStack.pushPose();
                poseStack.scale(0.5F, 0.5F, 0.5F);
                poseStack.translate(0.0D, 0.0D, 750.0F);
                MultiBufferSource.BufferSource multibuffersource$buffersource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
                font.drawInBatch(formattedPercentage, x, y, color, true, poseStack.last().pose(), multibuffersource$buffersource, Font.DisplayMode.NORMAL, 0, 15728880, false);
                multibuffersource$buffersource.endBatch();
                poseStack.popPose();

            }
            return true;
        }

    }

}
