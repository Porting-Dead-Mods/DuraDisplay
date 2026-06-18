package com.leclowndu93150.duradisplay.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Config {
    private static final Logger LOGGER = LogManager.getLogger("DuraDisplay");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static File configFile;
    private static boolean enabled = true;

    private Config() {
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void toggle() {
        enabled = !enabled;
        save();
    }

    public static void load() {
        ensureFile();
        if (!configFile.exists()) {
            save();
            return;
        }
        try (FileReader reader = new FileReader(configFile)) {
            ConfigData data = GSON.fromJson(reader, ConfigData.class);
            if (data != null) {
                enabled = data.enabled;
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load config", e);
        }
    }

    public static void save() {
        ensureFile();
        configFile.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(configFile)) {
            GSON.toJson(new ConfigData(enabled), writer);
        } catch (IOException e) {
            LOGGER.error("Failed to save config", e);
        }
    }

    private static void ensureFile() {
        if (configFile == null) {
            Path gameDir = Paths.get("").toAbsolutePath();
            configFile = new File(gameDir.toFile(), "config/duradisplay.json");
        }
    }

    private static final class ConfigData {
        boolean enabled;

        ConfigData(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
