package gemminer

import klyph.app.App
import klyph.app.deltaSeconds
import klyph.entity.Transform
import klyph.input.axis
import klyph.platform.Scancode
import klyph.render.lookAt
import kotlin.math.min

/** Zooms the camera so the whole 800 x 600 world fits in the window, whatever its size. */
fun fitCamera(app: App) {
    val camera = app.camera
    camera.zoom = min(camera.viewportWidth / Tuning.WORLD_WIDTH, camera.viewportHeight / Tuning.WORLD_HEIGHT)
    camera.lookAt(Tuning.WORLD_WIDTH / 2, Tuning.WORLD_HEIGHT / 2)
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
