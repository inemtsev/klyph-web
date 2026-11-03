---
title: "Sprite"
summary: "A 2D picture placed in the game world, like a sticker: the cart, a gem, a rock."
image: "img/words/sprite.png"
---

## Picture it

Cut a character out of paper and slide it around on a table. That paper cut-out is a **sprite**. Almost everything you see in a 2D game is one: players, enemies, coins, bullets.

## In klyph

A `Sprite` is a component. Stick it on an entity together with a {{< term "transform" "`Transform`" >}} (where to draw it):

```kotlin
app.commands.spawn {
    add(Transform, Transform(x = 400f, y = 300f))
    add(Sprite, Sprite(art.gem, width = 44f, height = 44f, color = Color(0xFF4D6DFFu), layer = 1))
}
```

- `art.gem` is the picture: a region of a {{< term "texture" >}}.
- `width` and `height` are its size in the world.
- `color` {{< term "tint" "tints" >}} it. `layer` picks what's drawn on top. See {{< term "layers" >}}.
- `isFlippedX = true` mirrors it, handy for a character turning around.
- `isVisible = false` hides it without deleting it.

klyph draws thousands of sprites in one go, so don't be shy.
