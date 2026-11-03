---
title: "Entities and components"
weight: 4
time: "4 min"
summary: "An entity is a name tag for a thing in your world. Components are the data stickers you put on it."
image: "img/ideas/entities.png"
imageAlt: "A stone cube with colorful stickers"
---

## Name tags and stickers

An **entity** is a thing in your game world: the cart, a gem, a burst of sparkles. On its own, it's only a number, a name tag. It has no position, no picture, nothing.

**Components** are stickers you put on an entity. Each one is a small bundle of data:

| Entity | Its components |
|---|---|
| Cart | `Transform`, `Sprite`, `Collider`, `Cart` |
| Gem | `Transform`, `Sprite`, `Collider`, `Falling`, `Gem` |
| Sparkle burst | `Transform`, `ParticleEmitter`, `Lifetime` |

What an entity *is* comes from which stickers it has. Nothing else.

## Make your own component

A component is a normal Kotlin class with one extra line, its **key**:

```kotlin
class Falling(var speed: Float) {
    companion object : ComponentType<Falling>("Falling")
}
```

A component with no data at all is called a **tag**. It just marks the entity: `class Rock { companion object : ComponentType<Rock>("Rock") }`.

## Spawn an entity

```kotlin
app.commands.spawn {
    add(Transform, Transform(x = 100f, y = 50f))
    add(Sprite, Sprite(art.gem, 44f, 44f))
    add(Falling, Falling(speed = 160f))
}
```

## Find entities: queries

A system asks the world for every entity with certain components. This is a **query**:

```kotlin
app.world.forEach(Falling, Transform) { entity, falling, transform ->
    transform.y += falling.speed * dt
}
```

Only entities with **both** `Falling` and `Transform` are visited. `forEach` takes one, two or three component types.

## Look at one entity

| Call | Gives you |
|---|---|
| `world.get(entity, Gem)` | The `Gem`. Crashes if there isn't one! |
| `world.getOrNull(entity, Gem)` | The `Gem`, or `null`. |
| `world.has(entity, Rock)` | `true` or `false`. |
| `world.isAlive(entity)` | Whether it still exists. |
| `world.count(Gem)` | How many entities have a `Gem`. |

## Components klyph gives you

`Transform` ({{< term "transform" >}}), `Sprite` ({{< term "sprite" >}}), `Collider` ({{< term "collider" >}}), `ParticleEmitter` ({{< term "particles" >}}), `Text` (words in the world), `SpriteAnimation` ({{< term "spritesheet" "animations" >}}), and `Interpolated` (smooth motion with a {{< term "fixed" "fixed timestep" >}}).

{{< nerd "Parents and children" >}}
Entities can be stuck to other entities. `app.commands.setParent(child, parent)` makes the child move, turn and scale with its parent, like a hat on a head. Its `Transform` is then measured from the parent. Destroying a parent destroys its children too.
{{< /nerd >}}
