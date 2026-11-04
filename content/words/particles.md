---
title: "Particles"
summary: "Lots of tiny pictures that fly out, fade and vanish: sparkles, smoke, dust, fire, rain."
image: "img/words/particles.png"
---

## Picture it

A firework. One bang, hundreds of little sparks, each flying its own way, fading and gone. Each spark is a **particle**, and the thing that shoots them out is an **emitter**.

## In klyph

A `ParticleEmitter` component holds a whole pool of particles. You describe them with ranges, and each particle picks a random value in each range:

```kotlin
val sparkles = ParticleEmitter(art.sparkle, capacity = 24).apply {
    speed = 80f..240f                          // units per second
    lifetime = 300.milliseconds..700.milliseconds
    gravityY = 400f                            // pulls them down
    startColor = gemColor                      // fades to see-through by itself
    endScale = 0.2f                            // shrink
    whenEmpty = EmitterEnd.DESTROY             // tidy up when the last one is gone
    emit(24)                                   // a burst, all at once
}
```

- **Burst:** `emit(24)` fires 24 at once. Great for explosions and catches.
- **Stream:** `rate = 30f` fires 30 per second, forever. Great for smoke and torches.
- **Direction:** `direction = 270f.degrees` aims up and `spread = 30f.degrees` makes a narrow cone. Like every angle in klyph, they're really radians; `.degrees` (`import klyph.math.degrees`) turns degrees into radians for you.
- **Tidy up:** with `whenEmpty = EmitterEnd.DESTROY`, the emitter's entity is removed once its last particle has faded. Perfect for one-off bursts.

Particles aren't entities, so thousands of them are cheap.
