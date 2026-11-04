---
title: "Catch!"
step: 7
weight: 7
time: "12 min"
summary: "Give things colliders, let klyph spot when a gem lands in the cart, and count your gems."
learn:
  - "What a **collider** is, and how **layers** and **masks** pick who bumps into whom"
  - "How to read **events**, like `Overlap`"
image: "img/words/collider.png"
imageAlt: "A blue gem inside a green dashed box"
---

## 1. Colliders

A {{< term "collider" >}} is an invisible shape stuck on an entity. When two colliders touch, klyph tells you. Pictures don't touch; colliders do.

First, name your **collision layers**. Add this to **`Components.kt`** (and `import klyph.collision.CollisionLayers`):

```kotlin {title="Components.kt"}
/** Collision layers: the cart looks for falling things, and nothing else. */
object Layers {
    val CART: CollisionLayers = CollisionLayers.of(1)
    val FALLING: CollisionLayers = CollisionLayers.of(2)
}
```

Now give each entity a `Collider` in **`Spawns.kt`**. Add the highlighted lines (and import `klyph.collision.Collider`, `klyph.collision.CollisionLayers` and `klyph.collision.Shape`):

```kotlin {title="Spawns.kt" hl_lines="5 6 15 25"}
fun Commands.spawnCart(art: Art) {
    spawn {
        add(Transform, Transform(Tuning.WORLD_WIDTH / 2, Tuning.CART_Y))
        add(Sprite, Sprite(art.cart, Tuning.CART_WIDTH, Tuning.CART_HEIGHT, layer = Tuning.LAYER_THINGS))
        // The cart looks for falling things, so the engine tells us when one touches it.
        add(Collider, Collider(Shape.Box(96f, 40f), layers = Layers.CART, mask = Layers.FALLING))
        add(Cart, Cart(Tuning.CART_SPEED))
    }
}

fun Commands.spawnGem(art: Art, x: Float, speed: Float, color: Color) {
    spawn {
        add(Transform, Transform(x, -Tuning.GEM_SIZE))
        add(Sprite, Sprite(art.gem, Tuning.GEM_SIZE, Tuning.GEM_SIZE, color = color, layer = Tuning.LAYER_THINGS))
        add(Collider, Collider(Shape.Circle(18f), layers = Layers.FALLING, mask = CollisionLayers.NONE))
        add(Falling, Falling(speed))
        add(Gem, Gem(points = 1))
    }
}

fun Commands.spawnRock(art: Art, x: Float, speed: Float) {
    spawn {
        add(Transform, Transform(x, -Tuning.ROCK_SIZE))
        add(Sprite, Sprite(art.rock, Tuning.ROCK_SIZE, Tuning.ROCK_SIZE, layer = Tuning.LAYER_THINGS))
        add(Collider, Collider(Shape.Circle(24f), layers = Layers.FALLING, mask = CollisionLayers.NONE))
        add(Falling, Falling(speed))
        add(Rock, Rock())
    }
}
```

- **`layers`** says what something *is*: the cart is `CART`, gems and rocks are `FALLING`.
- **`mask`** says what it *looks for*: the cart looks for `FALLING`. Gems look for `NONE`, so two gems touching each other is never reported. Less noise, and faster!

The cart's box is a bit smaller than its picture, so a gem has to land *in* the cart, not just brush its wheel.

## 2. Read the Overlap events

Every frame, klyph checks all the colliders and sends an `Overlap` {{< idea "events" "event" >}} for each touching pair. Add this to **`Systems.kt`** (and import `klyph.collision.Overlap`, `klyph.collision.otherThan` and `klyph.entity.Entity`):

```kotlin {title="Systems.kt"}
/** Reads the engine's Overlap events: a gem in the cart scores, a rock costs a life. */
fun Round.catchThings(app: App) {
    val world = app.world
    app.events.forEach<Overlap> { overlap ->
        // One of the pair is the cart, but the engine doesn't promise which one.
        val cart = if (world.has(overlap.a, Cart)) overlap.a else overlap.b
        catchOne(app, overlap.otherThan(cart))
    }
}

private fun Round.catchOne(app: App, thing: Entity) {
    val world = app.world
    val gem = world.getOrNull(thing, Gem)
    if (gem != null) {
        gems += gem.points
        println("Gems: $gems")
        app.commands.destroy(thing)
    } else if (world.has(thing, Rock)) {
        println("Ouch, a rock!")
        app.commands.destroy(thing)
    }
}
```

An `Overlap` has two entities, `a` and `b`. Only the cart looks for anything, so one of them is always the cart. We find it, and `overlap.otherThan(cart)` hands us the other one: the thing that fell in.

`world.getOrNull(thing, Gem)` asks: "does this entity have a `Gem` sticker?" You get the `Gem`, or `null` if it doesn't. (`null` means "nothing here".)

## 3. Hook it up

In **`Main.kt`**, add the system **after** `fall`:

```kotlin {title="Main.kt"}
        addSystem(Phase.UPDATE) { app -> app.round.catchThings(app) }
```

Run it and catch some gems. Watch the Terminal count them!

{{< checkpoint "07-catch" >}}

{{< nerd "When exactly are collisions checked?" >}}
Once per frame, right after `FIXED_UPDATE` and before `UPDATE`. So any system in `UPDATE` sees that frame's `Overlap` events. Events only last one frame, then they're gone. klyph sorts colliders into a grid first, so it only has to compare things that are near each other, even with thousands of them.
{{< /nerd >}}

{{< try >}}
Make a gem worth more: spawn some gems with `Gem(points = 5)`. Maybe only the topaz ones?
{{< /try >}}
