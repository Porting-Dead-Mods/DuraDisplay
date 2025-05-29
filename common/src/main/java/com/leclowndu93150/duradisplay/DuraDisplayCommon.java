package com.leclowndu93150.duradisplay;

import com.leclowndu93150.duradisplay.compat.BuiltinCompat;
import com.leclowndu93150.duradisplay.config.Config;
import com.leclowndu93150.duradisplay.platform.services.IPlatformHelper;
import net.minecraft.world.item.BundleItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DuraDisplayCommon {
    public static final String MODID = "duradisplay";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static final List<BuiltinCompat.CompatSupplier> BUILTIN_COMPATS = Collections.synchronizedList(new ArrayList<>());

    public static void init() {
        Config.load();
        registerCompats();
        IPlatformHelper.INSTANCE.registerPlatformCompats();
        IPlatformHelper.INSTANCE.registerKeybinds();
    }

    private static void registerCompats() {
        registerCompat(IPlatformHelper.INSTANCE::getPlatformCompat);

        registerCompat(itemStack -> {
            if (itemStack.isDamageableItem()) {
                List<BuiltinCompat> platformCompat = IPlatformHelper.INSTANCE.getPlatformCompat(itemStack);
                if (platformCompat != null && !platformCompat.isEmpty()) {
                    return null;
                }

                int damage = itemStack.getDamageValue();
                int maxDamage = itemStack.getMaxDamage();
                double durabilityPercentage = ((double) (maxDamage - damage) / (double) maxDamage) * 100D;
                return Collections.singletonList(new BuiltinCompat(durabilityPercentage, itemStack.getBarColor(), itemStack.isBarVisible()));
            }
            return null;
        });

        registerCompat(itemStack -> {
            if (itemStack.getItem() instanceof BundleItem) {
                double percentage = BundleItem.getFullnessDisplay(itemStack) * 100D;
                return Collections.singletonList(new BuiltinCompat(percentage, itemStack.getBarColor(), itemStack.isBarVisible()));
            }
            return null;
        });
    }

    public static void registerCompat(BuiltinCompat.CompatSupplier supplier) {
        BUILTIN_COMPATS.add(supplier);
    }

    public static void onTogglePressed() {
        Config.toggle();
    }
}