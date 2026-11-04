---
title: "Sparkles and sounds"
step: 8
weight: 8
time: "12 min"
summary: "Make catching a gem feel amazing: a burst of sparkles and a happy ding. A rock goes bonk."
learn:
  - "How to make **particles**"
  - "How to play **sounds**"
  - "Why game makers call this **juice**"
image: "img/words/particles.png"
imageAlt: "A burst of colorful sparkles"
---

Right now, catching a gem just makes it vanish. Boring! Game makers add **juice**: little effects that make actions feel good. Let's add two.

## 1. Load the sounds

The starter's `assets/` folder has sounds too. Add a `Sounds` class at the bottom of **`Art.kt`** (and import `klyph.audio.MusicTrack` and `klyph.audio.Sound`):

```kotlin {title="Art.kt"}
/** Every sound the game plays, loaded once at startup. */
class Sounds(
    val ding: Sound,
    val bonk: Sound,
    val start: Sound,
    val gameOver: Sound,
    val music: MusicTrack,
) {
    companion object {
        fun load(assets: Assets): Sounds = Sounds(
            ding = assets.sound("ding.wav"),
            bonk = assets.sound("bonk.wav"),
            start = assets.sound("start.wav"),
            gameOver = assets.sound("gameover.wav"),
            music = assets.musicTrack("music.ogg"),
        )
    }
}

val App.sounds: Sounds get() = resources.get()
```

A `Sound` is a short effect, loaded into memory. A `MusicTrack` is streamed bit by bit while it plays, because music is long. See {{< term "audio" "sound and music" >}}.

## 2. A sparkle burst

{{< term "particles" "Particles" >}} are lots of tiny pictures that fly out, fade and vanish. In klyph, one `ParticleEmitter` component holds a whole pool of them.

Add the burst to **`Spawns.kt`** (and import `klyph.particle.EmitterEnd`, `klyph.particle.ParticleEmitter` and `kotlin.time.Duration.Companion.milliseconds`):

```kotlin {title="Spawns.kt"}
/** A burst of sparkles of [color] at ([x], [y]). Its entity goes away once the last sparkle has faded. */
fun Commands.spawnSparkles(art: Art, x: Float, y: Float, color: Color) {
    spawn {
        val burst = ParticleEmitter(art.sparkle, capacity = 24).apply {
            speed = 80f..240f
            lifetime = 300.milliseconds..700.milliseconds
            gravityY = 400f
            // Each sparkle starts in the gem's color and fades to see-through on its own.
            startColor = color
            startScale = 1f
            endScale = 0.2f
            layer = Tuning.LAYER_SPARKLES
            whenEmpty = EmitterEnd.DESTROY
            emit(24)
        }
        add(Transform, Transform(x, y))
        add(ParticleEmitter, burst)
    }
}
```

Read it like a recipe: room for 24 sparkles, flying out at 80 to 240 units a second, each living 300 to 700 milliseconds, pulled down by gravity, shrinking as they fade. `emit(24)` fires them all at once.

The sparkles live on their **own** entity, not on the gem. The gem is destroyed right away, but the sparkles need a moment to fly. `whenEmpty = EmitterEnd.DESTROY` tells klyph to remove their entity once the last sparkle has faded, so nothing is left behind.

## 3. Ding and bonk

In **`Systems.kt`**, replace `catchOne` (and add `import klyph.render.Sprite`):

```kotlin {title="Systems.kt" hl_lines="6-8 11"}
private fun Round.catchOne(app: App, thing: Entity) {
    val world = app.world
    val gem = world.getOrNull(thing, Gem)
    if (gem != null) {
        gems += gem.points
        val where = world.get(thing, Transform)
        app.commands.spawnSparkles(app.art, where.x, where.y, world.get(thing, Sprite).color)
        app.audio.play(app.sounds.ding, pitch = 0.9f + Random.nextFloat() * 0.3f)
        app.commands.destroy(thing)
    } else if (world.has(thing, Rock)) {
        app.audio.play(app.sounds.bonk)
        app.commands.destroy(thing)
    }
}
```

The sparkles take the gem's own color from its `Sprite`, so a ruby bursts red and an emerald bursts green.

{{< kip tip "The pitch trick" >}}
`pitch = 0.9f + Random.nextFloat() * 0.3f` plays each ding a tiny bit higher or lower. Your ears get bored of the exact same sound 100 times. This keeps it fresh. Pros do it all the time!
{{< /kip >}}

## 4. Hook it up

In **`Main.kt`**, load the sounds at startup:

```kotlin {title="Main.kt" hl_lines="4"}
    runApp(config) {
        addSystem(Phase.STARTUP) { app ->
            app.resources.insert(Art.load(app.assets))
            app.resources.insert(Sounds.load(app.assets))
            app.resources.insert(Round())
            app.commands.spawnCart(app.art)
        }

        addSystem(Phase.UPDATE) { app -> moveCart(app) }
        addSystem(Phase.UPDATE) { app -> app.round.dropThings(app) }
        addSystem(Phase.UPDATE) { app -> fall(app) }
        addSystem(Phase.UPDATE) { app -> app.round.catchThings(app) }
        addSystem(Phase.LATE_UPDATE) { app -> fitCamera(app) }
    }
```

Run it, turn your sound on, and catch a gem. ✨ *Ding!* ✨ Much better.

{{< nerd "Why milliseconds, and not just numbers?" >}}
klyph measures every length of time as a Kotlin `Duration`, like `300.milliseconds` or `2.seconds`. A plain `0.3f` could mean seconds, milliseconds or frames; a `Duration` can't be misread. The one exception is `app.time.deltaSeconds`, a plain `Float` that's handy for "speed × time".
{{< /nerd >}}

{{< try >}}
Make sparkles go **up** instead of falling: set `gravityY = -300f`. Or make a rock throw grey dust: call `spawnSparkles` in the rock branch with `Color.GRAY` (and `import klyph.platform.Color`).
{{< /try >}}
