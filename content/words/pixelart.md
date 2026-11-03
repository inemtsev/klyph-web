---
title: "Pixel art"
term: "pixel art"
summary: "Art drawn one chunky square pixel at a time, on purpose. Retro, readable and fun to make."
image: "img/words/pixelart.png"
---

## Picture it

Mosaic tiles. With big, visible squares and only a few colors, every pixel matters. Old consoles *had* to look like this. Today people choose it because it looks great and one person can draw a whole game.

## In klyph

When a picture is drawn bigger than it really is, the graphics card has to fill in the gaps. There are two ways:

- **`SamplerFilter.LINEAR`** (the default) blends neighbours. Smooth, but pixel art goes blurry.
- **`SamplerFilter.NEAREST`** copies the closest pixel. Blocky and crisp, perfect for pixel art.

```kotlin
val hero = app.assets.texture("hero.png", SamplerFilter.NEAREST).asRegion()
```

Scale pixel art by whole numbers (2×, 3×, 4×) so every pixel stays the same size. klyph's built-in font is pixel art too, and always crisp.

Free tools to try: [Piskel](https://www.piskelapp.com/) (in the browser), [LibreSprite](https://libresprite.github.io/).
