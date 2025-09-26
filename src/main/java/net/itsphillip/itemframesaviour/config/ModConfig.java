package net.itsphillip.itemframesaviour.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = FabricLoader.getInstance()
            .getConfigDir().resolve("itemframesaviour.json").toFile();

    public static boolean ENABLED = true; // default

    /** Load the config from file (if exists) */
    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                ModConfigData data = GSON.fromJson(reader, ModConfigData.class);
                if (data != null) {
                    ENABLED = data.enabled;
                }
            } catch (IOException | JsonSyntaxException e) {
                System.err.println("Failed to load Item Frame Saviour config, using defaults");
                e.printStackTrace();
            }
        } else {
            save(); // create default config
        }
    }

    /** Save the current config to file */
    public static void save() {
        ModConfigData data = new ModConfigData();
        data.enabled = ENABLED;

        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(data, writer);
        } catch (IOException e) {
            System.err.println("Failed to save Item Frame Saviour config");
            e.printStackTrace();
        }
    }

    /** Internal data holder for JSON serialization */
    private static class ModConfigData {
        boolean enabled = true;
    }
}
