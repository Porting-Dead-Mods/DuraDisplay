package com.leclowndu93150.duradisplay;

import com.leclowndu93150.duradisplay.compat.BuiltinCompat;
import com.leclowndu93150.duradisplay.compat.CompatRegistry;
import com.leclowndu93150.duradisplay.config.Config;
import net.minecraft.world.item.BundleItem;

import java.util.Collections;
import java.util.List;

public final class DuraDisplay {
    public static final String MOD_ID = "duradisplay";
    public static final String MC_VERSION = new String("26.2");

    private DuraDisplay() {
    }

    public static void init() {
        Config.load();
        registerBuiltinCompats();
    }

    private static void registerBuiltinCompats() {
        CompatRegistry.register(stack -> {
            if (stack.isDamageableItem()) {
                List<BuiltinCompat> platform = CompatRegistry.platformCompat(stack);
                if (platform != null && !platform.isEmpty()) {
                    return null;
                }
                int damage = stack.getDamageValue();
                int maxDamage = stack.getMaxDamage();
                double percentage = ((double) (maxDamage - damage) / (double) maxDamage) * 100D;
                return Collections.singletonList(new BuiltinCompat(percentage, stack.getBarColor(), stack.isBarVisible()));
            }
            return null;
        });

        CompatRegistry.register(stack -> {
            if (stack.getItem() instanceof BundleItem) {
                double percentage = BundleItem.getFullnessDisplay(stack) * 100D;
                return Collections.singletonList(new BuiltinCompat(percentage, stack.getBarColor(), stack.isBarVisible()));
            }
            return null;
        });
    }
}
