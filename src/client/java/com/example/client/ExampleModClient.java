package com.example.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ExampleModClient implements ClientModInitializer {
    private static boolean macroEnabled = false;
    private static int tickCounter = 0;

    private static KeyBinding toggleKey;
    private static MacroConfig config;

    @Override
    public void onInitializeClient() {
        config = MacroConfig.load();

        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.simplemacro.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F8,
                "category.simplemacro"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                macroEnabled = !macroEnabled;
                tickCounter = 0;

                if (client.player != null) {
                    client.player.sendMessage(
                            Text.literal("Simple Macro " + (macroEnabled ? "enabled" : "disabled")),
                            false
                    );
                }
            }

            if (client.player == null || !macroEnabled) {
                tickCounter = 0;
                return;
            }

            tickCounter++;

            int delayTicks = Math.max(1, config.delaySeconds) * 20;

            if (tickCounter >= delayTicks) {
                sendCommand(client, config.command);
                tickCounter = 0;
            }
        });
    }

    private static void sendCommand(MinecraftClient client, String command) {
        if (client.player == null || client.player.networkHandler == null) {
            return;
        }

        command = command.trim();

        if (command.startsWith("/")) {
            command = command.substring(1);
        }

        if (command.isEmpty()) {
            return;
        }

        client.player.networkHandler.sendCommand(command);
    }
}