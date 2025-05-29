package com.leclowndu93150.duradisplay.mixin;

import com.gregtechceu.gtceu.client.renderer.item.GTItemBarRenderer;
import com.leclowndu93150.duradisplay.config.Config;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GTItemBarRenderer.class)
public abstract class GTItemBarRendererMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(GuiGraphics guiGraphics, Font font, ItemStack stack, int x, int y, CallbackInfoReturnable<Boolean> cir) {
        if (Config.isEnabled()) {
            cir.setReturnValue(false);
        }
    }
}