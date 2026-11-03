package gemminer

import klyph.app.AppConfig
import klyph.app.Phase
import klyph.app.runApp

fun main() {
    val config = AppConfig(
        title = "Gem Miner",
        width = 800,
        height = 600,
        clearColor = Tuning.CAVE_COLOR,
    )

    runApp(config) {
        addSystem(Phase.STARTUP) { app ->
            app.resources.insert(Art.load(app.assets))
            app.commands.spawnCart(app.art)
        }

        addSystem(Phase.UPDATE) { app -> moveCart(app) }
        addSystem(Phase.LATE_UPDATE) { app -> fitCamera(app) }
    }
}
