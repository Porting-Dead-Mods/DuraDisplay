package com.portingdeadmods.duradisplay.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.portingdeadmods.duradisplay.KeyBinds;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {
    @Shadow public abstract boolean containsPointInScissor(int pX, int pY);

    @ModifyExpressionValue(
            method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;isBarVisible()Z"
            )
    )
    private boolean showBarInGui(boolean barVisible) {
        return !KeyBinds.ForgeClient.modEnabled && barVisible;
    }
}
