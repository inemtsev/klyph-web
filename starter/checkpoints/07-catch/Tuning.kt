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
