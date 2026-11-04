---
title: "Camera"
summary: "Decides which part of the game world shows up in the window, and how zoomed in it is."
image: "img/words/camera.png"
---

## Picture it

Your game world can be much bigger than the window. The **camera** is like a film camera pointing at part of it. Move the camera and the world seems to scroll. Zoom it and everything gets bigger.

## In klyph

There's one camera, `app.camera`. Move it in `LATE_UPDATE`, after everything else has moved:

```kotlin
val camera = app.camera
camera.lookAt(player.x, player.y)   // center on the player
camera.zoom = 2f                     // everything twice as big
```

At zoom 1, one world unit is one point of the window, on every screen.

To follow smoothly instead of snapping, `follow` moves the camera part of the way each frame. And `bounds` stops it from showing anything outside your level:

```kotlin
camera.bounds = Rect(x = 0f, y = 0f, width = 4000f, height = 3000f)   // the level's edges
camera.follow(player.x, player.y, smoothing = 5f, delta = app.time.delta)
```

A bigger `smoothing` catches up faster. (`Rect` is `import klyph.math.Rect`, and `follow` is `import klyph.render.follow`.)

`camera.visibleWidth` and `visibleHeight` tell you how much of the world fits in the window, and `app.input.mouse.worldPosition` tells you where the mouse is *in the world*, with the camera taken into account.

{{< kip tip "Screen shake" >}}
Nudge `camera.x` and `camera.y` by a few random units for a quarter of a second when something big happens. Instant drama!
{{< /kip >}}
