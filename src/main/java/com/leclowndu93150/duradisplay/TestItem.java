package com.leclowndu93150.duradisplay;

import com.leclowndu93150.duradisplay.api.CustomDisplayItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TestItem extends Item implements CustomDisplayItem {
    public TestItem() {
        super(new Properties());
    }

    @Override
    public boolean shouldDisplay(ItemStack stack) {
        if (stack.hasTag()) {
            return stack.getTag().getInt("test") > 0;
        }
        return false;
    }

    @Override
    public int getPercentage(ItemStack stack) {
        int maxTest = stack.getOrCreateTag().getInt("maxTest");
        return ((maxTest - stack.getOrCreateTag().getInt("test")) / maxTest) * 100;
    }

    @Override
    public int getColor(ItemStack stack) {
        return 0xFFFFFF;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemInHand = pPlayer.getItemInHand(pUsedHand);
        itemInHand.getOrCreateTag().putInt("maxTest", 100);
        itemInHand.getOrCreateTag().putInt("test", itemInHand.getOrCreateTag().getInt("test") + 1);
        return InteractionResultHolder.success(itemInHand);
    }
}
