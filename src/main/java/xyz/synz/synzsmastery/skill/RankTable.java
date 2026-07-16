package xyz.synz.synzsmastery.skill;

/**
 * Maps a lifetime activity count to a MasteryRank for one skill, using a
 * fixed set of thresholds. Every skill gets its own RankTable instance with
 * its own thresholds, since "4,000 blocks mined" and "4,000 fish caught"
 * shouldn't necessarily feel the same.
 */
public final class RankTable {

    // thresholds[0] = count needed for NOVICE, [1] = APPRENTICE, [2] = SKILLED,
    // [3] = EXPERIENCED, [4] = MASTER. Below thresholds[0], the rank is BEGINNER.
    private final long[] thresholds;

    public RankTable(long noviceAt, long apprenticeAt, long skilledAt, long experiencedAt, long masterAt) {
        this.thresholds = new long[] { noviceAt, apprenticeAt, skilledAt, experiencedAt, masterAt };
    }

    public MasteryRank getRank(long lifetimeCount) {
        MasteryRank current = MasteryRank.BEGINNER;

        for (int i = 0; i < thresholds.length; i++) {
            if (lifetimeCount < thresholds[i]) {
                break;
            }
            current = MasteryRank.values()[i + 1];
        }

        return current;
    }

    /**
     * The lifetime count required to reach the next rank, or -1 if the
     * player has already reached the highest rank (MASTER).
     */
    public long getNextRankThreshold(long lifetimeCount) {
        for (long threshold : thresholds) {
            if (lifetimeCount < threshold) {
                return threshold;
            }
        }
        return -1;
    }
}