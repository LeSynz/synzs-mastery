package xyz.synz.synzsmastery.config;

import xyz.synz.synzsmastery.skill.RankTable;

public final class MiningConfig {

    private MiningConfig() {
    }

    /** Maximum possible mining speed bonus, as a fraction (0.05 = +5%). */
    public static final double MAX_SPEED_BONUS = 0.05;

    /** Lifetime blocks mined at which the curve reaches its "midpoint" shape. */
    public static final double SPEED_BONUS_HALF_LIFE = 11000;

    /** Lower = slower early growth, stretched over a wider range. */
    public static final double SPEED_BONUS_STEEPNESS = 0.5;

    public static final RankTable RANK_TABLE = new RankTable(100, 1000, 5000, 20000, 100000);
}