---
title: "Commands"
weight: 5
time: "2 min"
summary: "Commands are a to-do list for the world. Spawn and destroy through `app.commands`, and klyph applies them right after your system."
image: "img/ideas/commands.png"
imageAlt: "A clipboard with a checklist"
---

## Don't pull blocks out of the tower you're climbing

Imagine walking along a row of gems, counting them, and someone removes one under your feet. You'd lose your place! The same goes for code: changing the world **while** a system loops over it causes trouble.

So klyph gives you **`app.commands`**, a to-do list. You write down what you want, and klyph does it all **right after your system finishes**:

```kotlin
app.world.forEach(Falling, Transform) { entity, falling, transform ->
    if (transform.y > 664f) app.commands.destroy(entity) // done after the loop. Safe!
}
```

## What's on the menu

| Command | What it does |
|---|---|
| `spawn { add(...) }` | Makes a new entity with these components. |
| `destroy(entity)` | Removes the entity (and its children). |
| `add(entity, Type, component)` | Sticks a component on. |
| `remove(entity, Type)` | Peels a component off. |
| `setParent(child, parent)` | Sticks one entity to another. |

{{< kip tip "Spawned entities show up next system" >}}
`spawn` gives you the entity straight away, so you can keep it in a variable. But its components only attach when the commands are applied, so a query in the *same* system won't see it yet. The next system will.
{{< /kip >}}
