# Synz's Mastery

A **server-side only** Fabric mod for a vanilla+ progression system. No client
installation required — every player on the server benefits automatically.

## Philosophy

Most progression mods bolt on RPG-style levels, XP bars, and skill trees.
Synz's Mastery deliberately avoids that. Instead:

- **No levels. No XP bars.** Players never see a number they need to
  optimize.
- **Lifetime statistics, not levels.** The mod quietly tracks how much
  you've actually done — blocks mined, logs chopped, crops harvested, fish
  caught, distance travelled, mobs defeated.
- **Diminishing returns, not milestones.** Bonuses grow from those lifetime
  stats using a curve that slows down over time. There's no cap you "hit,"
  just a ceiling you slowly approach.
- **Small bonuses.** The goal is a server that still *feels* like vanilla
  Minecraft, just one where doing something a lot makes you quietly, subtly
  better at it — the way a real skill would.

Concretely: mine a lot of stone, and mining gets a little faster. Fish a lot,
and treasure gets a little more likely. Nothing dramatic, nothing that reads
as a number to chase — just steady, natural progress tied to what you
actually played.

## Skills

| Skill | Tracks | Status |
|---|---|---|
| ⛏ Mining | Blocks mined | ✅ Fully implemented |
| 🪓 Woodcutting | Logs chopped | 🚧 Planned |
| 🌾 Farming | Crops harvested | 🚧 Planned |
| 🎣 Fishing | Fish caught | 🚧 Planned |
| 👟 Athletics | Distance travelled | 🚧 Planned |
| ⚔ Combat | Hostile mobs defeated | 🚧 Planned |

Mining currently grants a small mining speed bonus, using a diminishing
returns curve tuned so early progress is slow, gains stretch out over a wide
range, and the bonus never exceeds a small, fixed cap.

The other five skills are scaffolded (they show up in commands with real,
currently-zero progress) but aren't wired to in-game events yet. Adding a
skill follows the same repeatable pattern Mining already uses:

1. An event hook that detects the action and increments the lifetime stat.
2. A tunable config (cap, ramp-up speed, rank thresholds).
3. A formula class converting the stat into a bonus.
4. Applying that bonus in-game (an attribute modifier, or similar).

## Commands

- **`/mastery`** — a one-line summary of every skill's current rank.
- **`/mastery stats <skill>`** — a detailed breakdown for one skill: exact
  progress, current rank, current bonus, and what the next rank requires.

Ranks are named tiers (Beginner → Novice → Apprentice → Skilled →
Experienced → Master) derived from lifetime stats. The underlying numbers
and formulas are intentionally never shown — players see *ranks* and
*bonuses*, not raw XP-style values, to keep the focus on playing rather than
optimizing a number.

## Player experience

New players get a one-time welcome message on their first join, explaining
the mod. Returning players get a short flavor greeting each session instead.
(A rank-up notification — an on-screen message when a skill's rank
increases — is planned but not yet implemented.)

## Persistence

All player statistics are stored **per-world**, inside the world's own save
data (alongside `region/`, `playerdata/`, etc.). This means:

- Stats survive server restarts.
- A fresh world means a fresh start for everyone — mastery is tied to that
  world's history, not a global player profile.

## Requirements

- Minecraft 26.2
- Fabric Loader + Fabric API
- **Server-side only** — no mod required on the client. Players can join
  with a completely vanilla client.

## Project structure

```
xyz.synz.synzsmastery
├── command   — in-game commands (/mastery)
├── config    — tunable per-skill constants (caps, ramp speed, rank tiers)
├── data      — persistent player statistics (SkillType, PlayerSkillData, MasterySaveData)
├── event     — hooks into vanilla game events (block breaking, player join, etc.)
├── skill     — formulas and logic converting stats into ranks/bonuses
└── util      — shared helpers (logging, player notifications)
```

## Building

Standard Fabric mod build:

```
./gradlew build
```

The compiled mod jar will be in `build/libs/`.