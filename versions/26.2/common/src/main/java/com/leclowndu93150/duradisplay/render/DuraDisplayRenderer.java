package com.leclowndu93150.duradisplay.render;

import com.leclowndu93150.duradisplay.compat.BuiltinCompat;
import com.leclowndu93150.duradisplay.compat.CompatRegistry;
import com.leclowndu93150.duradisplay.config.Config;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3x2fStack;

import java.util.List;

public final class DuraDisplayRenderer {
    public static final DuraDisplayRenderer INSTANCE = new DuraDisplayRenderer();

    private DuraDisplayRenderer() {
    }

    public void render(GuiGraphicsExtractor guiGraphics, Font font, ItemStack stack, int x, int y) {
        if (stack.isEmpty() || !Config.isEnabled()) {
            return;
        }
        for (BuiltinCompat.Supplier supplier : CompatRegistry.suppliers()) {
            List<BuiltinCompat> compats = supplier.compat(stack);
            if (compats != null && !compats.isEmpty()) {
                for (int i = 0; i < compats.size(); i++) {
                    BuiltinCompat compat = compats.get(i);
                    if (compat.active()) {
                        renderText(guiGraphics, font, String.format("%.0f%%", compat.percentage()), x, y - i * 5, compat.color());
                    }
                }
                return;
            }
        }
    }

    private void renderText(GuiGraphicsExtractor guiGraphics, Font font, String text, int x, int y, int color) {
        Matrix3x2fStack pose = guiGraphics.pose();
        int width = font.width(text);
        float textX = ((x + 8) * 2 + 1 + width / 2.0f - width);
        float textY = (y * 2) + 22;
        pose.pushMatrix();
        pose.scale(0.5f, 0.5f);
        guiGraphics.text(font, text, (int) textX, (int) textY, color | 0xFF000000, true);
        pose.popMatrix();
    }
}
