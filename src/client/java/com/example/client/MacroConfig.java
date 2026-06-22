package com.example.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MacroConfig {
    public String command = "sell all";
    public int delaySeconds = 300;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static MacroConfig load() {
        Path path = FabricLoader.getInstance()
                .getConfigDir()
                .resolve("simplemacro.json");

        try {
            if (!Files.exists(path)) {
                MacroConfig defaultConfig = new MacroConfig();
                Files.writeString(path, GSON.toJson(defaultConfig));
                return defaultConfig;
            }

            String json = Files.readString(path);
            MacroConfig config = GSON.fromJson(json, MacroConfig.class);

            if (config == null) {
                return new MacroConfig();
            }

            if (config.command == null) {
                config.command = "sell all";
            }

            if (config.delaySeconds < 1) {
                config.delaySeconds = 300;
            }

            return config;
        } catch (IOException e) {
            e.printStackTrace();
            return new MacroConfig();
        }
    }
}