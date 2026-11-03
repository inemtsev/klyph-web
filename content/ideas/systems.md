---
title: "Systems"
weight: 3
time: "3 min"
summary: "A system is a plain function that klyph calls in one phase, every frame. Systems are where your game's rules live."
image: "img/ideas/systems.png"
imageAlt: "A cheerful robot worker with a wrench"
---

## Little machines that never sleep

Picture a row of little machines on a conveyor belt. Each has one job: move the cart, drop gems, check for catches. Every frame, the belt goes round and every machine does its job once. Those machines are **systems**.

In klyph, a system is just a function that takes the `app`:

```kotlin
fun fall(app: App) {
    val dt = app.time.deltaSeconds
    app.world.forEach(Falling, Transform) { entity, falling, transform ->
        transform.y += falling.speed * dt
    }
}
```

and you tell klyph which {{< idea "loop" "phase" >}} it belongs to:

```kotlin
addSystem(Phase.UPDATE) { app -> fall(app) }
```

## The golden rules

1. **One job each.** `moveCart`, `fall`, `catchThings`. Small systems are easy to read and easy to fix.
2. **Order matters.** In the same phase, systems run in the order you added them. Catch *after* you fall.
3. **No data inside.** Systems don't keep their own state. Data lives in {{< idea "entities" "components" >}} or {{< idea "resources" >}}. That way any system can see it.

## Where can I add systems?

- In `runApp { }`. These run **on every screen**.
- In a {{< idea "scenes" "scene" >}}. These only run while that scene is on top. Game rules usually go here.

App systems run first, then the top scene's systems, in each phase.

{{< nerd "Why are systems separate from the data?" >}}
This style is called **ECS**: Entities, Components, Systems. Keeping data (components) apart from behavior (systems) means you can mix and match. Want rocks to fall? Give them a `Falling` component, and the `fall` system moves them. Want a falling *chicken*? Same component, zero new code. It's also very fast, because klyph keeps each kind of component packed together in memory.
{{< /nerd >}}
