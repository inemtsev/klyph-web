---
title: "Gear up"
step: 1
weight: 1
time: "15 min"
summary: "Install Java and IntelliJ IDEA, download the starter kit, and play the finished Gem Miner."
learn:
  - "What you need installed to make klyph games"
  - "How to run a klyph game"
image: "img/kip/laptop.png"
imageAlt: "Kip typing on a tiny laptop"
---

## 1. Install the tools

You need two things:

- **A JDK** (Java Development Kit), version 17 or newer. The build tool, Gradle, runs on it. Your game itself won't need Java.
- **IntelliJ IDEA**, the editor made by the same people who made Kotlin. The free version is all you need.

{{< tabs >}}
{{< tab "Windows" >}}
1. Get the **Temurin JDK 21** installer from [adoptium.net](https://adoptium.net/) and run it. When it asks, switch on **"Set JAVA_HOME variable"**.
2. Get **IntelliJ IDEA** from [jetbrains.com/idea](https://www.jetbrains.com/idea/download/) and install it.

That's it. klyph downloads the graphics library it needs (SDL3) by itself.
{{< /tab >}}
{{< tab "macOS" >}}
klyph runs on Macs with an Apple chip (M1 or newer).

1. Get the **Temurin JDK 21** installer from [adoptium.net](https://adoptium.net/) and run it.
2. Get **IntelliJ IDEA** from [jetbrains.com/idea](https://www.jetbrains.com/idea/download/) and install it.
3. Optional: if you use [Homebrew](https://brew.sh), run `brew install sdl3`. Without it, klyph downloads SDL3 by itself.
{{< /tab >}}
{{< tab "Linux" >}}
1. Install a JDK and SDL3 3.4 or newer:
   - **Debian 13, Ubuntu 25.04 or newer:** `sudo apt install openjdk-21-jdk libsdl3-dev`
   - **Fedora:** `sudo dnf install java-21-openjdk-devel SDL3-devel`
   - **Arch:** `sudo pacman -S jdk21-openjdk sdl3`
2. Get **IntelliJ IDEA** from [jetbrains.com/idea](https://www.jetbrains.com/idea/download/) or your software center.

Older distributions have no SDL3 package yet. Then you build SDL3 from source and install it into `/usr/local`; the [SDL guide](https://github.com/libsdl-org/SDL/blob/main/docs/README-linux.md) explains how.
{{< /tab >}}
{{< /tabs >}}

## 2. Download the starter kit

1. Download [the klyph-web zip]({{< param "starterZip" >}}) and unzip it.
2. Inside is a folder called **`starter`**. That's your game. Copy it somewhere easy to find, like your Desktop, and rename it `gem-miner` if you like.
3. Open IntelliJ IDEA, choose **Open**, and pick that folder.

IntelliJ spots the Gradle build and sets everything up. Give it a minute.

Here's what's inside:

| What | What it's for |
|---|---|
| `build.gradle.kts` | The build file. Just three lines! |
| `assets/` | The pictures and sounds: cart, gems, rocks, music. |
| `src/nativeMain/kotlin/gemminer/` | The code. Right now it's the **finished** game. |
| `checkpoints/` | Saved code from partway through the Quest. |

## 3. Play the finished game

Open the **Terminal** at the bottom of IntelliJ and type:

{{< tabs >}}
{{< tab "Windows" >}}
```powershell
.\gradlew run
```
{{< /tab >}}
{{< tab "macOS" >}}
```bash
./gradlew run
```
{{< /tab >}}
{{< tab "Linux" >}}
```bash
./gradlew run
```
{{< /tab >}}
{{< /tabs >}}

{{< kip oops "The first run is slow" >}}
The very first build downloads the Kotlin compiler and its tools (a few hundred MB). It can take **5 to 10 minutes**. Grab a snack! After that, builds take seconds.
{{< /kip >}}

A window opens. Press <kbd>Space</kbd>, move with {{< keys "A" "D" >}} or the arrow keys, and catch some gems. **This is what you're going to build.**

## 4. Clear the workbench

Now make room to build it yourself:

1. Create a folder called `peek` next to `src`.
2. Move the folder `src/nativeMain/kotlin/gemminer` into `peek`.
3. Create a new, empty folder `src/nativeMain/kotlin/gemminer`.

Gradle only builds what's inside `src`, so the finished game is out of the way. You can still open `peek/` when you're curious how something works.

{{< kip yay >}}
Your tools are ready and you've played the game. Press the button below to grab your first XP!
{{< /kip >}}
