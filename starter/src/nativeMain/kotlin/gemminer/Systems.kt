package gemminer

import klyph.app.App
import klyph.app.deltaSeconds
import klyph.collision.Overlap
import klyph.collision.otherThan
import klyph.entity.Entity
import klyph.entity.Transform
import klyph.input.axis
import klyph.platform.Scancode
import klyph.render.DrawSpace
import klyph.render.Sprite
import klyph.render.lookAt
import klyph.text.TextAlign
import klyph.text.TextShadow
import kotlin.math.min
import kotlin.random.Random

// Systems are plain functions that get the App. Scenes.kt and Main.kt decide when they run.

/** Zooms the camera so the whole 800 x 600 world fits in the window, whatever its size. */
fun fitCamera(app: App) {
    val camera = app.camera
    camera.zoom = min(camera.viewportWidth / Tuning.WORLD_WIDTH, camera.viewportHeight / Tuning.WORLD_HEIGHT)
    camera.lookAt(Tuning.WORLD_WIDTH / 2, Tuning.WORLD_HEIGHT / 2)
}

/** Draws the cave wall picture behind everything, in the world, so it zooms with the camera. */
fun drawCaveWall(app: App) {
    app.canvas.drawSprite(
        app.art.wall,
        x = Tuning.WORLD_WIDTH / 2,
        y = Tuning.WORLD_HEIGHT / 2,
        width = Tuning.WORLD_WIDTH,
        height = Tuning.WORLD_HEIGHT,
        layer = Tuning.LAYER_WALL,
        space = DrawSpace.WORLD,
    )
}

/** Slides the cart with A / D or the arrow keys, and keeps it on the screen. */
fun moveCart(app: App) {
    val keys = app.input.keyboard
    val direction = (keys.axis(Scancode.LEFT, Scancode.RIGHT) + keys.axis(Scancode.A, Scancode.D)).coerceIn(-1f, 1f)
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
        val where = world.get(thing, Transform)
        app.commands.spawnSparkles(app.art, where.x, where.y, world.get(thing, Sprite).color)
        app.audio.play(app.sounds.ding, pitch = 0.9f + Random.nextFloat() * 0.3f)
        app.commands.destroy(thing)
    } else if (world.has(thing, Rock)) {
        lives -= 1
        app.audio.play(app.sounds.bonk)
        app.commands.destroy(thing)
    }
}

/** Draws the score and the lives in the top corners of the window, with a shadow so they stand out. */
fun Round.drawHud(app: App) {
    val font = app.assets.defaultFont
    app.canvas.drawText(font, "GEMS $gems", x = 16f, y = 16f, scale = 3f, shadow = TextShadow())
    app.canvas.drawText(
        font,
        "LIVES $lives",
        x = app.window.width - 16f,
        y = 16f,
        scale = 3f,
        align = TextAlign.RIGHT,
        shadow = TextShadow(),
    )
}

/** Draws [text] across the middle of the window, [y] points from the top. */
fun drawCentered(app: App, text: String, y: Float, scale: Float) {
    app.canvas.drawText(
        app.assets.defaultFont,
        text,
        x = app.window.width / 2f,
        y = y,
        scale = scale,
        align = TextAlign.CENTER,
        shadow = TextShadow(),
    )
}
