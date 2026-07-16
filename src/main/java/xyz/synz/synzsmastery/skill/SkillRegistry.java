package xyz.synz.synzsmastery.skill;

import java.util.EnumMap;
import java.util.Map;

import xyz.synz.synzsmastery.data.SkillType;

/**
 * Central lookup from SkillType to its SkillMastery implementation. As each
 * skill gets fully wired up (events + a real bonus formula), swap its entry
 * here from a placeholder to its real implementation — nothing else needs
 * to change, including this command.
 */
public final class SkillRegistry {

    private static final Map<SkillType, SkillMastery> REGISTRY = new EnumMap<>(SkillType.class);

    static {
        REGISTRY.put(SkillType.MINING, new MiningMasteryInfo());
        REGISTRY.put(SkillType.WOODCUTTING, new UnimplementedSkillMastery("Woodcutting", "logs chopped"));
        REGISTRY.put(SkillType.FARMING, new UnimplementedSkillMastery("Farming", "crops harvested"));
        REGISTRY.put(SkillType.FISHING, new UnimplementedSkillMastery("Fishing", "fish caught"));
        REGISTRY.put(SkillType.ATHLETICS, new UnimplementedSkillMastery("Athletics", "km travelled"));
        REGISTRY.put(SkillType.COMBAT, new UnimplementedSkillMastery("Combat", "mobs defeated"));
    }

    private SkillRegistry() {
    }

    public static SkillMastery get(SkillType type) {
        return REGISTRY.get(type);
    }
}