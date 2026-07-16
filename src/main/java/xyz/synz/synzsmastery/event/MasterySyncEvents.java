package xyz.synz.synzsmastery.event;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.level.ServerPlayer;

import xyz.synz.synzsmastery.data.MasterySaveData;
import xyz.synz.synzsmastery.data.SkillType;
import xyz.synz.synzsmastery.skill.MiningMastery;

/**
 * Re-applies all mastery bonuses when a player joins. Attribute modifiers do
 * not survive a relog, so our saved lifetime stats are the real source of
 * truth, and get re-applied fresh every session.
 */
public final class MasterySyncEvents {

    private MasterySyncEvents() {
    }

    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayer player = handler.player;
            MasterySaveData saveData = MasterySaveData.get(server);

            long miningCount = saveData.getPlayerData(player.getUUID()).getLifetimeCount(SkillType.MINING);
            MiningMastery.applySpeedBonus(player, miningCount);
        });
    }
}