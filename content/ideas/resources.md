---
title: "Resources"
weight: 7
time: "2 min"
summary: "A resource is one shared object the whole game can reach, like the score or the loaded art. There's one of each kind."
image: "img/ideas/resources.png"
imageAlt: "An open treasure chest"
---

## The shared treasure chest

Some data isn't a thing in the world. The score. The pictures you loaded. The best time. There's exactly **one** of each, and lots of systems need it. Those are **resources**.

```kotlin
class Round {
    var gems = 0
    var lives = 3
}

// Put one in the chest (usually at STARTUP or in a scene's onEnter):
app.resources.insert(Round())

// Take it out anywhere:
val round = app.resources.get<Round>()
round.gems += 1
```

Resources are found by their **class**, so there's one `Round`, one `Art`, one `Sounds`. Inserting a new `Round` replaces the old one, which is a quick way to reset a game.

| Call | What it does |
|---|---|
| `insert(thing)` | Stores it, replacing any of the same class. |
| `get<T>()` | Gets it. Crashes if it isn't there. |
| `getOrNull<T>()` | Gets it, or `null`. |
| `has<T>()` | `true` if one is stored. |
| `remove<T>()` | Takes it out. |

{{< kip tip "The shortcut trick" >}}
Typing `app.resources.get<Round>()` gets old fast. One line of Kotlin makes it `app.round`:

```kotlin
val App.round: Round get() = resources.get()
```
{{< /kip >}}
