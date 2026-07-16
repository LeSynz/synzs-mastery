package xyz.synz.synzsmastery.event;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

import xyz.synz.synzsmastery.data.MasterySaveData;

/**
 * Greets players when they join: a one-time welcome explaining the mod the
 * very first time, and a short flavor line on every session after that.
 */
public final class WelcomeEvents {

    private WelcomeEvents() {
    }

    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayer player = handler.player;
            MasterySaveData saveData = MasterySaveData.get(server);

            if (!saveData.hasSeenWelcome(player.getUUID())) {
                sendFirstJoinMessage(player);
                saveData.markWelcomeSeen(player.getUUID());
            } else {
                sendSessionGreeting(player);
            }
        });
    }

    private static void sendFirstJoinMessage(ServerPlayer player) {
        MutableComponent message = Component.literal("----------------------\n")
                .withStyle(ChatFormatting.DARK_AQUA)
                .append(Component.literal("        ✦ Synz's Mastery ✦\n")
                        .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD))
                .append(Component.literal("----------------------\n")
                        .withStyle(ChatFormatting.DARK_AQUA))
                .append(Component.literal("Welcome, traveler.\n")
                        .withStyle(ChatFormatting.LIGHT_PURPLE))
                .append(Component.literal("In this world, ")
                        .withStyle(ChatFormatting.GRAY))
                .append(Component.literal("mastery is earned")
                        .withStyle(ChatFormatting.GOLD))
                .append(Component.literal("—not given.\n")
                        .withStyle(ChatFormatting.GRAY))
                .append(Component.literal("The more you ")
                        .withStyle(ChatFormatting.GRAY))
                .append(Component.literal("Mine")
                        .withStyle(ChatFormatting.AQUA))
                .append(Component.literal(", ")
                        .withStyle(ChatFormatting.GRAY))
                .append(Component.literal("Farm")
                        .withStyle(ChatFormatting.DARK_GREEN))
                .append(Component.literal(", ")
                        .withStyle(ChatFormatting.GRAY))
                .append(Component.literal("Chop")
                        .withStyle(ChatFormatting.GOLD))
                .append(Component.literal(",\n")
                        .withStyle(ChatFormatting.GRAY))
                .append(Component.literal("Fish")
                        .withStyle(ChatFormatting.BLUE))
                .append(Component.literal(", ")
                        .withStyle(ChatFormatting.GRAY))
                .append(Component.literal("Explore")
                        .withStyle(ChatFormatting.GREEN))
                .append(Component.literal(", and ")
                        .withStyle(ChatFormatting.GRAY))
                .append(Component.literal("Fight")
                        .withStyle(ChatFormatting.RED))
                .append(Component.literal("...\n")
                        .withStyle(ChatFormatting.GRAY))
                .append(Component.literal("the more skilled you naturally become.\n")
                        .withStyle(ChatFormatting.GRAY))
                .append(Component.literal("No levels. No XP bars.\n")
                        .withStyle(ChatFormatting.WHITE))
                .append(Component.literal("Only experience. ")
                        .withStyle(ChatFormatting.GOLD))
                .append(Component.literal("Only practice.\n")
                        .withStyle(ChatFormatting.GREEN))
                .append(Component.literal("----------------------------------------")
                        .withStyle(ChatFormatting.DARK_AQUA));

        player.sendSystemMessage(message);
    }

    private static void sendSessionGreeting(ServerPlayer player) {
        MutableComponent greeting = Component.literal("----------------------------------------\n")
                .withStyle(ChatFormatting.DARK_AQUA)
                .append(Component.literal("  Ah... you return.\n")
                        .withStyle(ChatFormatting.LIGHT_PURPLE))
                .append(Component.literal("  The old ways remember thee well, traveler.\n")
                        .withStyle(ChatFormatting.GRAY))
                .append(Component.literal("----------------------------------------")
                        .withStyle(ChatFormatting.DARK_AQUA));

        player.sendSystemMessage(greeting);
    }
}