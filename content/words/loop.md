---
title: "Game loop"
summary: "The loop at the heart of every game: read input, update the world, draw it, and repeat, many times a second."
image: "img/words/loop.png"
---

## Picture it

A flip book again, but you draw each page **while** flipping, based on what the player just did. Read the buttons, move things, draw the page, repeat. That's the **game loop**. It never stops until the game ends.

## In klyph

`runApp` runs the loop for you. Each trip round is a {{< term "frame" >}}, split into phases:

{{< phases >}}

You add {{< idea "systems" >}} to the phases. See {{< idea "loop" "the frame loop" >}} for what each phase is for.
