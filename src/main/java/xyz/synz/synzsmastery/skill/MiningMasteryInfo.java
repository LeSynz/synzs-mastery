package xyz.synz.synzsmastery.skill;

import xyz.synz.synzsmastery.config.MiningConfig;

public final class MiningMasteryInfo implements SkillMastery {

    @Override
    public String getDisplayName() {
        return "Mining";
    }

    @Override
    public RankTable getRankTable() {
        return MiningConfig.RANK_TABLE;
    }

    @Override
    public String formatProgress(long lifetimeCount) {
        return String.format("%,d blocks mined", lifetimeCount);
    }

    @Override
    public String formatBonus(long lifetimeCount) {
        double bonus = MiningMastery.getSpeedBonus(lifetimeCount);
        return String.format("+%.1f%% mining speed", bonus * 100);
    }
}