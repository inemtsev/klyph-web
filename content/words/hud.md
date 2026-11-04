---
title: "HUD"
term: "HUD"
summary: "The Heads-Up Display: score, lives, timer and other info drawn on top of the game, stuck to the screen."
image: "img/words/hud.png"
---

## Picture it

Fighter-jet pilots see speed and height projected on the glass in front of them, so they never look down. Games borrowed the name: the **HUD** is the hearts, coins and score floating over the game.

## In klyph

Draw it in the `RENDER` phase with `app.canvas`. Everything on the canvas is in **screen space** unless you say otherwise, so the HUD stays put when the {{< term "camera" >}} moves:

```kotlin
fun drawHud(app: App) {
    val font = app.assets.defaultFont
    app.canvas.drawText(font, "GEMS ${app.round.gems}", x = 16f, y = 16f, scale = 3f, shadow = TextShadow())
    app.canvas.drawRect(x = 16f, y = 56f, width = 200f, height = 12f, color = Color.RED)
}
```

`shadow = TextShadow()` draws a dark copy just under the words, so they're easy to read over a busy background. To draw something that moves with the world instead, such as a health bar over an enemy, pass `space = DrawSpace.WORLD`.

{{< kip tip "Less is more" >}}
Show only what the player needs **right now**. A big, clear score beats ten tiny numbers.
{{< /kip >}}
