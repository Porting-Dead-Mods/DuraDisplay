package com.leclowndu93150.duradisplay;

import com.leclowndu93150.duradisplay.api.CustomDisplayItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.IItemDecorator;
import net.neoforged.neoforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public record DuraDisplay(@Nullable CustomDisplayItem customDisplayItem, DisplayType type) implements IItemDecorator {
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xPosition, int yPosition) {
        if (!stack.isEmpty() && (stack.isBarVisible() || (customDisplayItem != null && customDisplayItem.shouldDisplay(stack)))) {
            IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
            DisplayType type = type();
            // Give energystorage a higer prio than durability
            if (energyStorage != null) {
                type = DisplayType.ENERGY;
            }
            switch (type) {
                case DURABILITY -> {
                    if (stack.isDamaged()) {
                        int damage = stack.getDamageValue();
                        int maxDamage = stack.getMaxDamage();
                        double durabilityPercentage = ((double) (maxDamage - damage) / (double) maxDamage) * 100D;
                        renderText(guiGraphics, font, String.format("%.0f%%", durabilityPercentage), xPosition, yPosition, stack.getItem().getBarColor(stack)); // Default color white
                    }
                }
                case ENERGY -> {
                    if (energyStorage != null) {
                        System.out.println("Found energy item: "+stack.getItem());
                        int energyStored = energyStorage.getEnergyStored();
                        int maxEnergyStorage = energyStorage.getMaxEnergyStored();
                        double energyPercentage = ((double) energyStored / (double) maxEnergyStorage) * 100D;
                        renderText(guiGraphics, font, String.format("%.0f%%", energyPercentage), xPosition, yPosition, 0x34D8EB); // Custom color for energy display
                    }
                    // Render regular item bar as a last resort
                    else if (stack.isBarVisible()) {
                        int l = stack.getBarWidth();
                        int i = stack.getBarColor();
                        int j = xPosition + 2;
                        int k = yPosition + 13;
                        guiGraphics.fill(RenderType.guiOverlay(), j, k, j + 13, k + 2, -16777216);
                        guiGraphics.fill(RenderType.guiOverlay(), j, k, j + l, k + 1, i | 0xFF000000);
                    }
                }
                case CUSTOM -> {
                    if (customDisplayItem != null && customDisplayItem.shouldDisplay(stack)) {
                        double percentage = customDisplayItem.getPercentage(stack);
                        int color = customDisplayItem.getColor(stack); // Get color dynamically
                        renderText(guiGraphics, font, String.format("%.0f%%", percentage), xPosition, yPosition, color); // Use custom color
                    }
                }
            }
            return true;
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

    public enum DisplayType {
        DURABILITY,
        ENERGY,
        CUSTOM,
    }
}