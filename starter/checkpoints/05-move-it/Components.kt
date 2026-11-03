package gemminer

import klyph.entity.ComponentType

// Components are plain data you stick onto entities. Each one has a companion object that
// klyph uses as its key, so you can write world.get(entity, Cart).

/** The player's minecart. */
class Cart(val speed: Float) {
    companion object : ComponentType<Cart>("Cart")
}
