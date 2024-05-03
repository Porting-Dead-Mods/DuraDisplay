package com.leclowndu93150.duradisplay.compat;

import com.github.wolfiewaffle.hardcore_torches.item.LanternItem;
import com.github.wolfiewaffle.hardcore_torches.item.TorchItem;
import com.leclowndu93150.duradisplay.Main;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class HTCompat {


    HTCompat(ItemStack stack, Main.DuraDisplay display, GuiGraphics guiGraphics, Font font,int xPosition, int yPosition){
        int fuelStored;
        int maxFuelStorage;
        if (stack.getItem() instanceof TorchItem item) {
            fuelStored = TorchItem.getFuel(stack);
            maxFuelStorage = item.getMaxFuel();
            double fuelPercentage = ((double) fuelStored / (double) maxFuelStorage) * 100D;
            display.renderText(guiGraphics, font, String.format("%.0f%%", fuelPercentage), xPosition, yPosition, 0xe3bb56); // Custom color for energy display
        } else if (stack.getItem() instanceof LanternItem item) {
            fuelStored = LanternItem.getFuel(stack);
            maxFuelStorage = item.getMaxFuel();
            double fuelPercentage = ((double) fuelStored / (double) maxFuelStorage) * 100D;
            display.renderText(guiGraphics, font, String.format("%.0f%%", fuelPercentage), xPosition, yPosition, 0xe3bb56); // Custom color for energy display
        }
        }
    }

