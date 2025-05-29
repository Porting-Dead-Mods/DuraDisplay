package com.leclowndu93150.duradisplay.mixin;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @Inject(method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At("TAIL"))
    private void renderDuraDisplayDecorations(Font font, ItemStack stack, int x, int y, String text, CallbackInfo ci) {
        if (!stack.isEmpty() && Config.isEnabled()) {
            GuiGraphics guiGraphics = new GuiGraphics(
                    net.minecraft.client.Minecraft.getInstance(),
                    net.minecraft.client.Minecraft.getInstance().renderBuffers().bufferSource()
            );
            DuraDisplayRenderer.INSTANCE.render(guiGraphics, font, stack, x, y);
        }
    }
    GuiGraphics
}