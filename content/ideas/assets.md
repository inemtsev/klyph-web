---
title: "Assets"
weight: 9
time: "2 min"
summary: "Assets are your game's files: pictures, sounds, music and fonts. Put them in `assets/` and load them with `app.assets`."
image: "img/ideas/assets.png"
imageAlt: "A crate with a paint palette, brush, speaker and music note"
---

## The assets folder

Put your files in the `assets/` folder of your project. When you build, klyph copies that folder **next to your game program**, and `app.assets` loads from there.

```kotlin
val cart = app.assets.texture("cart.png").asRegion()   // a picture
val ding = app.assets.sound("ding.wav")                 // a short sound
val song = app.assets.music("music.ogg")                // a long, streamed track
val font = app.assets.font("big.fnt")                   // a bitmap font
val boom = app.assets.spriteSheet("boom.png", columns = 6, rows = 1) // animation frames
```

| Kind | Files | Loaded with |
|---|---|---|
| Picture ({{< term "texture" >}}) | `.png`, `.jpg` | `texture(path)` |
| {{< term "spritesheet" "Sprite sheet" >}} | `.png` cut into a grid | `spriteSheet(path, columns, rows)` |
| Sound effect | `.wav`, `.ogg` | `sound(path)` |
| Music | `.ogg` | `music(path)` |
| Font | BMFont `.fnt` | `font(path)` |

And one you get for free: **`app.assets.defaultFont`**, an 8 × 8 pixel font built into klyph.

## Load once, at the start

Loading reads a file from the disk, which is slow. Load everything in `STARTUP` (or a scene's `onEnter`), keep it in a {{< idea "resources" "resource" >}}, and use it every frame.

{{< kip oops "Missing file?" >}}
`texture("cart.png")` crashes with an error if the file isn't there. That's on purpose: you find typos straight away. If a file is optional, use `textureOrNull("cart.png")`, which gives `null` instead. Most loaders have an `OrNull` twin.
{{< /kip >}}

{{< nerd "Crisp pixel art" >}}
By default, pictures are smoothed when they're scaled. For true {{< term "pixelart" "pixel art" >}} that should stay blocky, load with nearest sampling: `texture("hero.png", SamplerFilter.NEAREST)`.
{{< /nerd >}}
