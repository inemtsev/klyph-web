# Gem Miner

The game you build in the [klyph Quest](https://klyph.eventslooped.com/quest/). Slide the
minecart, catch the falling gems, dodge the rocks.

| Key | Does |
|---|---|
| <kbd>A</kbd> <kbd>D</kbd> or <kbd>←</kbd> <kbd>→</kbd> | Move the cart |
| <kbd>Space</kbd> | Start, or play again |
| <kbd>P</kbd> or <kbd>Esc</kbd> | Pause |
| <kbd>F3</kbd> | klyph's debug overlay |

## Run it

You need a JDK 17 or newer. On Linux you also need SDL3 3.4 or newer (`libsdl3-dev`); on macOS
and Windows the klyph Gradle plugin downloads it.

```bash
./gradlew run
```

On Windows: `.\gradlew run`. The first build downloads the Kotlin/Native toolchain and takes a
few minutes.

`./gradlew distribute` makes a zip in `build/distributions/` that runs on a computer without Java
or SDL3 installed. `./gradlew run -Pklyph.screenshot=120:shot.png` saves frame 120 as `shot.png`.

## What's here

| Path | What it is |
|---|---|
| `build.gradle.kts` | The whole build: the klyph plugin, the entry point, the engine. |
| `src/nativeMain/kotlin/gemminer/` | The finished game. |
| `checkpoints/05-move-it/` | The code after Quest step 5: a cart that moves. |
| `checkpoints/07-catch/` | After step 7: falling gems you can catch. |
| `checkpoints/09-lives/` | After step 9: score, lives, sounds and sparkles, before scenes. |
| `assets/` | Pictures and sounds. See [`assets/README.md`](assets/README.md). |
| `tools/synth.py` | Makes the sounds and the music. |

To catch up to a checkpoint, copy its files over the ones in `src/nativeMain/kotlin/gemminer/`.

Built for klyph 1.2.0. Code written for 1.0 keeps working in every 1.x release.
