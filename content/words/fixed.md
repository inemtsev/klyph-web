---
title: "Fixed timestep"
term: "fixed timestep"
summary: "Running game physics in exactly equal time steps (like 60 per second), however long each frame takes."
image: "img/words/fixed.png"
---

## Picture it

A metronome ticks at a perfectly steady beat, even if the band plays a bit fast or slow. A **fixed timestep** is a metronome for your physics: always exactly 1/60 of a second per step.

Why bother? Jumps, bounces and gravity can come out slightly different with uneven steps. A jump might go higher on a fast computer! Equal steps make the game behave the same everywhere.

## In klyph

Systems in `FIXED_UPDATE` run at a steady `AppConfig.fixedRate` (60 by default). If a frame was slow, klyph runs two or three steps to catch up. If it was quick, maybe none.

```kotlin
addSystem(Phase.FIXED_UPDATE) { app ->
    val dt = app.time.deltaSeconds   // always 1/60 here
    velocityY += GRAVITY * dt
    transform.y += velocityY * dt
}
```

## Jumping in a fixed step

Read the jump button right where the physics runs. Since klyph 1.1, `isJustPressed` and `isJustReleased` count **steps** inside `FIXED_UPDATE`:

- The first step after a press sees it. No other step does.
- A frame that runs no step keeps the press for the next frame's first step.
- A tap shorter than a step reads as both just pressed and just released in that step.

```kotlin
addSystem(Phase.FIXED_UPDATE) { app ->
    if (app.input.keyboard.isJustPressed(Scancode.SPACE) && onGround) velocityY = -600f
}
```

So every press makes exactly one jump, however many steps a frame runs. Everywhere else, "just" still means "this frame".

{{< nerd "Smooth drawing between steps" >}}
If things only move in fixed steps but the screen draws at 144 FPS, motion can look a bit steppy. Add the `Interpolated` component to an entity (`add(Interpolated, Interpolated)`) and klyph draws it smoothly between its last two positions.
{{< /nerd >}}
