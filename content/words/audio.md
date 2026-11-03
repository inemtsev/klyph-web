---
title: "Sound and music"
term: "sound"
summary: "Sound effects are short and play on events (a ding, a bonk). Music is long, loops, and plays in the background."
image: "img/words/audio.png"
---

## Picture it

Turn the sound off in a game you love. It feels flat, right? **Sound effects** tell the player "that worked!" or "ouch!". **Music** sets the mood. Both matter a lot.

## In klyph

```kotlin
// Load once, at startup:
val ding = app.assets.sound("ding.wav")    // short effect: WAV or OGG, kept in memory
val song = app.assets.music("music.ogg")   // long track: OGG, streamed bit by bit

// Effects:
app.audio.play(ding)                                // just play it
app.audio.play(ding, volume = 0.5f, pitch = 1.2f)   // quieter, a bit higher
app.audio.play(ding, pan = -1f)                     // from the left speaker

// Music (one track at a time):
app.audio.music.play(song, fadeIn = 2.seconds)      // loops by default
app.audio.music.volume = 0.3f                       // duck it, e.g. when paused
app.audio.music.stop(fadeOut = 1.seconds)
```

(`2.seconds` needs `import kotlin.time.Duration.Companion.seconds`.)

If a computer has no speakers, klyph simply stays quiet. Your game doesn't crash.

{{< kip tip "Make your own sounds" >}}
[jsfxr](https://sfxr.me/) makes retro sound effects in your browser with one click. Gem Miner's sounds were made with a tiny Python script; it's in `starter/tools/synth.py`.
{{< /kip >}}
