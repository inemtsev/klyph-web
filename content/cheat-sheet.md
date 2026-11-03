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
```

## Input

```kotlin
app.input.keyboard.isDown(Scancode.SPACE)
app.input.keyboard.isJustPressed(Scancode.SPACE)
app.input.axis(Scancode.A, Scancode.D)        // -1, 0, 1
app.input.mouse.worldPosition
app.input.mouse.isJustPressed(MouseButton.LEFT)
app.input.gamepads.firstOrNull()?.leftStick
```

## Assets and sound

```kotlin
val pic = app.assets.texture("hero.png").asRegion()
val ding = app.assets.sound("ding.wav")
val song = app.assets.music("music.ogg")

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

app.events.forEach<Overlap> { overlap -> /* overlap.a, overlap.b */ }
app.events.publish(MyEvent())
app.events.any<WindowFocusLost>()
```

## Particles

```kotlin
add(ParticleEmitter, ParticleEmitter(spark, maxParticles = 50).apply {
    speed = 50f..150f; lifetime = 0.5f..1f; emit(50)
})
```

## Drawing (RENDER only)

```kotlin
app.canvas.drawText(app.assets.defaultFont, "SCORE 10", x = 16f, y = 16f, scale = 3f)
app.canvas.drawSprite(region, x = 400f, y = 300f, layer = -10)
app.canvas.drawRect(x = 0f, y = 0f, width = 100f, height = 10f, color = Color(0xFF0000FFu))
```

## Camera

```kotlin
app.camera.lookAt(player.x, player.y)
app.camera.zoom = 2f
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

On Windows, use `.\gradlew` instead of `./gradlew`.
