package com.leclowndu93150.duradisplay.renderer;

import com.leclowndu93150.duradisplay.KeyBind;
import com.leclowndu93150.duradisplay.Main;
import com.leclowndu93150.duradisplay.compat.BuiltinCompat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemDecorator;

import static com.leclowndu93150.duradisplay.Main.BUILTIN_COMPATS;

public class DuraDisplay implements IItemDecorator {
    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xPosition, int yPosition) {
        if (!stack.isEmpty() && stack.isBarVisible()) {
            if (KeyBind.ForgeClient.modEnabled) {
                for (BuiltinCompat.CompatSupplier supplier : BUILTIN_COMPATS) {
                    BuiltinCompat compat = supplier.getCompat(stack);
                    if (compat != null && compat.active()) {
                        double percentage = compat.percentage();
                        renderText(guiGraphics, font, String.format("%.0f%%", percentage), xPosition, yPosition, compat.color());
                        return true;
                    }
                }
            }

            renderItemBar(stack, xPosition, yPosition, guiGraphics);
        }
        return true;
    }

    public void renderText(GuiGraphics guiGraphics, Font font, String text, int xPosition, int yPosition, int color) {
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

    private void renderItemBar(ItemStack stack, int xPosition, int yPosition, GuiGraphics guiGraphics) {
        int l = stack.getBarWidth();
        int i = stack.getBarColor();
        int j = xPosition + 2;
        int k = yPosition + 13;
        guiGraphics.fill(RenderType.guiOverlay(), j, k, j + 13, k + 2, -16777216);
        guiGraphics.fill(RenderType.guiOverlay(), j, k, j + l, k + 1, i | 0xFF000000);
    }
}