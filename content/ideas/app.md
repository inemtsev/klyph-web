---
title: "The App"
weight: 1
time: "3 min"
summary: "The App is your whole game machine. `runApp` builds it and hands it to every system as `app`."
image: "img/ideas/app.png"
imageAlt: "A pixel-art arcade machine"
---

## One machine, lots of tools

When you call `runApp`, klyph opens a window, sets up graphics and sound, and builds an **`App`**. Then it starts the {{< idea "loop" "frame loop" >}} and passes that `app` to every one of your {{< idea "systems" >}}.

```kotlin
fun main() {
    runApp(AppConfig(title = "My Game")) {
        // "this" is the App here, so you can call addSystem directly.
        addSystem(Phase.UPDATE) { app -> /* use the app's tools */ }
    }
}
```

## What's in the toolbox?

| Tool | What it does |
|---|---|
| `app.world` | All the {{< idea "entities" >}} and their components. |
| `app.commands` | A to-do list to spawn and destroy entities safely. See {{< idea "commands" >}}. |
| `app.input` | The keyboard, mouse and gamepads. See {{< term "input" >}}. |
| `app.time` | How long the last frame took, and how long the game has run. See {{< term "delta" "delta time" >}}. |
| `app.assets` | Loads pictures, sounds, music and fonts. See {{< idea "assets" >}}. |
| `app.audio` | Plays sounds and music. See {{< term "audio" "sound and music" >}}. |
| `app.camera` | Which part of the world shows in the window. See {{< term "camera" >}}. |
| `app.window` | The window itself: its size, fullscreen, the mouse cursor and screenshots. |
| `app.canvas` | Draws pictures, boxes and text directly, during `RENDER`. |
| `app.scenes` | The stack of screens. See {{< idea "scenes" >}}. |
| `app.resources` | Shared game data, like the score. See {{< idea "resources" >}}. |
| `app.events` | Messages, like "these two things touched". See {{< idea "events" >}}. |
| `app.collisions` | Asks "what's in this area?". See {{< term "collider" "colliders" >}}. |
| `app.quit()` | Ends the game after this frame. |

## AppConfig: the settings

`AppConfig` sets up the window and the engine before anything starts. Every setting has a default, so you only write the ones you want:

```kotlin
val config = AppConfig(
    title = "Gem Miner",       // the window title
    width = 800,                // window size, in points
    height = 600,
    clearColor = Color(0x2B1D14FFu), // the color behind everything
    fixedRate = 60,             // FIXED_UPDATE steps per second
)
```

Wrap it in `AppConfig.fromEnvironment(config)` and you can change settings from the Terminal without touching the code, like `-Pklyph.screenshot=120:shot.png` to save a picture of frame 120. Gem Miner does this in [the last Quest step]({{< relref "/quest/11-ship-it" >}}).

{{< nerd "What else can AppConfig do?" >}}
It can start in fullscreen (`windowMode = WindowMode.FULLSCREEN`), hide the mouse pointer (`cursor = CursorMode.HIDDEN`), quit after a number of frames (`frameLimit`), turn audio off (`audio = AudioConfig(output = AudioOutput.NONE)`), change the debug overlay key (`overlayKey`, <kbd>F3</kbd> by default), load assets from another folder (`assetRoot`), and change the collision grid size (`collisionCellSize`). You rarely need any of these. The [API reference](https://inemtsev.github.io/klyph/) lists every one.
{{< /nerd >}}
