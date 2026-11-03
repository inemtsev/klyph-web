---
title: "Ship it!"
step: 11
weight: 11
time: "10 min"
summary: "Zip your game up so friends can play it without installing anything, then pick your next adventure."
learn:
  - "How to make a game anyone can run"
  - "Where to go next"
image: "img/kip/cheer.png"
imageAlt: "Kip jumping for joy"
xp: 100
---

## 1. Build a release

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

## 2. Peek under the hood

While your game runs, press <kbd>F3</kbd>. That's klyph's **debug overlay**: {{< term "frame" "frames" >}} per second, how long each phase took, how many entities and sprites there are. Press it during a big sparkle burst and watch the numbers jump.

## 3. Level up your game

You know enough to make Gem Miner your own. Pick a challenge:

- 🏆 **Best score.** Keep it in a resource that survives between rounds, and show it on the title screen.
- 💎 **Golden gems.** A rare gem worth 5, with a bigger burst and a higher ding.
- 🎮 **Gamepad.** `app.input.gamepads` lists connected controllers. Use `gamepad.leftStick.x` to move. See {{< term "input" >}}.
- 📳 **Screen shake.** When a rock hits, wiggle `app.camera` for a moment.
- ⏩ **Getting harder.** Make `DROP_EVERY` shrink as the round goes on.
- 🧲 **Power-up.** A magnet that pulls nearby gems toward the cart for 5 seconds.

## 4. Where next?

- **[Big Ideas]({{< relref "/ideas" >}}).** Read how klyph works, one idea at a time. You've already used all nine!
- **[Game Words]({{< relref "/words" >}}).** Look up anything you're not sure about.
- **[The engine's own games](https://github.com/inemtsev/klyph).** The klyph repo has two finished games to read: `squish`, a bug-squishing game, and `shooter`, a space shooter with a following camera, animations and gamepad support.

{{< kip yay "You finished the Quest!" >}}
From an empty window to a real game you can share. That's a big deal. Press the button for a **bonus 100 XP**. You earned it, Legend.
{{< /kip >}}
