---
title: "Move it!"
step: 5
weight: 5
time: "10 min"
summary: "Make your own component, read the keyboard, and slide the cart left and right."
learn:
  - "How to make your own **component**"
  - "How a system finds entities with a **query**"
  - "Why we multiply by **delta time**"
image: "img/words/input.png"
imageAlt: "A game controller, a keyboard key and a mouse"
---

## 1. Make a `Cart` component

The cart needs to know how fast it goes. That's data, and data goes in a component. Make **`Components.kt`**:

```kotlin {title="Components.kt"}
package gemminer

import klyph.entity.ComponentType

// Components are plain data you stick onto entities. Each one has a companion object that
// klyph uses as its key, so you can write world.get(entity, Cart).

/** The player's minecart. */
class Cart(val speed: Float) {
    companion object : ComponentType<Cart>("Cart")
}
```

A component is just a normal Kotlin class. The `companion object : ComponentType<Cart>("Cart")` line is the **key** klyph uses to find it.

Now stick it on the cart. Add one line to `spawnCart` in **`Spawns.kt`**:

```kotlin {title="Spawns.kt" hl_lines="5"}
fun Commands.spawnCart(art: Art) {
    spawn {
        add(Transform, Transform(Tuning.WORLD_WIDTH / 2, Tuning.CART_Y))
        add(Sprite, Sprite(art.cart, Tuning.CART_WIDTH, Tuning.CART_HEIGHT, layer = Tuning.LAYER_THINGS))
        add(Cart, Cart(Tuning.CART_SPEED))
    }
}
```

## 2. The moving system

Add this to **`Systems.kt`** (and the new imports at the top):

```kotlin {title="Systems.kt"}
import klyph.app.deltaSeconds
import klyph.entity.Transform
import klyph.input.axis
import klyph.platform.Scancode

/** Slides the cart with A / D or the arrow keys, and keeps it on the screen. */
fun moveCart(app: App) {
    val direction = (app.input.axis(Scancode.LEFT, Scancode.RIGHT) + app.input.axis(Scancode.A, Scancode.D)).coerceIn(-1f, 1f)
    val dt = app.time.deltaSeconds
    val half = Tuning.CART_WIDTH / 2
    app.world.forEach(Cart, Transform) { _, cart, transform ->
        transform.x = (transform.x + direction * cart.speed * dt).coerceIn(half, Tuning.WORLD_WIDTH - half)
    }
}
```

Three new ideas in a few lines:

1. **`app.input.axis(LEFT, RIGHT)`** gives `-1` while ← is held, `1` while → is held, and `0` otherwise. We add up arrows and {{< keys "A" "D" >}}, then `coerceIn` keeps it between -1 and 1. See {{< term "input" >}}.
2. **`app.world.forEach(Cart, Transform)`** is a **query**: "give me every entity that has a `Cart` *and* a `Transform`". Rocks and gems won't have a `Cart`, so they'll never move like one.
3. **`dt`** is {{< term "delta" "delta time" >}}: how many seconds the last frame took (about 0.016). Speed × time = distance, so the cart moves 420 units per **second**, on a slow computer or a fast one.

## 3. Run it every frame

In **`Main.kt`**, add one line inside `runApp`:

```kotlin {title="Main.kt" hl_lines="7"}
    runApp(config) {
        addSystem(Phase.STARTUP) { app ->
            app.resources.insert(Art.load(app.assets))
            app.commands.spawnCart(app.art)
        }

        addSystem(Phase.UPDATE) { app -> moveCart(app) }
        addSystem(Phase.LATE_UPDATE) { app -> fitCamera(app) }
    }
```

Run it and press {{< keys "A" "D" >}} or the arrow keys. **Vroom!** 🛒

{{< checkpoint "05-move-it" >}}

{{< kip oops "Nothing moves?" >}}
Click the game window first, so it gets the keyboard. Still stuck? Check that `spawnCart` adds the `Cart` component. Without it, the query skips the cart.
{{< /kip >}}

{{< try >}}
Make `CART_SPEED` in `Tuning.kt` much bigger, like `1200f`. Too fast? What feels best to you? Game makers call this **tuning**.
{{< /try >}}
