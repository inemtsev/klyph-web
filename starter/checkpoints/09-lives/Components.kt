package gemminer

import klyph.app.App
import klyph.collision.CollisionLayers
import klyph.entity.ComponentType

// Components are plain data you stick onto entities. Each one has a companion object that
// klyph uses as its key, so you can write world.get(entity, Cart).

/** The player's minecart. */
class Cart(val speed: Float) {
    companion object : ComponentType<Cart>("Cart")
}

/** Anything falling down the mine shaft, at [speed] units per second. */
class Falling(var speed: Float) {
    companion object : ComponentType<Falling>("Falling")
}

/** A gem. Catch it for [points]. */
class Gem(val points: Int) {
    companion object : ComponentType<Gem>("Gem")
}

/** A rock. Catch it and you lose a life. */
class Rock {
    companion object : ComponentType<Rock>("Rock")
}

/** Destroys its entity once [seconds] run out. Used for sparkle bursts. */
class Lifetime(var seconds: Float) {
    companion object : ComponentType<Lifetime>("Lifetime")
}

/** Collision layers: the cart looks for falling things, and nothing else. */
object Layers {
    val CART: CollisionLayers = CollisionLayers.of(1)
    val FALLING: CollisionLayers = CollisionLayers.of(2)
}

/**
 * The state of one round: score, lives and timers. It is a resource, shared by every system.
 */
class Round {
    var gems = 0
    var lives = Tuning.STARTING_LIVES
    var elapsed = 0f
    var dropTimer = Tuning.FIRST_DROP_DELAY
}

val App.round: Round get() = resources.get()
