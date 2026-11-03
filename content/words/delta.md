---
title: "Delta time"
term: "delta time"
summary: "How long the last frame took, in seconds. Multiply speeds by it so things move at the same speed on every computer."
image: "img/words/delta.png"
---

## Picture it

You walk 2 steps every time a bell rings. On Monday it rings once a second, on Tuesday twice. You'd go twice as far on Tuesday! Better: walk "2 steps per second" and check how much time has passed. That time is **delta time** (*delta* means "change").

## In klyph

```kotlin
val dt = app.time.deltaSeconds   // e.g. 0.0167 at 60 FPS
transform.x += speed * dt        // speed in units per SECOND
```

**Speed × time = distance.** Fast computer, slow computer: the cart crosses the screen in the same time.

{{< kip oops "Don't forget it!" >}}
`transform.x += 5f` moves 5 units **per frame**, so it's twice as fast on a 120 Hz screen as on a 60 Hz one. Always multiply by `dt`.
{{< /kip >}}

In `FIXED_UPDATE`, `deltaSeconds` is always the same fixed step. See {{< term "fixed" "fixed timestep" >}}.
