package xyz.synz.synzsmastery.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import xyz.synz.synzsmastery.SynzsMastery;

public class MasterySaveData extends SavedData {

    private final Map<UUID, PlayerSkillData> playerData;
    private final Set<UUID> welcomedPlayers;

    public MasterySaveData() {
        this(new HashMap<>(), new HashSet<>());
    }

    private MasterySaveData(Map<UUID, PlayerSkillData> playerData, Set<UUID> welcomedPlayers) {
        // Defensive copy: whatever we're handed (our own HashMap, or an
        // immutable map produced by the codec when loading from disk) gets
        // copied into a real mutable collection we can safely modify.
        this.playerData = new HashMap<>(playerData);
        this.welcomedPlayers = new HashSet<>(welcomedPlayers);
    }

    private static final Codec<UUID> UUID_CODEC =
            Codec.STRING.xmap(UUID::fromString, UUID::toString);

    private static final Codec<SkillType> SKILL_TYPE_CODEC =
            Codec.STRING.xmap(SkillType::valueOf, SkillType::name);

    private static final Codec<PlayerSkillData> PLAYER_SKILL_DATA_CODEC =
            Codec.unboundedMap(SKILL_TYPE_CODEC, Codec.LONG)
                    .xmap(PlayerSkillData::new, PlayerSkillData::asMap);

    private static final Codec<Map<UUID, PlayerSkillData>> PLAYER_DATA_CODEC =
            Codec.unboundedMap(UUID_CODEC, PLAYER_SKILL_DATA_CODEC);

    private static final Codec<List<UUID>> WELCOMED_LIST_CODEC = UUID_CODEC.listOf();

    private static final Codec<MasterySaveData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PLAYER_DATA_CODEC.fieldOf("players").forGetter(data -> data.playerData),
            WELCOMED_LIST_CODEC.fieldOf("welcomed_players").forGetter(data -> new ArrayList<>(data.welcomedPlayers))
    ).apply(instance, (players, welcomedList) -> new MasterySaveData(players, new HashSet<>(welcomedList))));

    private static final SavedDataType<MasterySaveData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(SynzsMastery.MOD_ID, "player_skill_data"),
            MasterySaveData::new,
            CODEC,
            null
    );

    public static MasterySaveData get(MinecraftServer server) {
        ServerLevel overworld = server.getLevel(ServerLevel.OVERWORLD);

        if (overworld == null) {
            return new MasterySaveData();
        }

        return overworld.getDataStorage().computeIfAbsent(TYPE);
    }

    public PlayerSkillData getPlayerData(UUID playerId) {
        return playerData.computeIfAbsent(playerId, id -> new PlayerSkillData());
    }

    public void incrementSkill(UUID playerId, SkillType skill) {
        incrementSkill(playerId, skill, 1);
    }

    public void incrementSkill(UUID playerId, SkillType skill, long amount) {
        getPlayerData(playerId).increment(skill, amount);
        setDirty();
    }

    public boolean hasSeenWelcome(UUID playerId) {
        return welcomedPlayers.contains(playerId);
    }

    public void markWelcomeSeen(UUID playerId) {
        welcomedPlayers.add(playerId);
        setDirty();
    }
}