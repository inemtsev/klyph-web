package gemminer

import klyph.app.Phase
import klyph.platform.Scancode
import klyph.scene.Scene
import klyph.scene.buildScene

/**
 * The four screens of the game. Only the scene on top of the stack runs its systems.
 *
 * - title: SPACE replaces it with play. ESCAPE quits.
 * - play: a round. P or ESCAPE pushes pause on top. Losing your last life pushes gameOver.
 * - pause: sits on top of the frozen round. P or ESCAPE pops it, and the round carries on.
 * - gameOver: sits on top of the frozen round. SPACE replaces everything with a new play.
 *
 * Entities a scene spawns belong to it and vanish when it leaves the stack, so a new round
 * never has old gems lying around.
 */
object GameScenes {
    val title: Scene = buildScene("title") {
        addSystem(Phase.INPUT) { app ->
            val keys = app.input.keyboard
            if (keys.isJustPressed(Scancode.SPACE)) app.scenes.replace(play)
            if (keys.isJustPressed(Scancode.ESCAPE)) app.quit()
        }
        addSystem(Phase.RENDER) { app ->
            drawCentered(app, "GEM MINER", y = 180f, scale = 8f)
            drawCentered(app, "A/D or arrows to move", y = 320f, scale = 3f)
            drawCentered(app, "Press SPACE to start", y = 380f, scale = 3f)
        }
    }

    val play: Scene = buildScene("play") {
        onEnter { app ->
            app.resources.insert(Round())
            app.commands.spawnCart(app.art)
            app.audio.play(app.sounds.start)
        }

        addSystem(Phase.INPUT) { app ->
            val keys = app.input.keyboard
            if (keys.isJustPressed(Scancode.P) || keys.isJustPressed(Scancode.ESCAPE)) app.scenes.push(pause)
        }

        addSystem(Phase.UPDATE) { app -> moveCart(app) }
        addSystem(Phase.UPDATE) { app -> app.round.dropThings(app) }
        addSystem(Phase.UPDATE) { app -> fall(app) }
        addSystem(Phase.UPDATE) { app -> app.round.catchThings(app) }
        addSystem(Phase.UPDATE) { app -> expire(app) }
        addSystem(Phase.UPDATE) { app ->
            if (app.round.lives <= 0) app.scenes.push(gameOver)
        }

        addSystem(Phase.RENDER) { app -> app.round.drawHud(app) }
    }

    val pause: Scene = buildScene("pause") {
        onEnter { app -> app.audio.music.volume = 0.3f }
        onExit { app -> app.audio.music.volume = 1f }
        addSystem(Phase.INPUT) { app ->
            val keys = app.input.keyboard
            if (keys.isJustPressed(Scancode.P) || keys.isJustPressed(Scancode.ESCAPE)) app.scenes.pop()
        }
        addSystem(Phase.RENDER) { app ->
            drawCentered(app, "PAUSED", y = 240f, scale = 6f)
            drawCentered(app, "P to keep mining", y = 330f, scale = 3f)
        }
    }

    val gameOver: Scene = buildScene("gameOver") {
        onEnter { app -> app.audio.play(app.sounds.gameOver) }
        addSystem(Phase.INPUT) { app ->
            if (app.input.keyboard.isJustPressed(Scancode.SPACE)) app.scenes.replace(play)
        }
        addSystem(Phase.RENDER) { app ->
            drawCentered(app, "GAME OVER", y = 200f, scale = 7f)
            drawCentered(app, "You mined ${app.round.gems} gems!", y = 300f, scale = 3f)
            drawCentered(app, "Press SPACE to mine again", y = 360f, scale = 3f)
        }
    }
}
