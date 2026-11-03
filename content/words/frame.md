---
title: "Frame and FPS"
linkTitle: "Frame"
term: "frame"
summary: "One picture of your game. FPS (frames per second) is how many your game draws each second. 60 is smooth."
image: "img/words/frame.png"
---

## Picture it

A movie is lots of still pictures shown quickly. Each is a **frame**. Games work the same, except each frame is drawn on the spot. **FPS** means *frames per second*. At 60 FPS each frame has about 16 milliseconds. Fall behind, and the game looks jumpy.

## In klyph

- `app.time.frame` counts the frames drawn so far.
- By default klyph waits for your screen's refresh (*vsync*): 60 FPS on most screens, 120 or more on fast ones.
- Press <kbd>F3</kbd> in any klyph game for the **debug overlay**: FPS, how long each phase took, and a frame-time graph.

Because frames can take different times, move things with {{< term "delta" "delta time" >}}, never "5 pixels per frame".
