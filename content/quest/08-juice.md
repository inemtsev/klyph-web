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
            music = assets.music("music.ogg"),
        )
    }
}

val App.sounds: Sounds get() = resources.get()
```

A `Sound` is a short effect, loaded into memory. A `MusicTrack` is streamed bit by bit while it plays, because music is long. See {{< term "audio" >}}.

## 2. A sparkle burst

{{< term "particles" "Particles" >}} are lots of tiny pictures that fly out, fade and vanish. In klyph, one `ParticleEmitter` component holds a whole pool of them.

Add a `Lifetime` component to **`Components.kt`**:

```kotlin {title="Components.kt"}
/** Destroys its entity once [seconds] run out. Used for sparkle bursts. */
class Lifetime(var seconds: Float) {
    companion object : ComponentType<Lifetime>("Lifetime")
}
```

Then the burst itself, in **`Spawns.kt`** (and `import klyph.particle.ParticleEmitter`):

```kotlin {title="Spawns.kt"}
/** A burst of sparkles of [color] at ([x], [y]). The entity removes itself after a second. */
fun Commands.spawnSparkles(art: Art, x: Float, y: Float, color: Color) {
    spawn {
        val burst = ParticleEmitter(art.sparkle, maxParticles = 24).apply {
            speed = 80f..240f
            lifetime = 0.3f..0.7f
            gravityY = 400f
            startColor = color
            endColor = Color(color.red, color.green, color.blue, alpha = 0f)
            startScale = 1f
            endScale = 0.2f
            layer = Tuning.LAYER_SPARKLES
            emit(24)
        }
        add(Transform, Transform(x, y))
        add(ParticleEmitter, burst)
        add(Lifetime, Lifetime(1f))
    }
}
```

Read it like a recipe: 24 sparkles, flying out at 80 to 240 units a second, living 0.3 to 0.7 seconds, pulled down by gravity, fading from the gem's color to see-through and shrinking. `emit(24)` fires them all at once.

The sparkles live on their **own** entity, not on the gem. The gem is destroyed right away, but the sparkles need a second to fly. `Lifetime` cleans up their entity afterwards.

## 3. Ding, bonk, expire

In **`Systems.kt`**, replace `catchOne`:

```kotlin {title="Systems.kt" hl_lines="7-9 12"}
private fun Round.catchOne(app: App, cart: Entity, thing: Entity) {
    val world = app.world
    if (!world.has(cart, Cart)) return
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

(It uses `Sprite`, so add `import klyph.render.Sprite`.) And a system that counts down every `Lifetime`:

```kotlin {title="Systems.kt"}
/** Counts down every Lifetime and destroys its entity when it reaches zero. */
fun expire(app: App) {
    val dt = app.time.deltaSeconds
    app.world.forEach(Lifetime, Transform) { entity, life, _ ->
        life.seconds -= dt
        if (life.seconds <= 0f) app.commands.destroy(entity)
    }
}
```

{{< kip tip "The pitch trick" >}}
`pitch = 0.9f + Random.nextFloat() * 0.3f` plays each ding a tiny bit higher or lower. Your ears get bored of the exact same sound 100 times. This keeps it fresh. Pros do it all the time!
{{< /kip >}}

## 4. Hook it up

In **`Main.kt`**, load the sounds at startup and add `expire`:

```kotlin {title="Main.kt" hl_lines="4 13"}
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
        addSystem(Phase.UPDATE) { app -> expire(app) }
        addSystem(Phase.LATE_UPDATE) { app -> fitCamera(app) }
    }
```

Run it, turn your sound on, and catch a gem. ✨ *Ding!* ✨ Much better.

{{< try >}}
Make sparkles go **up** instead of falling: set `gravityY = -300f`. Or make a rock throw grey dust: call `spawnSparkles` in the rock branch with a grey color, like `Color(0x9A9A9AFFu)`.
{{< /try >}}
