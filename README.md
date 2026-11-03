# klyph-web

The learning site for [klyph](https://github.com/inemtsev/klyph), a 2D game engine for Kotlin.
It's written for younger readers: short pages, lots of pictures, one idea at a time, and a
mascot (Kip) who shows up with tips. It's a static site built with [Hugo](https://gohugo.io).

The site has three parts:

- **The Quest** (`content/quest/`): 11 steps that build *Gem Miner*, a catch-the-gems game.
- **Big Ideas** (`content/ideas/`): the nine klyph concepts: App, frame loop and phases,
  systems, entities and components, commands, scenes, resources, events, assets.
- **Game Words** (`content/words/`): a glossary of general game dev words (sprite, camera,
  particles…). Every page links into it.

There's also a cheat sheet and a "Kotlin in 5 minutes" page.

## Preview it

Install Hugo 0.158 or newer (the standard edition is enough):

- macOS: `brew install hugo`
- Windows: `winget install Hugo.Hugo`
- Linux: your package manager, or a release from [GitHub](https://github.com/gohugoio/hugo/releases)

Then, from this folder:

```bash
hugo server
```

and open <http://localhost:1313/klyph-web/>.

## Publish it

`.github/workflows/pages.yml` builds the site on every push and pull request and publishes `main`
to GitHub Pages. Turn it on once in the repository's **Settings → Pages → Build and deployment →
Source: GitHub Actions**. The site then lives at <https://inemtsev.github.io/klyph-web/>.

For a custom domain, change `baseURL` in `hugo.toml` and add the domain in the same settings page.

The build runs with `--panicOnWarning`, so a deprecated setting or a link to a missing glossary page
fails it.

## Layout

| Path | What's there |
|---|---|
| `hugo.toml` | Site settings and the main menu. |
| `content/` | Every page, in Markdown. |
| `layouts/` | The theme: page templates, partials and shortcodes. No external theme. |
| `static/css/site.css` | All styles. Color tokens at the top, dark mode below them. |
| `static/css/code.css` | Syntax colors, made with `hugo gen chromastyles --style=dracula`. |
| `static/js/site.js` | Theme toggle, XP and checkmarks, tabs, copy buttons. No libraries. |
| `static/img/` | Mascot poses, concept pictures, glossary icons, the hero. See [`art/README.md`](art/README.md). |
| `static/fonts/` | Self-hosted fonts (Jersey 10, Lexend, JetBrains Mono, all SIL Open Font License), so the site makes no third-party requests. |
| `starter/` | The finished Gem Miner as a klyph Gradle project, with checkpoints. See [`starter/README.md`](starter/README.md). |

## Writing a page

Front matter fields the theme understands:

```yaml
---
title: "Make it rain gems"
linkTitle: "Make it rain"     # shorter name for the sidebar and pager (optional)
step: 6                        # Quest only: the step number
weight: 6                      # order in the sidebar
time: "12 min"                 # shown as a chip
summary: "One sentence."       # the "In one line" box, the card text and the hover tip
learn:                         # the "You'll learn" box (optional)
  - "How to destroy entities"
image: "img/words/tint.png"    # the picture top right
imageAlt: "A paint bucket"     # its alt text
xp: 100                        # override the XP (Quest 50, Big Ideas 20, Game Words 5)
term: "sprite sheet"           # Game Words only: the default link text for {{< term >}}
---
```

Shortcodes:

| Shortcode | What it makes |
|---|---|
| `{{< term "sprite" >}}`, `{{< term "sprite" "sprites" >}}` | A dotted link to a Game Words page, with a hover tip. A typo fails the build. |
| `{{< idea "scenes" >}}`, `{{< idea "scenes" "a scene" >}}` | The same for a Big Ideas page. |
| `{{< kip tip >}}…{{< /kip >}}` | A callout from Kip. Kinds: `tip`, `oops`, `yay`, `think`, `hi`, `code`. A second argument replaces the heading. |
| `{{< nerd "Why?" >}}…{{< /nerd >}}` | An optional deep-dive, closed by default. |
| `{{< try >}}…{{< /try >}}` | A small optional challenge. |
| `{{< tabs >}}{{< tab "Windows" >}}…{{< /tab >}}{{< /tabs >}}` | Tabs. The reader's choice is remembered across the site. |
| `{{< phases "UPDATE" >}}` | The frame loop diagram, with phases lit up (comma separated). |
| `{{< scenestack "pause" "play" >}}` | The scene stack diagram, top scene first. |
| `{{< checkpoint "05-move-it" >}}` | A link to a folder in `starter/checkpoints/`. |
| `{{< keys "A" "D" >}}` | Keyboard keys. |

Code fences can take a file name and highlighted lines: ` ```kotlin {title="Main.kt" hl_lines="4 7-9"} `.

House style, to keep it friendly for younger readers and for readers with ADHD:

- One idea per section. Short sentences. Bold the key word.
- Show the code, run it, then explain it. Every Quest step ends with the game doing something new.
- Push the details into `{{< nerd >}}` boxes so nobody *has* to read them.
- Link every general game word to Game Words the first time it appears on a page.
- No references to other games by name and none of their art: keep the blocky, crafty vibe original.
- Code on the pages must match the code in `starter/`. If you change one, change the other.
