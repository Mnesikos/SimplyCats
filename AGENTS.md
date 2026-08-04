# AGENTS.md

Guidance for AI coding agents working in this repository.

## What this is

Simply Cats — a Minecraft mod adding genetics-based cats. Java 21, NeoForge 1.21.1, ModDevGradle.

**Branch layout is per-Minecraft-version.** Each MC version lives on its own long-lived branch (`forge/1.7.10` … `forge/1.20.1`, `neoforge/1.21.1`); `master` is historical. There is no shared trunk — features reach a new version by porting from the previous version branch. `neoforge/1.21.1` descends directly from `forge/1.20.1`'s tip, so a diff against `forge/1.20.1` shows exactly the port delta.

## Commands

```bash
./gradlew build                # compile + jar → build/libs/simplycats-<mc>-<ver>.jar
./gradlew compileJava          # fast compile check
./gradlew runData              # datagen → src/generated/resources (run after touching data/ providers)
./gradlew runClient            # launch dev client
./gradlew runGameTestServer    # boots server with mod loaded; FAILS with "No test functions were given!"
                               # — that error is EXPECTED (no gametests registered) and everything
                               # before it (registries, config, events) is the actual smoke test
```

There is no test suite and no lint/format gate. Versions (minecraft, neo, parchment, JEI) live in `gradle.properties`.

`javac` caps at 100 errors by default; during large migrations raise it with an init script (`options.compilerArgs.addAll(['-Xmaxerrs', '10000'])`) before estimating scope.

## Architecture

- **Entry point** `SimplyCats.java` — ctor `(IEventBus, ModContainer)`; owns every `DeferredRegister` (entity, blocks, items, sounds, POI, professions, creative tab) and the datagen `gatherData` wiring.
- **Genetics core** `entity/core/Genetics.java` — cat coat genetics as gene-string pairs (e.g. `"B-b"`, `"Xo-Y"`). Genes are stored as String synched-entity-data on `SimplyCatEntity` and persisted to NBT verbatim. Phenotype (name + texture layers) is derived, never stored.
- **`SimplyCatEntity`** — the giant class. Breeding/heat/pregnancy state machine, resting poses, owner data. AI behaviors live in `entity/goal/`.
- **Rendering** `client/render/entity/` — `SimplyCatRenderer.getTextureLocation` composites genetics texture layers at runtime via `LayeredTexture` (registered into the TextureManager on first render, cached by layer-string key). Not an atlas; each unique genotype is its own dynamic texture.
- **Village content** `worldgen/villages/SCWorldGen` — runtime injection: mutates vanilla `StructureTemplatePool` fields (opened by the access transformer) on `ServerAboutToStartEvent`. Shelter buildings are jigsaw pieces added to each biome's `village/*/houses` pool at weight 6; the shelter-pet pools are data (`data/simplycats/worldgen/template_pool/`). Injection logs at debug level, warns if a pool is missing.
- **Pet Carrier** — item state (`0` empty, `1` cat, `2` other pet, `3-6` adoption variants) is a `CarrierType` int inside the `DataComponents.CUSTOM_DATA` compound, sharing it with the captured pet's serialized NBT. It was the stack damage value on ≤1.20.1; 1.21 clamps damage to 0 on undamageable items, so never move it back.
- **Vanilla cat policy** `event/SCEvents.joinWorldEvent` — with `stop_vanilla_spawns=true` (default) untamed vanilla cat joins are cancelled server-side; `replace_tamed_vanilla` swaps tamed ones for mod cats. Note: the spawn-egg ambient meow plays *before* the join event cancels, so a cancelled spawn is audible — not a bug.
- **Config** `configuration/SCConfig` — `ModConfigSpec`, common file only.
- **Access transformer** `src/main/resources/META-INF/accesstransformer.cfg` — Mojang field names (not SRG), currently opens `StructureTemplatePool.templates/rawTemplates` and two `VillagerTrades` inner classes.

## 1.21-specific traps (all hit during the port)

- Datapack folders are singular (`structure/`, `advancement/`, `recipe/`…). Files in old plural folders are **silently ignored** — no log, content just doesn't exist.
- `neoforge.mods.toml` lives in `src/main/templates/META-INF/` — only that source set gets `${...}` placeholder expansion. A copy under `resources/` ships unexpanded.
- GUI screens: `Screen.render` (super) calls `renderBackground`, which applies the blur post-effect over everything already drawn. Never call `renderBackground` *and* `super.render` in the same frame — `CatBookScreen` renders its widgets directly instead.
- `textures/gui/icons.png` is gone; HUD icons are individual sprites (`hud/heart/full` etc.) drawn with `blitSprite`.
- `src/generated/resources` is a resource source dir; `.cache` is excluded in `build.gradle` — keep it that way or datagen cache ships in the jar.
- Region/entity `.mca` files are zlib-compressed per chunk — grepping saves for strings proves nothing.

## Known state (neoforge/1.21.1)

- Farmer's Respite compat (catnip tea) was **removed**, not ported — upstream has no 1.21.x build. `git log` on `faab169` shows exactly what to restore if it ships one.
- No mixins anywhere; the sponge plugin was dropped from the build.
- `updateJSONURL` points at `versions.json` on the `forge/1.16.5` branch; it has no 1.21.1 entry, so the version checker reports `Target: null` (harmless).
- Save-format deltas vs 1.20.1: `HomePos` is int-array now (old compound silently dropped); carriers filled on 1.20.1 read as empty.
