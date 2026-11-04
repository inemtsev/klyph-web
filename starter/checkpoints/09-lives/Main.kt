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
            app.resources.insert(Sounds.load(app.assets))
            app.resources.insert(Round())
            app.commands.spawnCart(app.art)
        }

        addSystem(Phase.UPDATE) { app -> moveCart(app) }
        addSystem(Phase.UPDATE) { app -> app.round.dropThings(app) }
        addSystem(Phase.UPDATE) { app -> fall(app) }
        addSystem(Phase.UPDATE) { app -> app.round.catchThings(app) }
        addSystem(Phase.UPDATE) { app ->
            // For now, game over just closes the window. A real screen comes next!
            if (app.round.lives <= 0) app.quit()
        }
        addSystem(Phase.LATE_UPDATE) { app -> fitCamera(app) }

        addSystem(Phase.RENDER) { app -> drawCaveWall(app) }
        addSystem(Phase.RENDER) { app -> app.round.drawHud(app) }
    }
}
