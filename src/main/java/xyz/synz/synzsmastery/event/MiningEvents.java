package xyz.synz.synzsmastery.event;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;

import xyz.synz.synzsmastery.data.MasterySaveData;
import xyz.synz.synzsmastery.data.SkillType;
import xyz.synz.synzsmastery.skill.MiningMastery;

/**
 * Hooks into block-breaking to grow the player's lifetime Mining stat,
 * and keeps their mining speed bonus in sync with that stat.
 */
public final class MiningEvents {

    private MiningEvents() {
    }

    public static void register() {
        PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, blockEntity) -> {
            if (player.isCreative()) {
                return;
            }

            if (!state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
                return;
            }

            MinecraftServer server = level.getServer();
            if (server == null) {
                return;
            }

            MasterySaveData saveData = MasterySaveData.get(server);
            saveData.incrementSkill(player.getUUID(), SkillType.MINING);

            long lifetimeCount = saveData.getPlayerData(player.getUUID()).getLifetimeCount(SkillType.MINING);
            MiningMastery.applySpeedBonus((ServerPlayer) player, lifetimeCount);
        });
    }
}