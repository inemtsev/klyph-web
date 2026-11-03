---
title: "Your first system"
step: 3
weight: 3
time: "8 min"
summary: "Add two systems: one says hello at the start, one makes the cave glow like torchlight."
learn:
  - "What a **system** is"
  - "What **phases** are, and why `STARTUP` and `UPDATE` are different"
image: "img/ideas/systems.png"
imageAlt: "A little robot worker with a wrench"
---

## Systems are little machines

A {{< idea "systems" "system" >}} is a small piece of code that klyph runs for you, again and again. You tell klyph **when** it should run by picking a {{< idea "loop" "phase" >}}.

{{< phases "STARTUP,UPDATE" >}}

klyph runs `STARTUP` once. Then, many times every second (usually 60), it runs the whole row in the box: `INPUT`, `FIXED_UPDATE`, `UPDATE`, `LATE_UPDATE`, `RENDER`. Each trip round the row is one {{< term "frame" >}}.

## Add two systems

Replace your `runApp` block. The new lines are highlighted:

```kotlin {title="Main.kt" hl_lines="4 7 18-28"}
package gemminer

import klyph.app.AppConfig
import klyph.app.Phase
import klyph.app.runApp
import klyph.platform.Color
import kotlin.math.sin

fun main() {
    val config = AppConfig(
        title = "Gem Miner",
        width = 800,
        height = 600,
        clearColor = Color(0x2B1D14FFu),
    )

    runApp(config) {
        addSystem(Phase.STARTUP) { app ->
            println("Hello from klyph! The mine is open.")
        }

        addSystem(Phase.UPDATE) { app ->
            // Seconds since the game started.
            val seconds = app.time.elapsed.inWholeMilliseconds / 1000f
            // sin() swings smoothly from -1 to 1 and back, so the glow goes up and down.
            val glow = 0.12f + 0.04f * sin(seconds * 4f)
            app.clearColor = Color(red = glow + 0.05f, green = glow, blue = glow * 0.6f)
        }
    }
}
```

Run it. Look at the Terminal: there's your hello! And the window gently glows, like a torch flickering in a cave.

## What's `app`?

Every system gets **`app`**, klyph's big toolbox. Through it you reach everything: `app.time`, `app.input`, `app.world`, `app.audio`, and more. You'll use a new tool from it in almost every step.

{{< kip think "Why not just a `while` loop?" >}}
You *could* write your own loop, but then you'd have to handle the window, the keyboard, drawing, timing… klyph does all that and calls your systems at exactly the right moment. You just write the interesting part.
{{< /kip >}}

{{< try >}}
Change `seconds * 4f` to `seconds * 12f`. Now it's a scary flicker! What happens with `seconds * 0.5f`?
{{< /try >}}
