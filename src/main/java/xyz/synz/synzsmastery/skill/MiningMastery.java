package xyz.synz.synzsmastery.skill;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import xyz.synz.synzsmastery.SynzsMastery;
import xyz.synz.synzsmastery.config.MiningConfig;

/**
 * Converts a player's lifetime Mining count into an actual mining speed bonus,
 * and applies that bonus to the player's block-break-speed attribute.
 */
public final class MiningMastery {

    private static final Identifier SPEED_MODIFIER_ID =
            Identifier.fromNamespaceAndPath(SynzsMastery.MOD_ID, "mining_mastery_speed");

    private MiningMastery() {
    }

    public static double getSpeedBonus(long lifetimeBlocksMined) {
        return MasteryFormula.diminishingReturns(
                lifetimeBlocksMined,
                MiningConfig.MAX_SPEED_BONUS,
                MiningConfig.SPEED_BONUS_HALF_LIFE,
                MiningConfig.SPEED_BONUS_STEEPNESS
        );
    }

    /**
     * Recomputes and applies the player's mining speed bonus based on their
     * current lifetime Mining count. Safe to call repeatedly — it updates
     * the existing modifier in place rather than stacking duplicates.
     */
    public static void applySpeedBonus(ServerPlayer player, long lifetimeBlocksMined) {
        AttributeInstance attribute = player.getAttribute(Attributes.BLOCK_BREAK_SPEED);

        if (attribute == null) {
            return;
        }

        double bonus = getSpeedBonus(lifetimeBlocksMined);

        attribute.addOrUpdateTransientModifier(new AttributeModifier(
                SPEED_MODIFIER_ID,
                bonus,
                AttributeModifier.Operation.ADD_VALUE
        ));
    }
}