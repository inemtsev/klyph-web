---
title: "Title, pause and game over"
linkTitle: "Scenes"
step: 10
weight: 10
time: "15 min"
summary: "Split the game into scenes (title, play, pause and game over) and switch between them."
learn:
  - "What a **scene** is, and what the **scene stack** does"
  - "The difference between `replace`, `push` and `pop`"
  - "Why scenes clean up their own entities"
image: "img/ideas/scenes.png"
imageAlt: "Three floating islands stacked on top of each other"
---

## What's a scene?

Your game has different **screens**: a title screen, the game itself, a pause screen, a game over screen. In klyph, each one is a {{< idea "scenes" "scene" >}}: a bundle of systems that only run while that scene is on top.

Scenes sit on a **stack**, like plates:

{{< scenestack "pause" "play" >}}

- **`replace(scene)`** clears the whole stack and puts `scene` on it. *Title → Play.*
- **`push(scene)`** puts `scene` on top. The one below **freezes**: its systems stop, but you can still see it. *Play → Pause.*
- **`pop()`** takes the top scene off, and the one below carries on. *Pause → back to Play.*

## 1. One small helper

Add this to **`Systems.kt`**, for big centered words:

```kotlin {title="Systems.kt"}
/** Draws [text] across the middle of the window, [y] points from the top. */
fun drawCentered(app: App, text: String, y: Float, scale: Float) {
    app.canvas.drawText(
        app.assets.defaultFont,
        text,
        x = app.window.width / 2f,
        y = y,
        scale = scale,
        align = TextAlign.CENTER,
        shadow = TextShadow(),
    )
}
```

## 2. The four scenes

Make **`Scenes.kt`**:

```kotlin {title="Scenes.kt"}
package gemminer

import klyph.app.Phase
import klyph.platform.Scancode
import klyph.scene.Scene
import klyph.scene.buildScene

object GameScenes {
    val title: Scene = buildScene("title") {
        addSystem(Phase.INPUT) { app ->
            val keys = app.input.keyboard
            if (keys.isJustPressed(Scancode.SPACE)) app.scenes.replace(play)
            if (keys.isJustPressed(Scancode.ESCAPE)) app.quit()
        }
        addSystem(Phase.RENDER) { app ->
            drawCentered(app, "GEM MINER", y = 180f, scale = 8f)
            drawCentered(app, "A/D or arrows to move", y = 320f, scale = 3f)
            drawCentered(app, "Press SPACE to start", y = 380f, scale = 3f)
        }
    }

    val play: Scene = buildScene("play") {
        onEnter { app ->
            app.resources.insert(Round())
            app.commands.spawnCart(app.art)
            app.audio.play(app.sounds.start)
        }

        addSystem(Phase.INPUT) { app ->
            val keys = app.input.keyboard
            if (keys.isJustPressed(Scancode.P) || keys.isJustPressed(Scancode.ESCAPE)) app.scenes.push(pause)
        }

        addSystem(Phase.UPDATE) { app -> moveCart(app) }
        addSystem(Phase.UPDATE) { app -> app.round.dropThings(app) }
        addSystem(Phase.UPDATE) { app -> fall(app) }
        addSystem(Phase.UPDATE) { app -> app.round.catchThings(app) }
        addSystem(Phase.UPDATE) { app ->
            if (app.round.lives <= 0) app.scenes.push(gameOver)
        }

        addSystem(Phase.RENDER) { app -> app.round.drawHud(app) }
    }

    val pause: Scene = buildScene("pause") {
        onEnter { app -> app.audio.music.volume = 0.3f }
        onExit { app -> app.audio.music.volume = 1f }
        addSystem(Phase.INPUT) { app ->
            val keys = app.input.keyboard
            if (keys.isJustPressed(Scancode.P) || keys.isJustPressed(Scancode.ESCAPE)) app.scenes.pop()
        }
        addSystem(Phase.RENDER) { app ->
            drawCentered(app, "PAUSED", y = 240f, scale = 6f)
            drawCentered(app, "P to keep mining", y = 330f, scale = 3f)
        }
    }

    val gameOver: Scene = buildScene("gameOver") {
        onEnter { app -> app.audio.play(app.sounds.gameOver) }
        addSystem(Phase.INPUT) { app ->
            if (app.input.keyboard.isJustPressed(Scancode.SPACE)) app.scenes.replace(play)
        }
        addSystem(Phase.RENDER) { app ->
            drawCentered(app, "GAME OVER", y = 200f, scale = 7f)
            drawCentered(app, "You mined ${app.round.gems} gems!", y = 300f, scale = 3f)
            drawCentered(app, "Press SPACE to mine again", y = 360f, scale = 3f)
        }
    }
}
```

Look at `play`: it's **all the game systems from `Main.kt`**, moved into one scene. Plus two new things:

- **`onEnter`** runs once, each time the scene starts. A fresh `Round` and a fresh cart, so every game starts from zero.
- Instead of `app.quit()`, losing your last life now **pushes** `gameOver` on top.

`isJustPressed` is `true` only in the frame the key went down, so one press is one action. (`isDown` is `true` the whole time you hold it.)

## 3. Slim down `Main.kt`

Now `Main.kt` only keeps what runs on **every** screen. Replace it:

```kotlin {title="Main.kt"}
package gemminer

import klyph.app.AppConfig
import klyph.app.Phase
import klyph.app.runApp

fun main() {
    val config = AppConfig(
        title = "Gem Miner",
        width = 800,
        height = 600,
        clearColor = Tuning.CAVE_COLOR,
    )

    runApp(config) {
        addSystem(Phase.STARTUP) { app ->
            app.resources.insert(Art.load(app.assets))
            app.resources.insert(Sounds.load(app.assets))
            app.audio.music.play(app.sounds.music)
            app.scenes.replace(GameScenes.title)
        }

        addSystem(Phase.LATE_UPDATE) { app -> fitCamera(app) }

        addSystem(Phase.RENDER) { app -> drawCaveWall(app) }
    }
}
```

We sneaked in **music**, too: `app.audio.music.play(...)` loops it forever, across every scene. The pause scene turns it down in `onEnter` and back up in `onExit`.

Run it! Title → <kbd>Space</kbd> → play → <kbd>P</kbd> to pause → <kbd>P</kbd> again → lose three lives → game over → <kbd>Space</kbd> to play again.

{{< kip think "Where did the old gems go?" >}}
When you press <kbd>Space</kbd> on the game over screen, `replace(play)` takes `gameOver` **and** the old `play` off the stack. Every entity a scene spawned **belongs** to it, so the old cart, gems and sparkles vanish with it. The new `play` starts clean. No cleanup code needed!
{{< /kip >}}

{{< kip yay "That's the whole game!" >}}
Your game now plays exactly like the finished one from step 1. Compare your code with `peek/gemminer` if you like: the last step adds one line to `Main.kt`, and then you share it with the world.
{{< /kip >}}

{{< try >}}
Make the game over screen show **"NEW RECORD!"** when you beat your best score. You'll need a place to keep the best score that survives between rounds. A new resource, inserted at `STARTUP`, perhaps?
{{< /try >}}
