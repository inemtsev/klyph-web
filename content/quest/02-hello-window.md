---
title: "Hello, window!"
linkTitle: "Hello, window!"
step: 2
weight: 2
time: "5 min"
summary: "Write 15 lines of Kotlin that open your game's window."
learn:
  - "What `runApp` and `AppConfig` do"
  - "How the 3-line build file finds your code"
image: "img/ideas/app.png"
imageAlt: "A pixel-art arcade machine"
---

## Make the file

In IntelliJ, right-click the empty `src/nativeMain/kotlin/gemminer` folder, choose **New → Kotlin Class/File → File**, and call it `Main`. Paste this in:

```kotlin {title="Main.kt"}
package gemminer

import klyph.app.AppConfig
import klyph.app.runApp
import klyph.platform.Color

fun main() {
    val config = AppConfig(
        title = "Gem Miner",
        width = 800,
        height = 600,
        clearColor = Color(0x2B1D14FFu),
    )

    runApp(config) {
        // Nothing here yet!
    }
}
```

Now run it, like last time:

```bash
./gradlew run
```

(On Windows: `.\gradlew run`.) You get an 800 × 600 window, filled with dark cave-brown. Close it with the ✕ button.

## What just happened?

- **`AppConfig`** is the settings for your game: the window title, its size, and the `clearColor` the screen gets wiped with before each picture is drawn.
- **`runApp`** opens the window and starts the {{< term "loop" "game loop" >}}. It keeps going until you close the window.
- The `{ }` block after `runApp` is where you'll add your game's {{< idea "systems" >}}. It's empty for now.

{{< kip tip "Colors in hex" >}}
`0x2B1D14FFu` is a color written as **RRGGBBAA**: red `2B`, green `1D`, blue `14`, and alpha (see-through-ness) `FF`, which means solid. Try `0x3F8CFFFFu` for a blue sky!
{{< /kip >}}

## How does Gradle find `main`?

Open `build.gradle.kts`. The whole build is three lines:

```kotlin {title="build.gradle.kts"}
plugins { id("com.eventslooped.klyph") version "0.1.0" }
klyph { entryPoint = "gemminer.main" }
kotlin { sourceSets.nativeMain.dependencies { implementation("com.eventslooped.klyph:klyph:0.1.0") } }
```

1. Use the klyph plugin.
2. The game starts at the function `main` in the package `gemminer`. That's your file!
3. Your code uses the klyph engine, version 0.1.0.

{{< nerd "What's `u` at the end of the color?" >}}
The `u` makes the number an **unsigned** integer (`UInt`). It means "no minus numbers allowed", which is right for colors. Without it, Kotlin would complain that `0x2B1D14FF` is too big for a normal `Int`.
{{< /nerd >}}

{{< try >}}
Change the title to your own game name and make the window 1024 × 768. Run it again.
{{< /try >}}
