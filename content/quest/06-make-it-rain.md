---
title: "Make it rain gems"
step: 6
weight: 6
time: "12 min"
summary: "Drop gems and rocks from the top every 0.7 seconds, and clean them up when they fall off the bottom."
learn:
  - "How to spawn entities while the game runs"
  - "How to **destroy** entities with **commands**"
  - "How one white picture becomes five colored gems (**tint**)"
image: "img/words/tint.png"
imageAlt: "A paint bucket splashing red, green and blue"
---

## 1. More components

Add these to **`Components.kt`**, under `Cart` (plus the `App` import at the top):

```kotlin {title="Components.kt"}
import klyph.app.App

/** Anything falling down the mine shaft, at [speed] units per second. */
class Falling(var speed: Float) {
    companion object : ComponentType<Falling>("Falling")
}

/** A gem. Catch it for [points]. */
class Gem(val points: Int) {
    companion object : ComponentType<Gem>("Gem")
}

/** A rock. Catch it and you lose a life. */
class Rock {
    companion object : ComponentType<Rock>("Rock")
}

/**
 * The state of one round: score, lives and timers. It is a resource, shared by every system.
 */
class Round {
    var gems = 0
    var lives = Tuning.STARTING_LIVES
    var elapsed = 0f
    var dropTimer = Tuning.FIRST_DROP_DELAY
}

val App.round: Round get() = resources.get()
```

`Rock` has no data at all! It's a **tag**: a sticker that just says "I'm a rock". That's a common trick.

`Round` isn't a component. It's another {{< idea "resources" "resource" >}}, like `Art`: one score, one timer, for the whole game.

## 2. Spawn gems and rocks

Add two functions to **`Spawns.kt`** (and `import klyph.platform.Color`):

```kotlin {title="Spawns.kt"}
/** A gem of [color] just above the top of the screen at [x], falling at [speed]. */
fun Commands.spawnGem(art: Art, x: Float, speed: Float, color: Color) {
    spawn {
        add(Transform, Transform(x, -Tuning.GEM_SIZE))
        add(Sprite, Sprite(art.gem, Tuning.GEM_SIZE, Tuning.GEM_SIZE, color = color, layer = Tuning.LAYER_THINGS))
        add(Falling, Falling(speed))
        add(Gem, Gem(points = 1))
    }
}

/** A rock just above the top of the screen at [x], falling at [speed]. */
fun Commands.spawnRock(art: Art, x: Float, speed: Float) {
    spawn {
        add(Transform, Transform(x, -Tuning.ROCK_SIZE))
        add(Sprite, Sprite(art.rock, Tuning.ROCK_SIZE, Tuning.ROCK_SIZE, layer = Tuning.LAYER_THINGS))
        add(Falling, Falling(speed))
        add(Rock, Rock())
    }
}
```

See `color = color` on the gem's sprite? The gem picture is **white**. A {{< term "tint" >}} multiplies every pixel by a color, so white × red = red. One picture, five gems!

## 3. Drop, fall, clean up

Add two systems to **`Systems.kt`** (and `import kotlin.random.Random`):

```kotlin {title="Systems.kt"}
/** Counts the round's time and, every so often, drops a gem or a rock from the top. */
fun Round.dropThings(app: App) {
    val dt = app.time.deltaSeconds
    elapsed += dt
    dropTimer -= dt
    if (dropTimer > 0f) return
    dropTimer += Tuning.DROP_EVERY

    val x = Random.nextFloat() * (Tuning.WORLD_WIDTH - 80f) + 40f
    val speed = Tuning.FALL_SPEED + elapsed * Tuning.FALL_SPEEDUP
    if (Random.nextFloat() < Tuning.ROCK_CHANCE) {
        app.commands.spawnRock(app.art, x, speed)
    } else {
        app.commands.spawnGem(app.art, x, speed, Tuning.GEM_COLORS.random())
    }
}

/** Moves everything that falls, and removes what drops off the bottom of the screen. */
fun fall(app: App) {
    val dt = app.time.deltaSeconds
    app.world.forEach(Falling, Transform) { entity, falling, transform ->
        transform.y += falling.speed * dt
        if (transform.y > Tuning.WORLD_HEIGHT + 64f) app.commands.destroy(entity)
    }
}
```

`dropThings` is a **timer**: count down, and when it hits zero, drop something and start again. Things also fall a little faster the longer you play.

{{< kip think "Why `app.commands.destroy`, not `app.world.destroy`?" >}}
`fall` is in the middle of looping over the world. Removing things from a list *while* you walk through it is like pulling a block out of a tower you're climbing! {{< idea "commands" "Commands" >}} write it on a to-do list instead, and klyph does it right after the system finishes. Spawning works the same way.
{{< /kip >}}

## 4. Hook them up

In **`Main.kt`**, insert the `Round` at startup and add the two systems:

```kotlin {title="Main.kt" hl_lines="4 9 10"}
    runApp(config) {
        addSystem(Phase.STARTUP) { app ->
            app.resources.insert(Art.load(app.assets))
            app.resources.insert(Round())
            app.commands.spawnCart(app.art)
        }

        addSystem(Phase.UPDATE) { app -> moveCart(app) }
        addSystem(Phase.UPDATE) { app -> app.round.dropThings(app) }
        addSystem(Phase.UPDATE) { app -> fall(app) }
        addSystem(Phase.LATE_UPDATE) { app -> fitCamera(app) }
    }
```

Run it. **It's raining gems!** They fall straight through the cart, though. We'll fix that next.

{{< try >}}
Set `ROCK_CHANCE` to `0f` for a gems-only paradise, or `0.9f` for a rockslide. Add a sixth color to `GEM_COLORS`!
{{< /try >}}
