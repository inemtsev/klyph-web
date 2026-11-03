---
title: "Collider (hitbox)"
linkTitle: "Collider"
term: "collider"
summary: "An invisible shape on an entity that the game uses to tell when two things touch. Often called a hitbox."
image: "img/words/collider.png"
---

## Picture it

Pictures have see-through corners and odd shapes. Checking every pixel would be slow and fiddly. So games give things a simple invisible shape, a box or a circle, and check if the **shapes** touch. That shape is a **collider**, or **hitbox**.

## In klyph

```kotlin
add(Collider, Collider(Shape.Circle(18f), layers = Layers.FALLING, mask = CollisionLayers.NONE))
add(Collider, Collider(Shape.Box(96f, 40f), layers = Layers.CART, mask = Layers.FALLING))
```

- **Shapes:** `Shape.Box(width, height)` or `Shape.Circle(radius)`, centered on the entity. `offsetX`, `offsetY` move it.
- **`layers`:** what this thing *is*. Up to 32 layers, made with `CollisionLayers.of(0..31)`.
- **`mask`:** which layers it *looks for*. A pair is reported if either one looks for the other.

Every frame, klyph sends an `Overlap` {{< idea "events" "event" >}} for each touching pair:

```kotlin
app.events.forEach<Overlap> { overlap -> /* overlap.a and overlap.b touched */ }
```

You can also **ask** what's somewhere, like "is anything under the mouse?" (`import klyph.collision.overlaps`):

```kotlin
val m = app.input.mouse.worldPosition
val hits = app.collisions.overlaps(Shape.Circle(30f), x = m.x, y = m.y, mask = Layers.FALLING)
```

{{< kip tip "Make hitboxes a bit smaller" >}}
A hitbox a little smaller than the picture feels **fair**. Players get annoyed when a rock "hits" them without visibly touching.
{{< /kip >}}
