package xyz.synz.synzsmastery.data;

import java.util.EnumMap;
import java.util.Map;

/**
 * Holds one player's lifetime statistics for every skill.
 * This class only tracks numbers in memory — it knows nothing about
 * saving/loading, and nothing about bonuses or formulas.
 */
public class PlayerSkillData {

    private final Map<SkillType, Long> lifetimeCounts = new EnumMap<>(SkillType.class);

    public PlayerSkillData() {
    }

    /**
     * Package-private: only persistence code in this same package is allowed
     * to rebuild a PlayerSkillData directly from raw saved data.
     */
    PlayerSkillData(Map<SkillType, Long> savedCounts) {
        lifetimeCounts.putAll(savedCounts);
    }

    public long getLifetimeCount(SkillType skill) {
        return lifetimeCounts.getOrDefault(skill, 0L);
    }

    public void increment(SkillType skill) {
        increment(skill, 1);
    }

    public void increment(SkillType skill, long amount) {
        lifetimeCounts.merge(skill, amount, Long::sum);
    }

    /**
     * Package-private: exposes the raw map only to persistence code,
     * so it can be written to disk. Everyone else must use getLifetimeCount/increment.
     */
    Map<SkillType, Long> asMap() {
        return lifetimeCounts;
    }
}