package xyz.synz.synzsmastery.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.EnumMap;
import java.util.Map;

import xyz.synz.synzsmastery.data.MasterySaveData;
import xyz.synz.synzsmastery.data.PlayerSkillData;
import xyz.synz.synzsmastery.data.SkillType;
import xyz.synz.synzsmastery.skill.MasteryRank;
import xyz.synz.synzsmastery.skill.RankTable;
import xyz.synz.synzsmastery.skill.SkillMastery;
import xyz.synz.synzsmastery.skill.SkillRegistry;

/**
 * /mastery               - summary of every skill's current rank
 * /mastery stats <skill> - full breakdown for one skill
 */
public final class MasteryCommand {

    private static final Map<SkillType, String> ICONS = new EnumMap<>(SkillType.class);

    static {
        ICONS.put(SkillType.MINING, "⛏");
        ICONS.put(SkillType.WOODCUTTING, "🪓");
        ICONS.put(SkillType.FARMING, "🌾");
        ICONS.put(SkillType.FISHING, "🎣");
        ICONS.put(SkillType.ATHLETICS, "👟");
        ICONS.put(SkillType.COMBAT, "⚔");
    }

    private MasteryCommand() {
    }

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                registerCommands(dispatcher));
    }

    private static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        var root = Commands.literal("mastery")
                .executes(MasteryCommand::showSummary);

        var statsNode = Commands.literal("stats");

        for (SkillType type : SkillType.values()) {
            statsNode.then(Commands.literal(type.name().toLowerCase())
                    .executes(context -> showSkillDetail(context.getSource(), type)));
        }

        root.then(statsNode);
        dispatcher.register(root);
    }

    private static int showSummary(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        MasterySaveData saveData = MasterySaveData.get(context.getSource().getServer());
        PlayerSkillData data = saveData.getPlayerData(player.getUUID());

        MutableComponent message = Component.empty()
                .append(Component.literal("✦ Synz's Mastery\n").withStyle(ChatFormatting.GOLD))
                .append(Component.literal("\n"));

        for (SkillType type : SkillType.values()) {
            SkillMastery mastery = SkillRegistry.get(type);
            long count = data.getLifetimeCount(type);
            MasteryRank rank = mastery.getRankTable().getRank(count);

            message.append(summaryLine(type, mastery, rank)).append(Component.literal("\n"));
        }

        message.append(Component.literal("\n"))
                .append(Component.literal("/mastery stats <skill>").withStyle(ChatFormatting.DARK_GRAY));

        context.getSource().sendSuccess(() -> message, false);
        return 1;
    }

    private static MutableComponent summaryLine(SkillType type, SkillMastery mastery, MasteryRank rank) {
        return Component.empty()
                .append(Component.literal(ICONS.get(type) + " ").withStyle(ChatFormatting.WHITE))
                .append(Component.literal(mastery.getDisplayName()).withStyle(ChatFormatting.GRAY))
                .append(Component.literal(" • ").withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal(rank.displayName()).withStyle(ChatFormatting.WHITE));
    }

    private static int showSkillDetail(CommandSourceStack source, SkillType type) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        MasterySaveData saveData = MasterySaveData.get(source.getServer());
        PlayerSkillData data = saveData.getPlayerData(player.getUUID());
        long count = data.getLifetimeCount(type);

        SkillMastery mastery = SkillRegistry.get(type);
        RankTable rankTable = mastery.getRankTable();
        MasteryRank rank = rankTable.getRank(count);
        long nextThreshold = rankTable.getNextRankThreshold(count);

        MutableComponent message = Component.empty()
                .append(Component.literal(ICONS.get(type) + " " + mastery.getDisplayName() + " Mastery\n")
                        .withStyle(ChatFormatting.GOLD))
                .append(Component.literal("\n"))
                .append(detailLine("Progress", mastery.formatProgress(count), ChatFormatting.WHITE))
                .append(Component.literal("\n"))
                .append(detailLine("Rank", rank.displayName(), ChatFormatting.WHITE))
                .append(Component.literal("\n"))
                .append(detailLine("Bonus", mastery.formatBonus(count), ChatFormatting.GREEN));

        if (nextThreshold != -1) {
            message.append(Component.literal("\n"))
                    .append(detailLine("Next", mastery.formatProgress(nextThreshold), ChatFormatting.WHITE))
                    .append(Component.literal("\n"))
                    .append(detailLine("Reward", mastery.formatBonus(nextThreshold), ChatFormatting.GREEN));
        } else {
            message.append(Component.literal("\n"))
                    .append(Component.literal("Maximum rank reached!").withStyle(ChatFormatting.GOLD));
        }

        source.sendSuccess(() -> message, false);
        return 1;
    }

    private static MutableComponent detailLine(String label, String value, ChatFormatting valueColor) {
        return Component.empty()
                .append(Component.literal(label).withStyle(ChatFormatting.GRAY))
                .append(Component.literal(" » ").withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal(value).withStyle(valueColor));
    }
}