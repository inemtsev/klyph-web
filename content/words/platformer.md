---
title: "Platformer"
term: "platformer"
summary: "A game where you run and jump between floors and ledges, and gravity pulls you back down."
image: "img/kip/cheer.png"
imageAlt: "Kip jumping for joy"
---

## Picture it

A hero runs along a rooftop, jumps a gap and lands on the next roof. Floors hold the hero up, walls stop them, and gravity always pulls down. That's a **platformer**.

## In klyph

New in klyph 1.1. Two {{< idea "entities" "components" >}} do the standing and stopping:

- **`Solid`** turns a box {{< term "collider" >}} into a wall, floor or ledge.
- **`KinematicBody`** moves its entity in every {{< term "fixed" "fixed step" >}} and stops at the solids in its way.

```kotlin
// A roof, and a ledge you can jump up through.
commands.spawn {
    add(Transform, Transform(x = 450f, y = 700f))
    add(Collider, Collider(Shape.Box(width = 900f, height = 600f)))
    add(Solid, Solid())
}
commands.spawn {
    add(Transform, Transform(x = 1000f, y = 306f))
    add(Collider, Collider(Shape.Box(width = 280f, height = 12f)))
    add(Solid, Solid(passable = Passable.FROM_BELOW))
}
```

`Passable.FROM_BELOW` makes a **one-way ledge**: you land on it from above and jump up through it from below.

The hero is a box with a `KinematicBody`:

```kotlin
val hero = commands.spawn {
    add(Transform, Transform(x = 100f, y = 380f - Collisions.CONTACT_GAP))
    add(Sprite, Sprite(assets.whitePixel, width = 24f, height = 40f, color = Color.YELLOW))
    add(Collider, Collider(Shape.Box(width = 24f, height = 40f)))
    add(KinematicBody, KinematicBody())
    add(Interpolated, Interpolated)
}
```

Its `dx` and `dy` are its speed, in units per second. The engine moves it, but it adds **no gravity**. That's your job, in a `FIXED_UPDATE` system that runs just before the move:

```kotlin
addSystem(Phase.FIXED_UPDATE) { app ->
    val body = app.world.get(hero, KinematicBody)
    val keyboard = app.input.keyboard
    body.dx = keyboard.axis(Scancode.A, Scancode.D) * 320f
    if (keyboard.isJustPressed(Scancode.SPACE) && body.isOnFloor) body.dy = -700f
    body.dy = minOf(body.dy + 1800f * app.time.deltaSeconds, 900f)   // gravity
    if (body.isOnFloor && keyboard.isJustPressed(Scancode.S)) body.dropThrough()
}
```

Reading `isJustPressed` here is fine: in `FIXED_UPDATE` each press reaches exactly one step. After every move the body tells you what it touched: `isOnFloor`, `isOnWall` and `isOnCeiling`. `dropThrough()` falls through the one-way ledge it stands on.

{{< kip tip "Make jumps feel nice" >}}
Real platformers cheat a little. **Coyote time** lets a jump work a moment after you ran off an edge. A **jump buffer** remembers a press made just before landing. Both are two little timers in your system.
{{< /kip >}}

{{< nerd "What it doesn't do" >}}
There's no physics engine behind it: no mass, no bouncing, no slopes and no moving platforms. Bodies and solids are upright boxes, and bodies don't stop each other.
{{< /nerd >}}

Want the whole thing, with coyote time, a jump buffer and enemies that turn at edges? Read the [Platformers guide](https://inemtsev.github.io/klyph/guide/platformers.html), then play and read **Rooftop Roller**, the platformer sample in [`games/roller`](https://github.com/inemtsev/klyph/tree/main/games/roller).
