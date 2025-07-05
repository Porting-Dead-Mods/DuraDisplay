package com.leclowndu93150.duradisplay.render;

import com.leclowndu93150.duradisplay.compat.BuiltinCompat;
import com.leclowndu93150.duradisplay.config.Config;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3x2fStack;

import java.util.List;

import static com.leclowndu93150.duradisplay.DuraDisplayCommon.BUILTIN_COMPATS;

public class DuraDisplayRenderer {
    public static final DuraDisplayRenderer INSTANCE = new DuraDisplayRenderer();

    private DuraDisplayRenderer() {
    }

    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xPosition, int yPosition) {
        if (!stack.isEmpty() && Config.isEnabled()) {
            for (BuiltinCompat.CompatSupplier supplier : BUILTIN_COMPATS) {
                List<BuiltinCompat> compats = supplier.compat(stack);
                if (compats != null && !compats.isEmpty()) {
                    for (int i = 0; i < compats.size(); i++) {
                        BuiltinCompat compat = compats.get(i);
                        if (compat.active()) {
                            double percentage = compat.percentage();
                            renderText(guiGraphics, font, String.format("%.0f%%", percentage), xPosition, yPosition - i * 5, compat.color());
                        }
                    }
                    return true;
                }
            }
        }
        return true;
    }

    private void renderText(GuiGraphics guiGraphics, Font font, String text, int xPosition, int yPosition, int color) {
        Matrix3x2fStack pose = guiGraphics.pose();
        int stringWidth = font.width(text);
        float x = ((xPosition + 8) * 2 + 1 + stringWidth / 2.0f - stringWidth);
        float y = (yPosition * 2) + 22;
        pose.pushMatrix();
        pose.scale(0.5f, 0.5f);
        guiGraphics.drawString(font, text, (int) x, (int) y, color | 0xFF000000, true);
        pose.popMatrix();
    }
}