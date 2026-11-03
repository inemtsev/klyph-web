---
title: "klyph"
lead: "klyph is a small, fast 2D game engine. Build a real game in **11 short steps**, each about 10 minutes. No boring bits, and no 300-page manual."
paths:
  - title: "The Quest"
    text: "Build Gem Miner, a game where you catch falling gems in a minecart. One small step at a time."
    image: "img/kip/laptop.png"
    url: "quest/"
    go: "Start building"
  - title: "Big Ideas"
    text: "The 9 ideas that make klyph tick: systems, scenes, entities and friends. Short pages, lots of pictures."
    image: "img/ideas/app.png"
    url: "ideas/"
    go: "Learn the ideas"
  - title: "Game Words"
    text: "What's a sprite? A collider? Delta time? Every game dev word, explained in a minute."
    image: "img/words/sprite.png"
    url: "words/"
    go: "Look up a word"
---

{{< kip hi >}}
I'm a block with a sprout on my head, and I'll be your guide. When you see me, I've got a tip, a warning, or a little party for you. Every step you finish gives you **XP**. Watch the bar at the top!
{{< /kip >}}

## klyph is built from blocks

A klyph game is made from a few simple pieces that snap together. You'll meet each one on the Quest.

<div class="blocks">
  <div class="block"><img src="img/ideas/entities.png" alt=""><b>Entities</b><span>The things in your game: a cart, a gem, a rock.</span></div>
  <div class="block"><img src="img/words/sprite.png" alt=""><b>Components</b><span>Stickers on an entity: its picture, its position, its speed.</span></div>
  <div class="block"><img src="img/ideas/systems.png" alt=""><b>Systems</b><span>Little machines that run every frame and make things move.</span></div>
  <div class="block"><img src="img/ideas/scenes.png" alt=""><b>Scenes</b><span>The screens of your game: title, play, pause, game over.</span></div>
</div>

That's the whole trick. A {{< term "engine" "game engine" >}} like klyph does the hard work (windows, drawing, sound, keyboards) so you can spend your time on the fun part: **your game**.

## What you'll build

<figure>
  <img class="pic" src="img/gem-miner.png" alt="Gem Miner: a wooden minecart at the bottom of a dark stone cave catches colorful falling gems, while grey rocks fall too. GEMS 7 is shown top left and LIVES 2 top right." width="800" height="600" loading="lazy">
  <figcaption>Gem Miner: slide the cart, catch the gems, dodge the rocks.</figcaption>
</figure>

By the end of the Quest your game has a moving cart, falling gems, sparkles, sounds, a score, lives, a title screen, a pause screen and a game over screen. Then you can zip it up and send it to a friend.

## Why klyph?

- **It's Kotlin.** A friendly, modern language. If you've written a little Java or Kotlin before, you'll feel at home.
- **Tiny setup.** Your whole build file is three lines long.
- **Fast.** Games compile to a real native program. No extra runtime to install.
- **Windows, macOS and Linux.** Same code everywhere.

## Before you start

You need a computer (Windows, a Mac with an Apple chip, or Linux) and about an hour in total. Take breaks! Your XP is saved in this browser.

New to Kotlin? Read [Kotlin in 5 minutes](kotlin/) first. It covers everything the Quest uses.

<p><a class="btn btn--big" href="quest/">▶ Start the Quest</a></p>
