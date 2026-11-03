---
title: "HUD"
term: "HUD"
summary: "The Heads-Up Display: score, lives, timer and other info drawn on top of the game, stuck to the screen."
image: "img/words/hud.png"
---

## Picture it

Fighter-jet pilots see speed and height projected on the glass in front of them, so they never look down. Games borrowed the name: the **HUD** is the hearts, coins and score floating over the game.

## In klyph

Draw it in the `RENDER` phase with `app.canvas`, in **screen space**, so it stays put when the {{< term "camera" >}} moves:

```kotlin
fun drawHud(app: App) {
    val font = app.assets.defaultFont
    app.canvas.drawText(font, "GEMS ${app.round.gems}", x = 16f, y = 16f, scale = 3f)
    app.canvas.drawRect(x = 16f, y = 56f, width = 200f, height = 12f, color = Color(0xFF4D6DFFu), space = DrawSpace.SCREEN)
}
```

`drawText` uses screen space by default; `drawSprite` and `drawRect` default to world space, so pass `space = DrawSpace.SCREEN` for those.

{{< kip tip "Less is more" >}}
Show only what the player needs **right now**. A big, clear score beats ten tiny numbers.
{{< /kip >}}
