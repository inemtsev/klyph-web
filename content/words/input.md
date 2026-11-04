---
title: "Input"
summary: "Everything the player does to control the game: keys, mouse, touchpad and gamepads."
image: "img/words/input.png"
---

## Picture it

The game asks, every frame: "What is the player pressing **right now**?" That's input. Games usually care about three moments: a button is **down**, was **just pressed** this frame, or was **just released**.

## In klyph

All of it is in `app.input`, updated at the start of each frame.

```kotlin
val keys = app.input.keyboard
keys.isDown(Scancode.SPACE)          // held down right now
keys.isJustPressed(Scancode.SPACE)   // went down this frame: jump, shoot, select
keys.isJustReleased(Scancode.SPACE)  // came up this frame

// -1, 0 or 1, for moving left and right:
val dx = keys.axis(Scancode.A, Scancode.D)   // import klyph.input.axis

// The mouse:
val mouse = app.input.mouse
mouse.worldPosition                     // where it points in the world
mouse.isJustPressed(MouseButton.LEFT)   // clicked

// Gamepads:
val pad = app.input.gamepads.firstOrNull()        // null when none is plugged in
val stickX = pad?.leftStick?.x ?: 0f              // -1 (left) to 1 (right)
if (app.input.gamepads.isJustPressed(GamepadButton.SOUTH)) jump()   // any pad: A on Xbox, ✕ on PlayStation (import klyph.input.isJustPressed)
```

{{< kip tip "Scancodes are positions" >}}
`Scancode.W` means "the key where W is on a US keyboard". On a French keyboard, that's Z. That's what you want for {{< keys "W" "A" "S" "D" >}} movement: the keys stay in the same *place* for everyone.

For a key you name by its **letter**, like "press <kbd>M</kbd> for music", use `KeyCode.M` instead: it follows the player's keyboard layout.
{{< /kip >}}
