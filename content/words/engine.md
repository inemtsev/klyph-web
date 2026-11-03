---
title: "Game engine"
term: "game engine"
summary: "The toolkit that does the hard, boring parts of a game (windows, drawing, sound, input) so you can make the fun parts."
image: "img/words/engine.png"
---

## Picture it

Building a game from nothing is like building a car starting with digging up iron. A **game engine** hands you the engine, wheels and steering wheel. You design the car.

It handles things every game needs: opening a window, drawing pictures fast with the graphics card, playing sound, reading the keyboard, keeping time. You write what makes *your* game special.

## In klyph

klyph is a small 2D engine for Kotlin. `runApp` starts it, and from then on it calls your {{< idea "systems" >}} at the right moments. Some engines come with a big editor app. klyph is **code-first**: your game is just Kotlin files.
