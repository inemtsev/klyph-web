---
title: "Scenes"
weight: 6
time: "4 min"
summary: "A scene is one screen of your game, with its own systems. Scenes sit on a stack, and only the top one runs."
image: "img/ideas/scenes.png"
imageAlt: "Three floating islands stacked on top of each other"
---

## Screens on a stack

Most games have several screens: title, play, pause, game over. In klyph each is a **scene**. A scene is a bundle of {{< idea "systems" >}} plus two optional actions: `onEnter` and `onExit`.

```kotlin
val pause = buildScene("pause") {
    onEnter { app -> app.audio.music.volume = 0.3f }
    onExit { app -> app.audio.music.volume = 1f }
    addSystem(Phase.INPUT) { app ->
        if (app.input.keyboard.isJustPressed(Scancode.P)) app.scenes.pop()
    }
    addSystem(Phase.RENDER) { app -> drawCentered(app, "PAUSED", y = 240f, scale = 6f) }
}
```

Scenes sit on a **stack**. Only the **top** scene's systems run:

{{< scenestack "gameOver" "play" >}}

## Three moves

| Move | What happens | Example |
|---|---|---|
| `app.scenes.replace(s)` | Every scene leaves, then `s` is the only one. | Title → Play |
| `app.scenes.push(s)` | `s` goes on top. The one below **freezes**. | Play → Pause |
| `app.scenes.pop()` | The top scene leaves. The one below carries on. | Pause → Play |

The changes happen at the **end of the frame**, so the rest of the frame finishes normally.

## Frozen, not gone

When you push `pause` on top of `play`, the play scene **freezes**. Its systems stop, and its {{< term "particles" >}}, {{< term "spritesheet" "animations" >}} and {{< term "collider" "colliders" >}} stand still. But its entities are **still drawn**, so you see the game behind the pause text. Pop the pause, and it carries on exactly where it was.

## Scenes clean up after themselves

Every entity spawned while a scene's `onEnter` or systems run **belongs to that scene**. When the scene leaves the stack, its entities are destroyed. That's why a new round of Gem Miner never has old gems lying around.

Entities spawned in `runApp`'s own systems belong to no scene and live forever.

{{< kip oops "Scenes have no STARTUP" >}}
A scene's systems can use any phase **except** `STARTUP` and `SHUTDOWN`. Use `onEnter` and `onExit` instead.
{{< /kip >}}

{{< nerd "Can a scene be on the stack twice?" >}}
No. `push` throws an error if that scene is already on the stack. `replace` is fine, though: replacing `play` with `play` makes it leave and enter again, which is a handy restart.
{{< /nerd >}}
