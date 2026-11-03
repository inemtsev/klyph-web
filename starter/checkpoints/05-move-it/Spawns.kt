package gemminer

import klyph.entity.Commands
import klyph.entity.Transform
import klyph.render.Sprite

/** The minecart, at the bottom of the screen in the middle. */
fun Commands.spawnCart(art: Art) {
    spawn {
        add(Transform, Transform(Tuning.WORLD_WIDTH / 2, Tuning.CART_Y))
        add(Sprite, Sprite(art.cart, Tuning.CART_WIDTH, Tuning.CART_HEIGHT, layer = Tuning.LAYER_THINGS))
        add(Cart, Cart(Tuning.CART_SPEED))
    }
}
