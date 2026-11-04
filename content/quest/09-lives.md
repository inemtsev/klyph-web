---
title: "Score, lives and the cave wall"
linkTitle: "Score and lives"
step: 9
weight: 9
time: "10 min"
summary: "Draw the score and lives on the screen, paint the cave wall behind everything, and let rocks cost a life."
learn:
  - "What the `RENDER` phase is for"
  - "How to draw text and pictures with `app.canvas`"
  - "The difference between **world** and **screen** space"
image: "img/words/hud.png"
imageAlt: "Three red hearts and a gold coin"
---

## 1. Rocks hurt

In `catchOne` in **`Systems.kt`**, add one line to the rock branch:

```kotlin {title="Systems.kt" hl_lines="2"}
    } else if (world.has(thing, Rock)) {
        lives -= 1
        app.audio.play(app.sounds.bonk)
        app.commands.destroy(thing)
    }
```

## 2. Draw with the canvas

So far, every picture came from a `Sprite` on an entity. For things that aren't entities, like the score, you can draw directly on **`app.canvas`**. It only works in the `RENDER` phase:

{{< phases "RENDER" >}}

Add these to **`Systems.kt`** (and import `klyph.render.DrawSpace`, `klyph.text.TextAlign` and `klyph.text.TextShadow`):

```kotlin {title="Systems.kt"}
/** Draws the cave wall picture behind everything, in the world, so it zooms with the camera. */
fun drawCaveWall(app: App) {
    app.canvas.drawSprite(
        app.art.wall,
        x = Tuning.WORLD_WIDTH / 2,
        y = Tuning.WORLD_HEIGHT / 2,
        width = Tuning.WORLD_WIDTH,
        height = Tuning.WORLD_HEIGHT,
        layer = Tuning.LAYER_WALL,
        space = DrawSpace.WORLD,
    )
}

/** Draws the score and the lives in the top corners of the window, with a shadow so they stand out. */
fun Round.drawHud(app: App) {
    val font = app.assets.defaultFont
    app.canvas.drawText(font, "GEMS $gems", x = 16f, y = 16f, scale = 3f, shadow = TextShadow())
    app.canvas.drawText(
        font,
        "LIVES $lives",
        x = app.window.width - 16f,
        y = 16f,
        scale = 3f,
        align = TextAlign.RIGHT,
        shadow = TextShadow(),
    )
}
```

The canvas has two ways of measuring where things go:

- **Screen space**, the default: measured from the window's top-left corner, so it stays put whatever the camera does. Perfect for the score and lives, the {{< term "hud" "HUD" >}}.
- **World space**, when you pass `space = DrawSpace.WORLD`: the same units as the cart and the gems, so it moves and zooms with the camera. That's what the wall needs, so it always lines up with the cave. Its {{< term "layers" "layer" >}} is `-10`, behind everything.

`app.assets.defaultFont` is a tiny 8 × 8 {{< term "pixelart" "pixel" >}} font built into klyph, so you don't need a font file. `scale = 3f` makes it 24 tall, and `shadow = TextShadow()` draws a dark copy just under it, so the words are easy to read on the busy wall.

## 3. Hook it up

In **`Main.kt`**, add the two drawing systems, plus a very simple game over:

```kotlin {title="Main.kt" hl_lines="5-8 11 12"}
        addSystem(Phase.UPDATE) { app -> moveCart(app) }
        addSystem(Phase.UPDATE) { app -> app.round.dropThings(app) }
        addSystem(Phase.UPDATE) { app -> fall(app) }
        addSystem(Phase.UPDATE) { app -> app.round.catchThings(app) }
        addSystem(Phase.UPDATE) { app ->
            // For now, game over just closes the window. A real screen comes next!
            if (app.round.lives <= 0) app.quit()
        }
        addSystem(Phase.LATE_UPDATE) { app -> fitCamera(app) }

        addSystem(Phase.RENDER) { app -> drawCaveWall(app) }
        addSystem(Phase.RENDER) { app -> app.round.drawHud(app) }
```

Run it. **It's a real game now!** A cave, a score, lives, and you can lose. Three rocks and the window closes.

{{< checkpoint "09-lives" >}}

{{< kip think "Systems run in the order you add them" >}}
Within one phase, klyph runs systems in the order you added them. That's why `catchThings` comes after `fall`, and the lives check comes after `catchThings`. Order matters, so keep it tidy!
{{< /kip >}}

{{< try >}}
Show how many **seconds** you've survived, in the middle at the top. Hint: `elapsed.toInt()` turns `12.7f` into `12`, and `TextAlign.CENTER` with `x = app.window.width / 2f` centers it.
{{< /try >}}
