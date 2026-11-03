---
title: "Transform"
summary: "Where an entity is, how it's turned, and how big it is: position, rotation and scale."
image: "img/words/transform.png"
---

## Picture it

Three questions about anything in the world: **Where** is it? **Which way** is it turned? **How big** is it? The answers are its **transform**.

## In klyph

```kotlin
Transform(x = 400f, y = 300f, rotation = 0.5f, scaleX = 2f, scaleY = 2f)
```

| Field | Meaning |
|---|---|
| `x`, `y` | Position in world units. See {{< term "coordinates" >}}. |
| `rotation` | Turn, in **radians**. A full circle is `2 * PI` (about 6.28). Positive turns clockwise. |
| `scaleX`, `scaleY` | Stretch. `1f` is normal, `2f` is double, `0.5f` is half. |

Change them in a system and the entity moves on the next draw:

```kotlin
app.world.forEach(Falling, Transform) { _, falling, transform ->
    transform.y += falling.speed * dt
    transform.rotation += 2f * dt   // spin while falling
}
```
