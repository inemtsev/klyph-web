"""Rebuilds starter/checkpoints/ from the finished Gem Miner in starter/src.

Usage, from the repository root:

    python3 tools/make-checkpoints.py

The checkpoints are cut from the finished code, so they can never drift from it. CI runs this and
fails if the result differs from what is committed. When a Quest step changes what a checkpoint
holds, change the pieces below to match the step's page.
"""
import os

os.chdir(os.path.join(os.path.dirname(os.path.abspath(__file__)), '..', 'starter'))
F = 'src/nativeMain/kotlin/gemminer/'
read = lambda n: open(F + n).read()
art, comp, spawns, systems, tuning = read('Art.kt'), read('Components.kt'), read('Spawns.kt'), read('Systems.kt'), read('Tuning.kt')

def block(text, start, end=None):
    i = text.index(start)
    return text[i:text.index(end, i)] if end else text[i:]

def write(d, name, text):
    os.makedirs('checkpoints/' + d, exist_ok=True)
    open(f'checkpoints/{d}/{name}', 'w').write(text.rstrip('\n') + '\n')

MAIN_HEAD = '''package gemminer

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
'''
art_only = '''package gemminer

import klyph.app.App
import klyph.app.Assets
import klyph.render.TextureRegion
import klyph.render.asRegion

''' + block(art, '/** Every picture', '/** Every sound') + '''/** A shortcut, so a system can write `app.art` instead of `app.resources.get<Art>()`. */
val App.art: Art get() = resources.get()
'''
round_doc = ' * The play scene inserts a fresh one each time it starts, so every round starts from zero.\n'
assert round_doc in comp

# 05-move-it
d = '05-move-it'
write(d, 'Tuning.kt', tuning)
write(d, 'Art.kt', art_only)
write(d, 'Components.kt', '''package gemminer

import klyph.entity.ComponentType

// Components are plain data you stick onto entities. Each one has a companion object that
// klyph uses as its key, so you can write world.get(entity, Cart).

''' + block(comp, "/** The player's minecart. */", '/** Anything falling'))
write(d, 'Spawns.kt', '''package gemminer

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
''')
write(d, 'Systems.kt', '''package gemminer

import klyph.app.App
import klyph.app.deltaSeconds
import klyph.entity.Transform
import klyph.input.axis
import klyph.platform.Scancode
import klyph.render.lookAt
import kotlin.math.min

''' + block(systems, '/** Zooms the camera', '/** Draws the cave wall') + block(systems, '/** Slides the cart', '/** Counts the round'))
write(d, 'Main.kt', MAIN_HEAD + '''        addSystem(Phase.STARTUP) { app ->
            app.resources.insert(Art.load(app.assets))
            app.commands.spawnCart(app.art)
        }

        addSystem(Phase.UPDATE) { app -> moveCart(app) }
        addSystem(Phase.LATE_UPDATE) { app -> fitCamera(app) }
    }
}
''')

# 07-catch
d = '07-catch'
write(d, 'Tuning.kt', tuning)
write(d, 'Art.kt', art_only)
write(d, 'Components.kt', comp.replace(round_doc, ''))
write(d, 'Spawns.kt', block(spawns, 'package', '/** A burst of sparkles')
      .replace('import klyph.particle.EmitterEnd\n', '').replace('import klyph.particle.ParticleEmitter\n', '')
      .replace('import kotlin.time.Duration.Companion.milliseconds\n', ''))
catch07 = '''private fun Round.catchOne(app: App, thing: Entity) {
    val world = app.world
    val gem = world.getOrNull(thing, Gem)
    if (gem != null) {
        gems += gem.points
        println("Gems: $gems")
        app.commands.destroy(thing)
    } else if (world.has(thing, Rock)) {
        println("Ouch, a rock!")
        app.commands.destroy(thing)
    }
}
'''
write(d, 'Systems.kt', '''package gemminer

import klyph.app.App
import klyph.app.deltaSeconds
import klyph.collision.Overlap
import klyph.collision.otherThan
import klyph.entity.Entity
import klyph.entity.Transform
import klyph.input.axis
import klyph.platform.Scancode
import klyph.render.lookAt
import kotlin.math.min
import kotlin.random.Random

''' + block(systems, '/** Zooms the camera', '/** Draws the cave wall') + block(systems, '/** Slides the cart', 'private fun Round.catchOne') + catch07)
write(d, 'Main.kt', MAIN_HEAD + '''        addSystem(Phase.STARTUP) { app ->
            app.resources.insert(Art.load(app.assets))
            app.resources.insert(Round())
            app.commands.spawnCart(app.art)
        }

        addSystem(Phase.UPDATE) { app -> moveCart(app) }
        addSystem(Phase.UPDATE) { app -> app.round.dropThings(app) }
        addSystem(Phase.UPDATE) { app -> fall(app) }
        addSystem(Phase.UPDATE) { app -> app.round.catchThings(app) }
        addSystem(Phase.LATE_UPDATE) { app -> fitCamera(app) }
    }
}
''')

# 09-lives
d = '09-lives'
write(d, 'Tuning.kt', tuning)
write(d, 'Art.kt', art)
write(d, 'Components.kt', comp.replace(round_doc, ''))
write(d, 'Spawns.kt', spawns)
write(d, 'Systems.kt', systems[:systems.index('/** Draws [text] across the middle')])
write(d, 'Main.kt', MAIN_HEAD + '''        addSystem(Phase.STARTUP) { app ->
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
''')
print('Rebuilt starter/checkpoints/05-move-it, 07-catch and 09-lives.')
