package gemminer

import klyph.app.App
import klyph.app.deltaSeconds
import klyph.collision.Overlap
import klyph.entity.Entity
import klyph.entity.Transform
import klyph.input.axis
import klyph.platform.Scancode
import klyph.render.lookAt
import kotlin.math.min
import kotlin.random.Random

/** Zooms the camera so the whole 800 x 600 world fits in the window, whatever its size. */
fun fitCamera(app: App) {
    val camera = app.camera
    camera.zoom = min(camera.viewportWidth / Tuning.WORLD_WIDTH, camera.viewportHeight / Tuning.WORLD_HEIGHT)
    camera.lookAt(Tuning.WORLD_WIDTH / 2, Tuning.WORLD_HEIGHT / 2)
}

/** Slides the cart with A / D or the arrow keys, and keeps it on the screen. */
fun moveCart(app: App) {
    val direction = (app.input.axis(Scancode.LEFT, Scancode.RIGHT) + app.input.axis(Scancode.A, Scancode.D)).coerceIn(-1f, 1f)
    val dt = app.time.deltaSeconds
    val half = Tuning.CART_WIDTH / 2
    app.world.forEach(Cart, Transform) { _, cart, transform ->
        transform.x = (transform.x + direction * cart.speed * dt).coerceIn(half, Tuning.WORLD_WIDTH - half)
    }
}

/** Counts the round's time and, every so often, drops a gem or a rock from the top. */
fun Round.dropThings(app: App) {
    val dt = app.time.deltaSeconds
    elapsed += dt
    dropTimer -= dt
    if (dropTimer > 0f) return
    dropTimer += Tuning.DROP_EVERY

    val x = Random.nextFloat() * (Tuning.WORLD_WIDTH - 80f) + 40f
    val speed = Tuning.FALL_SPEED + elapsed * Tuning.FALL_SPEEDUP
    if (Random.nextFloat() < Tuning.ROCK_CHANCE) {
        app.commands.spawnRock(app.art, x, speed)
    } else {
        app.commands.spawnGem(app.art, x, speed, Tuning.GEM_COLORS.random())
    }
}

/** Moves everything that falls, and removes what drops off the bottom of the screen. */
fun fall(app: App) {
    val dt = app.time.deltaSeconds
    app.world.forEach(Falling, Transform) { entity, falling, transform ->
        transform.y += falling.speed * dt
        if (transform.y > Tuning.WORLD_HEIGHT + 64f) app.commands.destroy(entity)
    }
}

/** Reads the engine's Overlap events: a gem in the cart scores, a rock costs a life. */
fun Round.catchThings(app: App) {
    app.events.forEach<Overlap> { overlap ->
        // The engine does not promise which entity of a pair is a, so try both ways round.
        catchOne(app, overlap.a, overlap.b)
        catchOne(app, overlap.b, overlap.a)
    }
}

private fun Round.catchOne(app: App, cart: Entity, thing: Entity) {
    val world = app.world
    if (!world.has(cart, Cart)) return
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
