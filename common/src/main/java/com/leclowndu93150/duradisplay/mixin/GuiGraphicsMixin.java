package com.leclowndu93150.duradisplay.mixin;

import com.leclowndu93150.duradisplay.config.Config;
import com.leclowndu93150.duradisplay.render.DuraDisplayRenderer;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin {

    @Redirect(
            method = "renderItemBar(Lnet/minecraft/world/item/ItemStack;II)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;isBarVisible()Z"
            )
    )
    private boolean hideDefaultDurabilityBar(ItemStack stack) {
        return stack.isBarVisible() && !Config.isEnabled();
    }


    @Inject(
            method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At("TAIL")
    )
    private void renderCustomDurabilityDisplay(Font font, ItemStack stack, int x, int y, String text, CallbackInfo ci) {
        if (!stack.isEmpty() && Config.isEnabled()) {
            DuraDisplayRenderer.INSTANCE.render((GuiGraphics)(Object)this, font, stack, x, y);
        }
    }
}