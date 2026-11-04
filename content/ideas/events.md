---
title: "Events"
weight: 8
time: "3 min"
summary: "Events are short messages, like \"these two touched\" or \"the window lost focus\". They last one frame."
image: "img/ideas/events.png"
imageAlt: "A red mailbox with letters flying out"
---

## The mailbox

Sometimes one part of the game needs to tell another: "a gem landed in the cart!" or "the player picked a power-up!". Instead of calling each other directly, they use the **event bus**, `app.events`. One system posts a message; any system after it can read it.

```kotlin
// Any class can be an event.
class GemCaught(val points: Int)

// Post one:
app.events.publish(GemCaught(points = 1))

// Read them all (later this frame):
app.events.forEach<GemCaught> { event -> println("+${event.points}") }

// Or just ask "did it happen?":
if (app.events.any<GemCaught>()) shakeCamera()
```

## Two rules

1. **Events last one frame.** At the start of the next frame they're gone.
2. **Only later systems see them.** A system that runs *before* the publisher this frame never sees the event. So put readers in a later phase, or later in the same phase.

## Events klyph sends you

| Event | When |
|---|---|
| `Overlap` | Two {{< term "collider" "colliders" >}} touch. Has `a` and `b`, the two entities; `overlap.otherThan(mine)` gives the one that isn't yours. |
| `AnimationFinished` | A play-once {{< term "spritesheet" "animation" >}} reached its last frame. |
| `KeyPressed`, `KeyReleased` | A key goes down or up. |
| `MouseMoved`, `MouseButtonPressed`, `MouseWheelScrolled` | The mouse does something. |
| `GamepadConnected`, `GamepadDisconnected` | A controller is plugged in or out. |
| `WindowFocusLost`, `WindowFocusGained` | The player clicks another window, or comes back. |
| `WindowResized` | The window changes size. |

{{< kip tip "Auto-pause" >}}
Pause the game when the player switches windows:

```kotlin
if (app.events.any<WindowFocusLost>()) app.scenes.push(pause)
```
{{< /kip >}}
