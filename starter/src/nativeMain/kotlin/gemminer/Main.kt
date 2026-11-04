package gemminer

import klyph.app.AppConfig
import klyph.app.Phase
import klyph.app.runApp

/**
 * Gem Miner: slide your minecart left and right, catch the falling gems and dodge the rocks.
 *
 * A / D or the arrow keys move the cart. P or Escape pauses. F3 shows klyph's debug overlay.
 * `./gradlew run -Pklyph.screenshot=120:shot.png` saves frame 120 as a picture.
 *
 * Every screen of the game is a scene (see Scenes.kt). Only the things that run on every screen
 * are added here: loading the art and sounds, fitting the camera, and drawing the cave wall.
 */
fun main() {
    // fromEnvironment lets settings such as -Pklyph.screenshot=120:shot.png reach the game.
    val config = AppConfig.fromEnvironment(
        AppConfig(
            title = "Gem Miner",
            width = 800,
            height = 600,
            clearColor = Tuning.CAVE_COLOR,
        ),
    )

    runApp(config) {
        addSystem(Phase.STARTUP) { app ->
            app.resources.insert(Art.load(app.assets))
            app.resources.insert(Sounds.load(app.assets))
            app.audio.music.play(app.sounds.music)
            app.scenes.replace(GameScenes.title)
        }

        addSystem(Phase.LATE_UPDATE) { app -> fitCamera(app) }

        addSystem(Phase.RENDER) { app -> drawCaveWall(app) }
    }
}
