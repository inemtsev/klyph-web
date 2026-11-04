---
title: "Ship it!"
step: 11
weight: 11
time: "10 min"
summary: "Take a screenshot to show off, zip your game up so friends can play it without installing anything, then pick your next adventure."
learn:
  - "How to pass settings to your game from the command line"
  - "How to make a game anyone can run"
  - "Where to go next"
image: "img/kip/cheer.png"
imageAlt: "Kip jumping for joy"
xp: 100
---

## 1. Take a screenshot

First, a picture to show off. In **`Main.kt`**, wrap your settings in `AppConfig.fromEnvironment(...)`:

```kotlin {title="Main.kt" hl_lines="1 2 9"}
    // fromEnvironment lets settings such as -Pklyph.screenshot=120:shot.png reach the game.
    val config = AppConfig.fromEnvironment(
        AppConfig(
            title = "Gem Miner",
            width = 800,
            height = 600,
            clearColor = Tuning.CAVE_COLOR,
        ),
    )
```

That one line lets you hand your game settings from the Terminal, without changing the code. Try it:

```bash
./gradlew run -Pklyph.screenshot=120:shot.png
```

(On Windows: `.\gradlew run -Pklyph.screenshot=120:shot.png`.) Press <kbd>Space</kbd> quickly. klyph saves the 120th {{< term "frame" >}}, about two seconds in, as `shot.png` in your project folder.

{{< nerd "What else can it do?" >}}
`-Pklyph.frames=600` quits after 600 frames, `-Pklyph.fullscreen=1` starts in fullscreen and `-Pklyph.overlay=1` opens the debug overlay straight away. The official [Getting started guide](https://inemtsev.github.io/klyph/guide/getting-started.html) lists them all.
{{< /nerd >}}

## 2. Build a release

In the Terminal:

```bash
./gradlew distribute
```

(On Windows: `.\gradlew distribute`.) Gradle builds a fast **release** version of your game and zips it into `build/distributions/`. Inside the zip:

- your game program,
- the `assets/` folder,
- the SDL3 library it needs.

Send the zip to a friend on the same kind of computer. They unzip it and double-click. No Java, no Kotlin, no install. 🎉

{{< kip oops "Same kind of computer" >}}
A Windows build runs on Windows, a Mac build on Macs. Each computer type needs its own build. A Mac's security check may also ask your friend to allow the app in **System Settings → Privacy & Security**, because it isn't signed by Apple.
{{< /kip >}}

## 3. Peek under the hood

While your game runs, press <kbd>F3</kbd>. That's klyph's **debug overlay**: {{< term "frame" "frames" >}} per second, how long each phase took, how many entities and sprites there are. Press it during a big sparkle burst and watch the numbers jump.

## 4. Level up your game

You know enough to make Gem Miner your own. Pick a challenge:

- 🏆 **Best score.** Keep it in a resource that survives between rounds, and show it on the title screen.
- 💎 **Golden gems.** A rare gem worth 5, with a bigger burst and a higher ding.
- 🖥️ **Fullscreen.** Make <kbd>F11</kbd> switch `app.window.mode` between `WindowMode.WINDOWED` and `WindowMode.FULLSCREEN` (`import klyph.platform.WindowMode`).
- 🎮 **Gamepad.** `app.input.gamepads` lists connected controllers. Use `gamepad.leftStick.x` to move. See {{< term "input" >}}.
- 📳 **Screen shake.** When a rock hits, wiggle `app.camera` for a moment.
- ⏩ **Getting harder.** Make `DROP_EVERY` shrink as the round goes on.
- 🧲 **Power-up.** A magnet that pulls nearby gems toward the cart for 5 seconds.

## 5. Where next?

- **[Big Ideas]({{< relref "/ideas" >}}).** Read how klyph works, one idea at a time. You've already used all nine!
- **[Game Words]({{< relref "/words" >}}).** Look up anything you're not sure about.
- **[Start your own game](https://github.com/inemtsev/klyph-template).** The klyph template is a ready-made project with a ship that flies. Press **Use this template** on GitHub and build something new.
- **[The official klyph guide](https://inemtsev.github.io/klyph/guide/index.html)** and the **[API reference](https://inemtsev.github.io/klyph/)**. Grown-up docs with every detail, for when you want to go deeper.
- **[The engine's own games](https://github.com/inemtsev/klyph/tree/main/games).** Two finished games to read: `squish`, a bug-squishing game, and `shooter`, a space shooter with waves, a boss, a following camera and gamepad support.

{{< kip yay "You finished the Quest!" >}}
From an empty window to a real game you can share. That's a big deal. Press the button for a **bonus 100 XP**. You earned it, Legend.
{{< /kip >}}
