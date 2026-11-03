# Gem Miner assets

The klyph plugin leaves this file out of the game's `assets/` folder (its default `assetExcludes`).

## Pictures

Generated with xAI Grok Imagine (`grok-imagine-image`) on 2026-11-03 through the grok-image skill.
Sprites used its sprite mode, which asks for a flat magenta background, keys it out and trims the
result. Each prompt ended with "Chunky 16-bit pixel art video game sprite, crisp visible square
pixels, bold dark outline, limited bright palette." Each was generated twice and the better one
kept, then scaled to twice its size in the game so it stays sharp on high-density screens.

| File | Size | Prompt |
|---|---|---|
| `cart.png` | 224 × 147 | Side view of a cute wooden mine cart on four small iron wheels, open top, brown wood planks with dark iron bands and rivets, empty, facing the viewer from the side. |
| `gem.png` | 96 × 93 | A single faceted cut gemstone, diamond shape, drawn entirely in white and very light grey shades with darker grey facet lines and one bright white shine highlight, no color, so it can be tinted later. |
| `rock.png` | 112 × 112 | A single lumpy grey boulder rock, round-ish, with darker grey cracks and a couple of small light highlights, chunky and heavy looking. |
| `wall.png` | 800 × 600 | Chunky 16-bit pixel art video game background, front view of a dark underground mine cave wall made of brown and dark-grey stone blocks in a grid, wooden support beams on the left and right edges, a few tiny glowing ore specks, a couple of small wall torches giving warm light, dark and low-contrast in the middle so that sprites on top stay readable, no characters, no text. (Not a sprite; reduced to 96 colors.) |

`sparkle.png` (16 × 16) is drawn by hand in code: a white four-point star.

## Sounds

`ding.wav`, `bonk.wav`, `start.wav`, `gameover.wav` and `music.ogg` are synthesised by
`tools/synth.py` (Python 3, standard library; ffmpeg for the Ogg Vorbis music). Run it from the
starter folder to make them again:

```bash
python3 tools/synth.py
```
