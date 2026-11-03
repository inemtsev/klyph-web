package gemminer

import klyph.collision.Collider
import klyph.collision.CollisionLayers
import klyph.collision.Shape
import klyph.entity.Commands
import klyph.entity.Transform
import klyph.particle.ParticleEmitter
import klyph.platform.Color
import klyph.render.Sprite

// One function per kind of entity. They go through Commands, so they are safe to call from
// inside a system that is looping over the world: the new entities appear after it finishes.

/** The minecart, at the bottom of the screen in the middle. */
fun Commands.spawnCart(art: Art) {
    spawn {
        add(Transform, Transform(Tuning.WORLD_WIDTH / 2, Tuning.CART_Y))
        add(Sprite, Sprite(art.cart, Tuning.CART_WIDTH, Tuning.CART_HEIGHT, layer = Tuning.LAYER_THINGS))
        // The cart looks for falling things, so the engine tells us when one touches it.
        add(Collider, Collider(Shape.Box(96f, 40f), layers = Layers.CART, mask = Layers.FALLING))
        add(Cart, Cart(Tuning.CART_SPEED))
    }
}

/** A gem of [color] just above the top of the screen at [x], falling at [speed]. */
fun Commands.spawnGem(art: Art, x: Float, speed: Float, color: Color) {
    spawn {
        add(Transform, Transform(x, -Tuning.GEM_SIZE))
        add(Sprite, Sprite(art.gem, Tuning.GEM_SIZE, Tuning.GEM_SIZE, color = color, layer = Tuning.LAYER_THINGS))
        add(Collider, Collider(Shape.Circle(18f), layers = Layers.FALLING, mask = CollisionLayers.NONE))
        add(Falling, Falling(speed))
        add(Gem, Gem(points = 1))
    }
}

/** A rock just above the top of the screen at [x], falling at [speed]. */
fun Commands.spawnRock(art: Art, x: Float, speed: Float) {
    spawn {
        add(Transform, Transform(x, -Tuning.ROCK_SIZE))
        add(Sprite, Sprite(art.rock, Tuning.ROCK_SIZE, Tuning.ROCK_SIZE, layer = Tuning.LAYER_THINGS))
        add(Collider, Collider(Shape.Circle(24f), layers = Layers.FALLING, mask = CollisionLayers.NONE))
        add(Falling, Falling(speed))
        add(Rock, Rock())
    }
}

/** A burst of sparkles of [color] at ([x], [y]). The entity removes itself after a second. */
fun Commands.spawnSparkles(art: Art, x: Float, y: Float, color: Color) {
    spawn {
        val burst = ParticleEmitter(art.sparkle, maxParticles = 24).apply {
            speed = 80f..240f
            lifetime = 0.3f..0.7f
            gravityY = 400f
            startColor = color
            endColor = Color(color.red, color.green, color.blue, alpha = 0f)
            startScale = 1f
            endScale = 0.2f
            layer = Tuning.LAYER_SPARKLES
            emit(24)
        }
        add(Transform, Transform(x, y))
        add(ParticleEmitter, burst)
        add(Lifetime, Lifetime(1f))
    }
}
