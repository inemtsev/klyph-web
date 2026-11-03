---
title: "Camera"
summary: "Decides which part of the game world shows up in the window, and how zoomed in it is."
image: "img/words/camera.png"
---

## Picture it

Your game world can be much bigger than the window. The **camera** is like a film camera pointing at part of it. Move the camera and the world seems to scroll. Zoom it and everything gets bigger.

## In klyph

There's one camera, `app.camera`. Set it in `LATE_UPDATE`, after everything has moved:

```kotlin
val camera = app.camera
camera.lookAt(player.x, player.y)   // center on the player
camera.zoom = 2f                     // everything twice as big
```

To follow smoothly instead of snapping, move part of the way each frame:

```kotlin
camera.lookAt(camera.x + (player.x - camera.x) * 0.1f, camera.y + (player.y - camera.y) * 0.1f)
```

`camera.visibleWidth` and `visibleHeight` tell you how much of the world fits in the window, and `app.input.mouse.worldPosition` tells you where the mouse is *in the world*, with the camera taken into account.

{{< kip tip "Screen shake" >}}
Nudge `camera.x` and `camera.y` by a few random units for a quarter of a second when something big happens. Instant drama!
{{< /kip >}}
