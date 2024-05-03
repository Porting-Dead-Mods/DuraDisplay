package com.leclowndu93150.duradisplay.compat;
import com.leclowndu93150.duradisplay.Main;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class DependencyHandler
{
    public static void ifHTloaded(ItemStack stack, Main.DuraDisplay display, GuiGraphics guiGraphics, Font font, int xPosition, int yPosition) {
        if (ModList.get().isLoaded("hardcore_torches")) {
            HTCompat htCompat = new HTCompat(stack,display,guiGraphics,font,xPosition,yPosition);
        }
    }

}
