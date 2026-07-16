package xyz.synz.synzsmastery.skill;

/**
 * Everything the command layer needs to display a skill in player-facing
 * text: how to describe progress and bonuses, and its rank thresholds.
 * Implemented once per skill.
 */
public interface SkillMastery {
    String getDisplayName();
    RankTable getRankTable();
    String formatProgress(long lifetimeCount);
    String formatBonus(long lifetimeCount);
}