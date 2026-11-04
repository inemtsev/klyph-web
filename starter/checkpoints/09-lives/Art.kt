package gemminer

import klyph.app.App
import klyph.app.Assets
import klyph.audio.MusicTrack
import klyph.audio.Sound
import klyph.render.TextureRegion
import klyph.render.asRegion

/** Every picture the game draws, loaded once at startup from the assets/ folder. */
class Art(
    val cart: TextureRegion,
    val gem: TextureRegion,
    val rock: TextureRegion,
    val sparkle: TextureRegion,
    val wall: TextureRegion,
) {
    companion object {
        fun load(assets: Assets): Art = Art(
            cart = assets.texture("cart.png").asRegion(),
            gem = assets.texture("gem.png").asRegion(),
            rock = assets.texture("rock.png").asRegion(),
            sparkle = assets.texture("sparkle.png").asRegion(),
            wall = assets.texture("wall.png").asRegion(),
        )
    }
}

/** Every sound the game plays, loaded once at startup. */
class Sounds(
    val ding: Sound,
    val bonk: Sound,
    val start: Sound,
    val gameOver: Sound,
    val music: MusicTrack,
) {
    companion object {
        fun load(assets: Assets): Sounds = Sounds(
            ding = assets.sound("ding.wav"),
            bonk = assets.sound("bonk.wav"),
            start = assets.sound("start.wav"),
            gameOver = assets.sound("gameover.wav"),
            music = assets.musicTrack("music.ogg"),
        )
    }
}

/** Shortcuts, so a system can write `app.art` instead of `app.resources.get<Art>()`. */
val App.art: Art get() = resources.get()
val App.sounds: Sounds get() = resources.get()
