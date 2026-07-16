package xyz.synz.synzsmastery.skill;

/**
 * Placeholder for skills that don't have event hooks or a bonus formula
 * wired up yet. Shows real (currently-zero) progress rather than fake data,
 * and is upfront that no bonus is active yet.
 */
public final class UnimplementedSkillMastery implements SkillMastery {

    private static final RankTable PLACEHOLDER_RANKS = new RankTable(100, 1000, 5000, 20000, 100000);

    private final String displayName;
    private final String progressUnit;

    public UnimplementedSkillMastery(String displayName, String progressUnit) {
        this.displayName = displayName;
        this.progressUnit = progressUnit;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public RankTable getRankTable() {
        return PLACEHOLDER_RANKS;
    }

    @Override
    public String formatProgress(long lifetimeCount) {
        return String.format("%,d %s", lifetimeCount, progressUnit);
    }

    @Override
    public String formatBonus(long lifetimeCount) {
        return "Not yet active";
    }
}