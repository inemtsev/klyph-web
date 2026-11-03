---
title: "Texture"
summary: "A picture loaded into the graphics card, ready to draw. Sprites show part or all of a texture."
image: "img/words/texture.png"
---

## Picture it

A `.png` file sits on your disk. To draw it fast, the computer copies it into the graphics card's own memory. That copy is a **texture**. A {{< term "sprite" >}} then says "draw this texture here, this big".

## In klyph

```kotlin
val texture = app.assets.texture("cart.png")
val region = texture.asRegion() // the whole picture
```

A `TextureRegion` is a rectangle inside a texture: the whole thing, or one frame of a {{< term "spritesheet" "sprite sheet" >}}. Sprites and particles always draw a region.

Load textures **once**, at startup. Loading reads the disk and talks to the graphics card, which is too slow to do every frame.
