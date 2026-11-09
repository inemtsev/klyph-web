---
title: "Cheat Sheet"
summary: "Everything from the Quest on one page. Keep it open while you build."
image: "img/kip/point.png"
---

## Start a game

```kotlin
fun main() {
    runApp(AppConfig(title = "My Game", width = 800, height = 600)) {
        addSystem(Phase.STARTUP) { app -> /* load, spawn, pick a scene */ }
        addSystem(Phase.UPDATE) { app -> /* game rules */ }
        addSystem(Phase.RENDER) { app -> /* draw HUD */ }
    }
}
```

Phases in order: `STARTUP` · `INPUT` · `FIXED_UPDATE` · `UPDATE` · `LATE_UPDATE` · `RENDER` · `SHUTDOWN`. See {{< idea "loop" >}}.

## Entities and components

```kotlin
class Speed(var value: Float) { companion object : ComponentType<Speed>("Speed") }

val e = app.commands.spawn {
    add(Transform, Transform(x = 100f, y = 100f))
    add(Sprite, Sprite(region, 32f, 32f, layer = 1))
    add(Speed, Speed(200f))
}
app.commands.destroy(e)

app.world.forEach(Speed, Transform) { entity, speed, transform -> transform.x += speed.value * app.time.deltaSeconds }
app.world.getOrNull(e, Speed)   // Speed or null
app.world.has(e, Speed)         // true / false

add(Velocity, Velocity(dx = 0f, dy = -40f))   // klyph moves it every fixed step (1.1)
app.time.fixedStepCount                        // FIXED_UPDATE steps so far (1.1)
```

## Input

```kotlin
app.input.keyboard.isDown(Scancode.SPACE)
app.input.keyboard.isJustPressed(Scancode.SPACE)
app.input.keyboard.axis(Scancode.A, Scancode.D)   // -1, 0, 1
app.input.mouse.worldPosition
app.input.mouse.isJustPressed(MouseButton.LEFT)
app.input.gamepads.firstOrNull()?.leftStick
```

In `FIXED_UPDATE`, `isJustPressed` and `isJustReleased` mean "since the last step": each press reaches exactly one step (since 1.1). See {{< term "fixed" "fixed timestep" >}}.

## Assets and sound

```kotlin
val pic = app.assets.texture("hero.png").asRegion()
val ding = app.assets.sound("ding.wav")
val song = app.assets.musicTrack("music.ogg")

app.audio.play(ding, volume = 1f, pitch = 1f)
app.audio.music.play(song)
```

## Resources

```kotlin
app.resources.insert(Round())
val round = app.resources.get<Round>()
```

## Events and collisions

```kotlin
add(Collider, Collider(Shape.Circle(16f), layers = CollisionLayers.of(1), mask = CollisionLayers.of(2)))

app.events.forEach<Overlap> { overlap -> /* overlap.a, overlap.b, overlap.otherThan(mine) */ }
app.events.publish(MyEvent())
app.events.any<WindowFocusLost>()

// First solid a moving box would touch, or null. Works in FIXED_UPDATE. (1.1)
app.collisions.sweepBox(x = 100f, y = 300f, width = 24f, height = 40f, dx = 0f, dy = 200f)
```

## Particles

```kotlin
add(ParticleEmitter, ParticleEmitter(app.assets.disc(8), capacity = 50).apply {
    speed = 50f..150f
    lifetime = 500.milliseconds..1.seconds
    whenEmpty = EmitterEnd.DESTROY   // remove the entity when the burst is over
    emit(50)
})

// One burst of 24, then the entity is gone. (1.1)
app.commands.spawnBurst(x, y, ParticleEmitter.burst(spark, count = 24))
```

## Timers that clean up

```kotlin
add(Lifetime, Lifetime(2.seconds))                       // destroy after 2 seconds
add(Fade, Fade(500.milliseconds, end = FadeEnd.DESTROY)) // fade out, then destroy
```

## Animation

```kotlin
app.world.get(e, SpriteAnimation).speed = 2f   // twice as fast; 0 holds the frame (1.1)
```

## Platformers (1.1)

```kotlin
add(Collider, Collider(Shape.Box(width = 900f, height = 600f)))
add(Solid, Solid())                                      // a wall or floor
add(Solid, Solid(passable = Passable.FROM_BELOW))       // or: a one-way ledge

add(KinematicBody, KinematicBody(dx = 0f, dy = 0f))     // stops at solids
body.isOnFloor; body.isOnWall; body.isOnCeiling          // what the last move touched
body.dropThrough()                                       // fall through the ledge below
```

You add the gravity yourself, in `FIXED_UPDATE`. See {{< term "platformer" >}}.

## Drawing (RENDER only)

Screen space unless you pass `space = DrawSpace.WORLD`:

```kotlin
app.canvas.drawText(app.assets.defaultFont, "SCORE 10", x = 16f, y = 16f, scale = 3f, shadow = TextShadow())
app.canvas.drawRect(x = 0f, y = 0f, width = 100f, height = 10f, color = Color.RED)
app.canvas.drawSprite(region, x = 400f, y = 300f, layer = -10, space = DrawSpace.WORLD)
```

## Camera

```kotlin
app.camera.lookAt(player.x, player.y)
app.camera.zoom = 2f
app.camera.bounds = Rect(x = 0f, y = 0f, width = 4000f, height = 3000f)
app.camera.follow(player.x, player.y, smoothing = 5f, delta = app.time.delta)
```

## Angles and time

```kotlin
transform.rotation = 90f.degrees   // angles are radians; .degrees converts
Lifetime(1.5.seconds)              // times are Durations
app.time.deltaSeconds              // except this one: a Float, for speed × time
```

## Scenes

```kotlin
val play = buildScene("play") {
    onEnter { app -> /* fresh round */ }
    onExit { app -> }
    addSystem(Phase.UPDATE) { app -> }
}
app.scenes.replace(play)  // only this scene
app.scenes.push(pause)    // on top; play freezes
app.scenes.pop()          // back to play
```

## Gradle

| Command | Does |
|---|---|
| `./gradlew run` | Build and play (debug). |
| `./gradlew runRelease` | Build and play (fast release build). |
| `./gradlew distribute` | Zip the game for sharing, in `build/distributions/`. |
| `./gradlew run -Pklyph.screenshot=120:shot.png` | Save frame 120 as a picture (needs `AppConfig.fromEnvironment`). |

On Windows, use `.\gradlew` instead of `./gradlew`.

Every class and function, with examples, is in the official [API reference](https://inemtsev.github.io/klyph/).
