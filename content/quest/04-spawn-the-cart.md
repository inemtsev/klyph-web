---
title: "Spawn the cart"
step: 4
weight: 4
time: "12 min"
summary: "Load the pictures, make your first entity (the minecart) and point the camera at the mine."
learn:
  - "What **entities** and **components** are"
  - "How to load pictures from `assets/`"
  - "How the **camera** decides what you see"
image: "img/ideas/entities.png"
imageAlt: "A stone cube with colorful stickers on it"
---

## Entities are things, components are stickers

Everything in your game world is an {{< idea "entities" "entity" >}}: the cart, each gem, each rock. On its own, an entity is just a name tag. It does nothing.

You give it **components** to make it something. Think of them as stickers:

- a {{< term "transform" "`Transform`" >}} sticker says **where** it is,
- a {{< term "sprite" "`Sprite`" >}} sticker says **what it looks like**.

Cart = entity + `Transform` + `Sprite`. That's it!

## 1. A file for the numbers

Games are full of numbers: sizes, speeds, timings. Keep them in one place so they're easy to tweak. Make a new file, **`Tuning.kt`**, and paste this. It has every number for the whole game, so you only do it once:

```kotlin {title="Tuning.kt"}
package gemminer

import klyph.platform.Color

/** Every number that changes how the game feels, in one place so you can play with them. */
object Tuning {
    /** The world is 800 by 600 units; the camera fits it into the window at any size. */
    const val WORLD_WIDTH = 800f
    const val WORLD_HEIGHT = 600f

    val CAVE_COLOR = Color(0x2B1D14FFu)

    // The cart. The picture is 224 x 147 pixels, drawn at half size so it stays sharp on big screens.
    const val CART_WIDTH = 112f
    const val CART_HEIGHT = 74f
    const val CART_Y = 530f
    const val CART_SPEED = 420f

    // Falling things
    const val GEM_SIZE = 44f
    const val ROCK_SIZE = 56f
    const val FIRST_DROP_DELAY = 1f
    const val DROP_EVERY = 0.7f
    const val FALL_SPEED = 160f

    /** How much faster things fall for every second you survive. */
    const val FALL_SPEEDUP = 4f
    const val ROCK_CHANCE = 0.25f
    const val STARTING_LIVES = 3

    /** Gems come in five colors; one white gem picture is tinted with these. */
    val GEM_COLORS = listOf(
        Color(0xFF4D6DFFu), // ruby
        Color(0x3DDC84FFu), // emerald
        Color(0x4DA6FFFFu), // sapphire
        Color(0xFFD23FFFu), // topaz
        Color(0xB36BFFFFu), // amethyst
    )

    // Draw order: higher layers are drawn on top.
    const val LAYER_WALL = -10
    const val LAYER_THINGS = 1
    const val LAYER_SPARKLES = 2
}
```

## 2. Load the pictures

The pictures live in the `assets/` folder. When the game builds, klyph copies that folder next to your program, and {{< idea "assets" "`app.assets`" >}} loads files from it. Make **`Art.kt`**:

```kotlin {title="Art.kt"}
package gemminer

import klyph.app.App
import klyph.app.Assets
import klyph.render.TextureRegion
import klyph.render.asRegion

/** Every picture the game draws, loaded once at startup from the assets/ folder. */
class Art(
    val cart: TextureRegion,
    val gem: TextureRegion,
    val rock: TextureRegion,
    val sparkle: TextureRegion,
    val wall: TextureRegion,
) {
    companion object {
        fun load(assets: Assets): Art = Art(
            cart = assets.texture("cart.png").asRegion(),
            gem = assets.texture("gem.png").asRegion(),
            rock = assets.texture("rock.png").asRegion(),
            sparkle = assets.texture("sparkle.png").asRegion(),
            wall = assets.texture("wall.png").asRegion(),
        )
    }
}

/** A shortcut, so a system can write `app.art` instead of `app.resources.get<Art>()`. */
val App.art: Art get() = resources.get()
```

We load **all** the pictures now, even the ones we'll use later. Each one becomes a {{< term "texture" >}} in the graphics card.

The `Art` object will be a {{< idea "resources" "resource" >}}: something the whole game shares that isn't an entity. The last line is a little shortcut so any system can just say `app.art`.

## 3. Spawn the cart

Make **`Spawns.kt`**. It holds one function per kind of entity:

```kotlin {title="Spawns.kt"}
package gemminer

import klyph.entity.Commands
import klyph.entity.Transform
import klyph.render.Sprite

/** The minecart, at the bottom of the screen in the middle. */
fun Commands.spawnCart(art: Art) {
    spawn {
        add(Transform, Transform(Tuning.WORLD_WIDTH / 2, Tuning.CART_Y))
        add(Sprite, Sprite(art.cart, Tuning.CART_WIDTH, Tuning.CART_HEIGHT, layer = Tuning.LAYER_THINGS))
    }
}
```

`spawn { }` makes a new entity, and each `add` sticks a component on it. Notice you name the component **twice**: `add(Transform, Transform(...))`. The first is the *kind* of sticker, the second is the sticker itself.

{{< kip tip "Where is (400, 530)?" >}}
In klyph, (0, 0) is the **top-left** corner and **y goes down**. So `y = 530` is near the bottom of our 600-tall world. It's the same in most 2D games; see {{< term "coordinates" >}}.
{{< /kip >}}

## 4. Point the camera

The {{< term "camera" >}} decides which part of the world shows up in the window. Make **`Systems.kt`**:

```kotlin {title="Systems.kt"}
package gemminer

import klyph.app.App
import klyph.render.lookAt
import kotlin.math.min

/** Zooms the camera so the whole 800 x 600 world fits in the window, whatever its size. */
fun fitCamera(app: App) {
    val camera = app.camera
    camera.zoom = min(camera.viewportWidth / Tuning.WORLD_WIDTH, camera.viewportHeight / Tuning.WORLD_HEIGHT)
    camera.lookAt(Tuning.WORLD_WIDTH / 2, Tuning.WORLD_HEIGHT / 2)
}
```

## 5. Hook it all up

Replace **`Main.kt`**. (Bye-bye, torch glow. The cave wall picture will cover the background soon anyway.)

```kotlin {title="Main.kt"}
package gemminer

import klyph.app.AppConfig
import klyph.app.Phase
import klyph.app.runApp

fun main() {
    val config = AppConfig(
        title = "Gem Miner",
        width = 800,
        height = 600,
        clearColor = Tuning.CAVE_COLOR,
    )

    runApp(config) {
        addSystem(Phase.STARTUP) { app ->
            app.resources.insert(Art.load(app.assets))
            app.commands.spawnCart(app.art)
        }

        addSystem(Phase.LATE_UPDATE) { app -> fitCamera(app) }
    }
}
```

Run it. **There's your cart!** Try resizing the window: the camera keeps the whole mine in view.

{{< nerd "Why is `fitCamera` in LATE_UPDATE?" >}}
The window can change size at any moment, so we fit the camera every frame. `LATE_UPDATE` runs after the game rules (`UPDATE`) and just before drawing, which is the usual spot for camera work. If the camera followed a player, you'd want the player's *final* position for the frame.
{{< /nerd >}}

{{< try >}}
Spawn a **second** cart: call `app.commands.spawnCart(app.art)` twice. Where is it? (Hint: exactly on top of the first one!) Can you change `spawnCart` to take an `x` so they sit side by side?
{{< /try >}}
