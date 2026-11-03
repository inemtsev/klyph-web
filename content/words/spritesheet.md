---
title: "Sprite sheet and animation"
linkTitle: "Sprite sheet"
term: "sprite sheet"
summary: "One picture with many frames in a grid. Show them one after another, quickly, and you get animation."
image: "img/words/spritesheet.png"
---

## Picture it

A flip book! Draw a slime slightly squashed on each page, flip fast, and it bounces. A **sprite sheet** is all the pages laid out on one picture, in a grid.

## In klyph

Cut the picture into a grid, then play the frames with a `SpriteAnimation` component:

```kotlin
val boom = app.assets.spriteSheet("explosion.png", columns = 6, rows = 1)

app.commands.spawn {
    val animation = SpriteAnimation(boom, 80.milliseconds, AnimationMode.ONCE)
    add(Transform, Transform(x, y))
    add(Sprite, Sprite(animation.currentFrame, 96f, 96f))
    add(SpriteAnimation, animation)
}
```

Every frame, klyph moves the animation on and swaps the sprite's picture for you. `AnimationMode.LOOP` repeats forever; `ONCE` stops on the last frame. (`80.milliseconds` needs `import kotlin.time.Duration.Companion.milliseconds`.)
