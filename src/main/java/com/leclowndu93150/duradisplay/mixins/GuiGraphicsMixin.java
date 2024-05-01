package com.leclowndu93150.duradisplay.mixins;

import com.leclowndu93150.duradisplay.KeyBind;
import com.leclowndu93150.duradisplay.Main;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemRenderer.class)
public abstract class GuiGraphicsMixin {
    @ModifyExpressionValue(
            method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;isBarVisible()Z"
            )
    )
    private boolean disableBarInGui(boolean barVisible) {
        return !KeyBind.ForgeClient.modEnabled;
    }

    @Inject(
            method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At("TAIL")
    )
    public void renderItemDecoration(Font p_115175_, ItemStack p_115176_, int p_115177_, int p_115178_, String p_115179_, CallbackInfo ci){
        if (KeyBind.ForgeClient.modEnabled)
            Main.renderDurability(p_115175_,p_115176_,p_115177_,p_115178_);
    }
}
