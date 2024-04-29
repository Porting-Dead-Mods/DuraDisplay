package com.leclowndu93150.duradisplay.Compat;

import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper;
import com.gregtechceu.gtceu.api.capability.IPlatformEnergyStorage;
import com.leclowndu93150.duradisplay.Main;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.IItemDecorator;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import static com.leclowndu93150.duradisplay.Main.MODID;


@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents
{

    @SubscribeEvent
    private static void onRegisterItemDecorations(final RegisterItemDecorationsEvent event)
    {
        for (Item item : ForgeRegistries.ITEMS)
        {

           if() {
                event.register(item, new DuraDisplay(null,GregTech.DisplayType.GREGTECH));
            }
        }
    }
}


public class GregTech{

    public record Compat() implements IItemDecorator {
        @Override
        public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xPosition, int yPosition) {

            IPlatformEnergyStorage euStorage = GTCapabilityHelper.getPlatformEnergyItem(stack);
            long energyStored = euStorage.getAmount();
            long maxEnergyStorage = euStorage.getCapacity();
            double energyPercentage = ((double) energyStored / (double) maxEnergyStorage) * 100D;
            Main.DuraDisplay.renderText(guiGraphics, font, String.format("%.0f%%", energyPercentage), xPosition, yPosition, 0x34D8EB); // Custom color for energy display


            return true;
        }
    }


    private enum DisplayType {
       GREGTECH,
    }

}
