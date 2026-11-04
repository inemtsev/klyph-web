---
title: "The frame loop and phases"
linkTitle: "Frame loop and phases"
weight: 2
time: "4 min"
summary: "Every frame, klyph runs your systems in a fixed order of phases: input, game rules, then drawing."
image: "img/ideas/loop.png"
imageAlt: "A conveyor belt loop with a golden clock gear"
---

## Round and round

A game is a {{< term "loop" "loop" >}}. Many times every second (usually 60, or more on fast screens), klyph does one **frame**: read the keys, update the game, draw a picture. Each frame is split into **phases**, and every {{< idea "systems" "system" >}} belongs to exactly one.

{{< phases >}}

| Phase | When | Put here… |
|---|---|---|
| `STARTUP` | Once, before the first frame | Loading art and sounds, choosing the first scene. |
| `INPUT` | First, every frame | Reading keys: "was <kbd>Space</kbd> just pressed?" |
| `FIXED_UPDATE` | 0, 1 or more times a frame, at a steady 60 per second | Physics and anything that must tick at an exact rate. |
| `UPDATE` | Once a frame | Most game rules: moving, catching, scoring. |
| `LATE_UPDATE` | After `UPDATE` | The camera, and anything that needs final positions. |
| `RENDER` | Last | Drawing with `app.canvas`: HUD, text, backgrounds. |
| `SHUTDOWN` | Once, after the last frame | Saving, printing a goodbye. |

Inside one phase, systems run **in the order you added them**.

## Why FIXED_UPDATE?

Frames don't all take the same time. A busy frame might take 0.03 seconds, a quiet one 0.008. For most things, multiplying by {{< term "delta" "delta time" >}} handles that. But physics (jumping, bouncing, gravity) behaves best with **exactly equal** steps. That's a {{< term "fixed" "fixed timestep" >}}: `FIXED_UPDATE` runs at exactly 60 steps per second, catching up with extra steps when a frame was slow.

## What the engine does in between

You don't write these, but it helps to know they happen:

- **Before `INPUT`:** last frame's events are dropped and new keyboard, mouse and window events come in.
- **After `FIXED_UPDATE`:** {{< term "collider" "collisions" >}} are checked and `Overlap` {{< idea "events" >}} are sent.
- **After `UPDATE`:** `Lifetime` and `Fade` timers count down, then {{< term "particles" >}} and {{< term "spritesheet" "sprite animations" >}} move one frame on.
- **After `RENDER`:** the {{< term "camera" >}} is kept inside its bounds, everything is drawn, then scene changes (push, pop, replace) take effect.

{{< nerd "The whole frame, in order" >}}
`runApp` runs STARTUP once, then each frame: drop last frame's events, refresh audio, translate platform events into `app.input` and `app.events`, toggle the overlay, INPUT, FIXED_UPDATE zero or more times, update transforms, detect collisions and publish `Overlap` events, UPDATE, count down `Lifetime` and `Fade`, advance particles, advance sprite animations (publishing `AnimationFinished`), LATE_UPDATE, update transforms again, RENDER, keep the camera inside its bounds, draw sprites, text, particles and the canvas, apply scene transitions, and quit if a frame or time limit is reached. Commands are applied after every system and after each of the engine's own steps. After the last frame every scene leaves the stack and SHUTDOWN runs.
{{< /nerd >}}
