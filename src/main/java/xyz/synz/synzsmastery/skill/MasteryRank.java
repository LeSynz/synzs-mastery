package xyz.synz.synzsmastery.skill;

/**
 * The named tiers a player can reach in a skill. Deliberately vague/flavorful
 * rather than numeric — players see "Skilled," never "Level 17" or raw XP.
 */
public enum MasteryRank {
    BEGINNER,
    NOVICE,
    APPRENTICE,
    SKILLED,
    EXPERIENCED,
    MASTER;

    /** e.g. "Skilled" instead of "SKILLED" */
    public String displayName() {
        String name = name();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }
}