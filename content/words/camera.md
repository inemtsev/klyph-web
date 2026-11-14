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

If your player has the `Interpolated` component (see {{< term "fixed" "fixed timestep" >}}), its picture is drawn a little behind its `Transform`. Follow where the player is **drawn**, so the camera and the picture stay together:

```kotlin
camera.follow(app.drawnPositionOf(player), smoothing = 5f, delta = app.time.delta)   // player is the entity
```

(`drawnPositionOf` is `import klyph.app.drawnPositionOf`. It also works for an entity without `Interpolated`.)

`camera.visibleWidth` and `visibleHeight` tell you how much of the world fits in the window, and `app.input.mouse.worldPosition` tells you where the mouse is *in the world*, with the camera taken into account.

{{< kip tip "Screen shake" >}}
When something big happens, set `camera.offsetX` and `camera.offsetY` to a few random units on every frame for a quarter of a second, then set both back to `0f`. They sit on top of `follow` and `bounds`, so the camera keeps following while it shakes (new in klyph 1.2). Instant drama!
{{< /kip >}}
