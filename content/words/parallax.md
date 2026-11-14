---
title: "Parallax"
term: "parallax"
summary: "Far things slide past slowly and near things slide past fast, which makes a flat picture look deep."
image: "img/kip/point.png"
imageAlt: "Kip pointing off into the distance"
---

## Picture it

Look out of a car window. Fence posts by the road whip past, the hills far away drift by slowly, and the moon seems to stay put. The farther away something is, the less it seems to move. That's **parallax**.

A 2D game fakes it by drawing the sky, the hills and the ground as separate pictures and scrolling each one at its own speed. That makes a flat picture look deep.

## In klyph

New in klyph 1.2. A **`ParallaxLayer`** is a {{< idea "entities" "component" >}} that makes a {{< term "sprite" >}} scroll slower or faster than the world when the {{< term "camera" >}} moves. Put it on an entity that has a {{< term "transform" "`Transform`" >}} and a `Sprite`. The number you give it is the **factor**:

| Factor | The layer is |
|---|---|
| `0f` | Stuck to the screen, like a sky or the moon. |
| `0.5f` | Far away. It moves at half the world's speed. |
| `1f` | Part of the world. It moves like any sprite. |
| `1.5f` | Close up, like a fence in front. It moves faster than the world. |

Here are hills that scroll at half speed and never run out:

```kotlin
val hills = Sprite(app.assets.texture("hills.png").asRegion(), layer = -2)
app.commands.spawn {
    add(Transform, Transform(x = 0f, y = 400f))
    add(Sprite, hills)
    add(ParallaxLayer, ParallaxLayer(0.5f, factorY = 1f, repeatX = hills.width))
}
```

- `repeatX` is the distance from one copy of the picture to the next. Set it to the picture's width and the copies touch.
- klyph draws every copy the camera can see, so one entity makes a strip that never ends.
- `factorY = 1f` makes the hills move up and down with the world, so only the sideways scroll is slowed.
- `layer = -2` draws the hills behind the other sprites. See {{< term "layers" >}}.

With a factor of `0f`, the `Transform` is measured from the middle of the window:

```kotlin
val moon = Sprite(app.assets.texture("moon.png").asRegion(), layer = -3)
app.commands.spawn {
    add(Transform, Transform(x = 250f, y = -200f))   // 250 right, 200 up from the middle
    add(Sprite, moon)
    add(ParallaxLayer, ParallaxLayer(0f))             // stays put as the camera moves
}
```

{{< kip tip "Put one in front" >}}
A factor above `1f`, like `1.5f`, makes a layer move faster than the world. Give it a high `layer`, and a row of grass or a fence in front makes everything behind it feel farther away.
{{< /kip >}}

{{< nerd "Where does my layer end up?" >}}
klyph draws a layer at its `Transform` plus `1 - factor` times the place the camera looks at. At a factor of `1f` that is exactly the `Transform`, and at `0f` it's an offset from the middle of the window. A layer can't have a parent entity. klyph draws at most 256 copies of one picture, so a tiny `repeatX` leaves the far copies out.
{{< /nerd >}}

The [Scrolling worlds guide](https://inemtsev.github.io/klyph/guide/scrolling.html) has every detail. Rooftop Roller, in [`games/roller`](https://github.com/inemtsev/klyph/tree/main/games/roller), uses parallax layers for its clouds and its skyline.
