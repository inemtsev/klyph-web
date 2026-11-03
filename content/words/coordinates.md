---
title: "Coordinates"
summary: "Numbers that say where something is: x goes right, y goes down, and (0, 0) is the top-left corner."
image: "img/words/coordinates.png"
---

## Picture it

A treasure map on graph paper. "Go 5 squares right and 3 squares down." That's **x = 5, y = 3**. Every spot in your game world has an x and a y.

## In klyph

- **x** grows to the **right**.
- **y** grows **down**. (Like reading a page: top to bottom.)
- **(0, 0)** starts at the top-left of the window, until you move the {{< term "camera" >}}.

In Gem Miner the world is 800 wide and 600 tall, so the cart at y = 530 is near the bottom, and gems start at y = -44, just above the top.

{{< kip think "Why does y go down?" >}}
Screens are drawn top to bottom, row by row, so most 2D tools count that way. Your maths teacher's graphs go *up*. Just remember: in games, **bigger y = lower**.
{{< /kip >}}

There are two spaces: **world** space (moves with the camera) and **screen** space (stuck to the window, in points). Game things live in the world; the score lives on the screen.
