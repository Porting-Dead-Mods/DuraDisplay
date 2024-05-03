package com.leclowndu93150.duradisplay;

import com.leclowndu93150.duradisplay.api.CustomDisplayItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class TestItem extends Item implements CustomDisplayItem {
    public TestItem() {
        super(new Properties().component(Main.TEST_DATA, 0));
    }

    @Override
    public boolean shouldDisplay(ItemStack stack) {
        return true;
    }

    @Override
    public int getPercentage(ItemStack stack) {
        int maxTest = 100;
        float test = (float) (maxTest - stack.getOrDefault(Main.TEST_DATA, 0)) / maxTest;
        Main.LOGGER.info("Percentage: {}%", test * 100);
        return (int) (test * 100);
    }

    @Override
    public int getColor(ItemStack stack) {
        return 0xFFFFFF;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack itemInHand = pPlayer.getItemInHand(pUsedHand);
        itemInHand.set(Main.TEST_DATA, itemInHand.getOrDefault(Main.TEST_DATA, 0) + 1);
        return InteractionResultHolder.success(itemInHand);
    }
}
