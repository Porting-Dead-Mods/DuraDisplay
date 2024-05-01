package com.leclowndu93150.duradisplay;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(Main.MODID)
public class Main
{
    public static final Logger LOGGER = LogUtils.getLogger();
    // Define mod id in a common place for everything to reference
    public static final String MODID = "duradisplay";

    public static boolean renderDurability(Font font, ItemStack stack, int xPosition, int yPosition) {
        if(KeyBind.ForgeClient.modEnabled){
            if (!stack.isEmpty() && stack.isBarVisible()) {
                LazyOptional<IEnergyStorage> energyStorage = stack.getCapability(CapabilityEnergy.ENERGY);
                DisplayType type = null;
                // Give energystorage a higer prio than durability
                if (energyStorage.isPresent()) {
                    type = DisplayType.ENERGY;
                } else if (stack.isDamaged()) {
                    type = DisplayType.DURABILITY;
                }
                if (type != null) {
                    switch (type) {
                        case DURABILITY:
                            if (stack.isDamaged()) {
                                int damage = stack.getDamageValue();
                                int maxDamage = stack.getMaxDamage();
                                double durabilityPercentage = ((double) (maxDamage - damage) / (double) maxDamage) * 100D;
                                renderText(font, String.format("%.0f%%", durabilityPercentage), xPosition, yPosition, stack.getItem().getBarColor(stack)); // Default color white
                            }
                            break;
                        case ENERGY:
                            if (energyStorage.isPresent()) {
                                energyStorage.ifPresent(es -> {
                                    int energyStored = es.getEnergyStored();
                                    int maxEnergyStorage = es.getMaxEnergyStored();
                                    double energyPercentage = ((double) energyStored / (double) maxEnergyStorage) * 100D;
                                    renderText(font, String.format("%.0f%%", energyPercentage), xPosition, yPosition, 0x34D8EB); // Custom color for energy display
                                });
                            } else {
                                int l = stack.getBarWidth();
                                int i = stack.getBarColor();
                                int j = xPosition + 2;
                                int k = yPosition + 13;
                                GuiComponent.fill(new PoseStack(), j, k, j + 13, k + 2, -16777216);
                                GuiComponent.fill(new PoseStack(), j, k, j + l, k + 1, i | 0xFF000000);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return false;
    }

    private static void renderText(Font font, String text, int xPosition, int yPosition, int color) {
        PoseStack poseStack = new PoseStack();
        int stringWidth = font.width(text);
        int x = ((xPosition + 8) * 2 + 1 + stringWidth / 2 - stringWidth);
        int y = (yPosition * 2) + 22;
        poseStack.pushPose();
        poseStack.scale(0.5F, 0.5F, 0.5F);
        poseStack.translate(0.0D, 0.0D, 500.0D);
        MultiBufferSource.BufferSource multibuffersource$buffersource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        font.drawInBatch(text, x, y, color, true, poseStack.last().pose(), multibuffersource$buffersource, true, 0, 15728880, false);
        multibuffersource$buffersource.endBatch();
        poseStack.popPose();
    }

    public enum DisplayType {
        DURABILITY,
        ENERGY,
    }
}
