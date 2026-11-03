# Site art

Every picture in `static/img/` was generated with xAI Grok Imagine on 2026-11-03 through the
grok-image skill: 46 generations in all, about $1.12. Sprites used the skill's sprite mode,
which asks for a flat magenta background, keys it out and trims the result. Images were then
scaled down and reduced to 128 colors with Pillow. Nothing else was edited.

## Kip, the mascot (`static/img/kip/`)

`grok-imagine-image-2.0`, sprite mode. The first prompt made three candidates; the second one
became `wave.png`. The other poses are edits of that image (`/v1/images/edits`), so Kip stays
the same character.

Base prompt: "Chunky 32-bit pixel art video game mascot: Kip, a small friendly cube-shaped
creature made of a single glowing teal-and-mint block, with a big cute face on the front (two
large shiny black eyes, small happy smile, pink cheeks), tiny stubby arms and feet, a glowing
yellow letter k carved on its forehead like a rune, a little sprout of grass on top of its head.
Waving hello with one hand. Bold dark outline, bright cheerful colors, crisp visible pixels, video
game sprite."

Every edit ended with: "Keep exactly the same character design: same teal-mint cube body, same
face, same glowing yellow letter k on the forehead, same green leaf sprout on top, same chunky
pixel art style and bold dark outline, same flat magenta background."

| File | Edit prompt |
|---|---|
| `think.png` | Change the pose: the character is thinking, one hand on its chin, eyes looking up, a small thought bubble with three dots above it. |
| `cheer.png` | Change the pose: the character is jumping for joy with both arms raised high, a huge open smile, eyes closed happily, a few small yellow pixel sparkles around it. |
| `oops.png` | Change the pose: the character looks worried and a bit embarrassed, both hands up near its face, a single blue sweat drop on its side, wobbly smile. |
| `point.png` | Change the pose: the character is pointing to the right with one arm stretched out, confident smile, one eye winking. |
| `laptop.png` | Change the pose: the character is sitting and happily typing on a tiny grey pixel laptop in front of it, focused cheerful face. |

## Hero (`static/img/hero.jpg`, `hero.png`)

`grok-imagine-image-2.0`, 16:9, two candidates, the second kept: "Chunky 32-bit pixel art video
game title illustration, wide banner: inside a cosy underground mine cave lit by warm torches,
colorful gems (red, green, blue, yellow, purple) falling from above, a wooden minecart on rails at
the bottom catching them, sparkles everywhere. On the left, a small cute teal-mint cube creature
with big shiny eyes, pink cheeks, a glowing yellow letter k on its forehead and a green leaf sprout
on top, waving. Bright cheerful palette, crisp pixels, no text, no logos."

## Sneak peek (`static/img/gem-miner.png`)

Not generated: it's laid out from the starter's own assets and numbers (`starter/assets`,
`Tuning.kt`), with the HUD drawn in klyph's built-in 8×8 font.

## Big Ideas and Game Words (`static/img/ideas/`, `static/img/words/`)

`grok-imagine-image`, sprite mode, one generation each. Every prompt ended with: "Chunky 32-bit
pixel art icon, crisp visible square pixels, bold dark outline, bright cheerful palette, cute
video game item."

| File | Prompt |
|---|---|
| `ideas/app.png` | A chunky retro arcade cabinet game machine with a glowing screen showing a tiny cave with gems, colorful buttons and joystick, little gears peeking out of an open side panel. |
| `ideas/loop.png` | A looping conveyor belt shaped like a circle with small colorful boxes riding around it, a big golden clock gear in the middle. |
| `ideas/systems.png` | A cute little boxy robot worker with a wrench in one hand and a tiny oil can in the other, busy and cheerful, small antenna on its head. |
| `ideas/entities.png` | A plain grey stone cube in the middle with three bright colorful round stickers being stuck onto it: one with a picture frame, one with an arrow, one with a heart. |
| `ideas/commands.png` | A wooden clipboard holding a paper checklist with three check boxes, a yellow pencil next to it. |
| `ideas/scenes.png` | Three thin floating island platforms stacked vertically one above another with gaps between them: the top one has snow, the middle one is a dark cave with gems, the bottom one is green grass with a tree. |
| `ideas/resources.png` | An open wooden treasure chest with gold trim, holding a rolled scroll, a small coin pouch and a folded map. |
| `ideas/events.png` | A cute red mailbox with its flag up and three white envelopes flying out of it. |
| `ideas/assets.png` | A wooden crate full of creative supplies: a paint palette, a paintbrush, a small speaker and a music note sticking out of it. |
| `words/engine.png` | A cute chunky game engine machine with pistons, pipes, gears and a little chimney puffing a small cloud, painted teal and orange. |
| `words/sprite.png` | A small hero character picture printed on a floating paper card with a dashed cut-out border, like a sticker. |
| `words/texture.png` | A painter's canvas on a small easel showing a colorful landscape painting. |
| `words/spritesheet.png` | A sheet of paper showing a neat grid of four animation frames of a little green slime bouncing. |
| `words/camera.png` | A cute retro handheld video camera with a big round lens and a red record light. |
| `words/particles.png` | A burst of tiny colorful sparkles, stars and confetti squares exploding outward from a center point. |
| `words/collider.png` | A blue gem surrounded by a glowing bright green dashed outline box, like a hitbox. |
| `words/loop.png` | A circular arrow loop made of glowing blue blocks going round and round. |
| `words/frame.png` | A short strip of old movie film with three frames, each frame showing a ball at a slightly different height. |
| `words/delta.png` | An hourglass with a wooden frame and glowing golden sand flowing down. |
| `words/transform.png` | A purple block with four arrows pointing up, down, left and right out of it and a curved rotate arrow around it. |
| `words/coordinates.png` | A small treasure map on a square grid with a red X mark and a dotted path. |
| `words/layers.png` | Three stacked floating translucent sheets, the bottom one a dark cave, the middle one with a minecart, the top one with sparkles. |
| `words/pixelart.png` | A big red heart made of chunky square pixels, with a white shine pixel. |
| `words/input.png` | A cute game controller next to a single keyboard key and a computer mouse. |
| `words/audio.png` | A cute speaker box with music notes floating out of it. |
| `words/hud.png` | A row of three red hearts next to a shiny gold coin. |
| `words/tint.png` | A paint bucket tipping over with splashes of red, green and blue paint. |
| `words/fixed.png` | A wooden metronome with its pendulum swinging. |
