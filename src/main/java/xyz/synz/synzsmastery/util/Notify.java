package xyz.synz.synzsmastery.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

/**
 * Helpers for broadcasting messages to every player on the server.
 *
 * <p>Chat broadcasts are prefixed with the mod tag; action-bar (overlay)
 * broadcasts are sent verbatim so they stay short. All methods are
 * null-safe on the server argument so they can be called freely from
 * events that may fire before/after a server is available.
 */
public final class Notify {

    private Notify() {}

    /** Tag shown in front of every chat broadcast. */
    private static final Component PREFIX =
            Component.literal("[Mastery] ").withStyle(ChatFormatting.GOLD);

    // ---------------------------------------------------------------------
    // Chat broadcasts (appear in the chat box, prefixed with the mod tag)
    // ---------------------------------------------------------------------

    /** Broadcast a plain (white) chat message to all online players. */
    public static void broadcast(MinecraftServer server, String message) {
        broadcast(server, Component.literal(message));
    }

    /** Broadcast a pre-built chat component to all online players. */
    public static void broadcast(MinecraftServer server, Component message) {
        if (server == null || message == null) {
            return;
        }
        server.getPlayerList().broadcastSystemMessage(prefixed(message), false);
    }

    /** Broadcast an informational (aqua) message to all online players. */
    public static void info(MinecraftServer server, String message) {
        broadcast(server, Component.literal(message).withStyle(ChatFormatting.AQUA));
    }

    /** Broadcast a success (green) message to all online players. */
    public static void success(MinecraftServer server, String message) {
        broadcast(server, Component.literal(message).withStyle(ChatFormatting.GREEN));
    }

    /** Broadcast a warning (yellow) message to all online players. */
    public static void warn(MinecraftServer server, String message) {
        broadcast(server, Component.literal(message).withStyle(ChatFormatting.YELLOW));
    }

    /** Broadcast an error (red) message to all online players. */
    public static void error(MinecraftServer server, String message) {
        broadcast(server, Component.literal(message).withStyle(ChatFormatting.RED));
    }

    // ---------------------------------------------------------------------
    // Action bar broadcasts (overlay above the hotbar, no prefix)
    // ---------------------------------------------------------------------

    /** Broadcast a message to the action bar of all online players. */
    public static void actionBar(MinecraftServer server, String message) {
        actionBar(server, Component.literal(message));
    }

    /** Broadcast a pre-built component to the action bar of all online players. */
    public static void actionBar(MinecraftServer server, Component message) {
        if (server == null || message == null) {
            return;
        }
        server.getPlayerList().broadcastSystemMessage(message, true);
    }

    // ---------------------------------------------------------------------
    // Single-player convenience (same styling, one recipient)
    // ---------------------------------------------------------------------

    /** Send a prefixed chat message to a single player. */
    public static void send(ServerPlayer player, String message) {
        send(player, Component.literal(message));
    }

    /** Send a prefixed chat component to a single player. */
    public static void send(ServerPlayer player, Component message) {
        if (player == null || message == null) {
            return;
        }
        player.sendSystemMessage(prefixed(message));
    }

    // ---------------------------------------------------------------------

    private static Component prefixed(Component message) {
        MutableComponent out = Component.empty();
        out.append(PREFIX);
        out.append(message);
        return out;
    }
}
