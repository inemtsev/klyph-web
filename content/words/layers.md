---
title: "Layers (draw order)"
linkTitle: "Layers"
term: "layers"
summary: "A number that decides what's drawn on top: higher layers cover lower ones, like stacked sheets of glass."
image: "img/words/layers.png"
---

## Picture it

Painted sheets of glass, stacked. The cave wall on the bottom sheet, the cart and gems on the next, sparkles on top. You see through to the lower sheets wherever the upper ones are empty.

## In klyph

`Sprite`, `Text`, `ParticleEmitter` and every `app.canvas` drawing take a `layer`:

```kotlin
const val LAYER_WALL = -10    // at the back
const val LAYER_THINGS = 1    // cart, gems, rocks
const val LAYER_SPARKLES = 2  // in front
```

Higher numbers are drawn on top. Any `Int` works, negatives too.

{{< kip tip "Use a few layers, not one each" >}}
Group things into a handful of layers: background, world, effects, HUD. If two different pictures overlap **in the same layer**, which one wins isn't promised, so give them different layers.
{{< /kip >}}
