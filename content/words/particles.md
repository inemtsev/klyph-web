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
val sparkles = ParticleEmitter(art.sparkle, maxParticles = 24).apply {
    speed = 80f..240f          // units per second
    lifetime = 0.3f..0.7f      // seconds
    gravityY = 400f            // pulls them down
    startColor = gemColor
    endColor = Color(gemColor.red, gemColor.green, gemColor.blue, alpha = 0f) // fade out
    endScale = 0.2f            // shrink
    emit(24)                   // a burst, all at once
}
```

- **Burst:** `emit(24)` fires 24 at once. Great for explosions and catches.
- **Stream:** `rate = 30f` fires 30 per second, forever. Great for smoke and torches.
- **Direction:** `direction = 270f` aims up and `spread = 30f` makes a narrow cone. These angles are in degrees.

Particles aren't entities, so thousands of them are cheap.
