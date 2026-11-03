---
title: "Kotlin in 5 minutes"
summary: "Just enough Kotlin to follow the Quest. If you've written a bit of Java, Python or JavaScript, you'll be fine."
time: "5 min"
image: "img/kip/laptop.png"
imageAlt: "Kip typing on a tiny laptop"
---

## Values and variables

```kotlin
val name = "Kip"   // val: can't change. Use it most of the time.
var gems = 0       // var: can change.
gems += 1

val speed = 420f   // the f makes it a Float (a number with a decimal point)
val lives = 3      // an Int (a whole number)
```

Kotlin works out the type for you. You *can* write it, too: `val speed: Float = 420f`.

## Functions

```kotlin
fun add(a: Int, b: Int): Int {
    return a + b
}

fun double(x: Float) = x * 2f   // short version for one-liners
```

Arguments can be given **by name**, which klyph uses a lot because it's easier to read:

```kotlin
Transform(x = 400f, y = 530f)
```

## Classes

```kotlin
class Cart(val speed: Float) {
    var wheelsSpinning = false
}

val cart = Cart(speed = 420f)
println(cart.speed)
```

An `object` is a class with exactly one copy, made for you: `object Tuning { const val WORLD_WIDTH = 800f }`, then `Tuning.WORLD_WIDTH`.

A `companion object` lives inside a class. klyph components use one as their key:

```kotlin
class Cart(val speed: Float) {
    companion object : ComponentType<Cart>("Cart")
}
```

## if, when, loops

```kotlin
if (lives <= 0) println("Game over") else println("Keep going!")

for (i in 0 until 3) println(i)           // 0, 1, 2

val label = when (lives) {
    3 -> "Perfect"
    0 -> "Ouch"
    else -> "OK"
}
```

## Lambdas: code you pass around

A lambda is a little function in `{ }`. klyph's systems are lambdas:

```kotlin
addSystem(Phase.UPDATE) { app -> moveCart(app) }
```

`app ->` names what the lambda receives. Read it as "given the app, move the cart".

## null: "nothing here"

A type with `?` can be `null`:

```kotlin
val gem: Gem? = world.getOrNull(entity, Gem)
if (gem != null) score += gem.points      // safe: Kotlin knows it's not null here
val points = gem?.points ?: 0             // ?. means "if not null", ?: means "otherwise"
```

## Extension functions

You can add functions to classes you didn't write. Gem Miner does it a lot:

```kotlin
fun Commands.spawnCart(art: Art) { /* ... */ }   // now app.commands.spawnCart(art) works
val App.round: Round get() = resources.get()      // now app.round works
```

## Strings

```kotlin
val text = "GEMS $gems"                     // $ puts a value in a string
val more = "You mined ${round.gems} gems!"  // ${ } for anything longer
```

That's all the Kotlin the Quest uses! Want more? The official [Kotlin tour](https://kotlinlang.org/docs/kotlin-tour-welcome.html) is short and friendly.

<p><a class="btn btn--big" href="{{< relref "/quest" >}}">▶ Start the Quest</a></p>
