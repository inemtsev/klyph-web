---
title: "Tint"
summary: "Coloring a picture by multiplying every pixel by a color. A white picture tinted red becomes red."
image: "img/words/tint.png"
---

## Picture it

Look through a piece of red glass. White things look red, black things stay black. That's a **tint**: every pixel's color times the tint color.

## In klyph

`Sprite` has a `color`, and so do particles and text:

```kotlin
Sprite(art.gem, 44f, 44f, color = Color(0x3DDC84FFu)) // a green gem
```

- Draw your picture in **white and greys** and you can tint it any color. Gem Miner has one gem picture and five colors.
- Tinting with white, `Color.WHITE`, leaves it unchanged. Ready-made colors include `Color.RED`, `GREEN`, `BLUE`, `YELLOW`, `CYAN`, `MAGENTA`, `GRAY` and `BLACK`.
- The last two digits are **alpha**: `...80u` makes it half see-through, `...00u` invisible. `color.withAlpha(0.5f)` gives you any color, half see-through (`import klyph.platform.withAlpha`).
- To fade something out smoothly, add a `Fade` component: `add(Fade, Fade(1.seconds))`.

Flash something red for a moment when it gets hit, by setting its sprite's `color` and then setting it back. Classic!
