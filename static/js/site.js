// klyph docs: theme toggle, XP and checkmarks, tabs, copy buttons.
// Progress lives in this browser only (localStorage). Every storage call is wrapped, so the
// site still works where storage is blocked; it just forgets.
(function () {
  "use strict";

  var STORE_KEY = "klyph:progress:v1";
  var THEME_KEY = "klyph:theme";
  var TAB_KEY = "klyph:tab";

  var LEVELS = [
    { xp: 0, title: "Pebble" },
    { xp: 60, title: "Digger" },
    { xp: 150, title: "Miner" },
    { xp: 280, title: "Crafter" },
    { xp: 420, title: "Builder" },
    { xp: 580, title: "Engineer" },
    { xp: 780, title: "Legend" }
  ];

  function read(key) {
    try { return window.localStorage.getItem(key); } catch (e) { return null; }
  }
  function write(key, value) {
    try { window.localStorage.setItem(key, value); } catch (e) { /* storage blocked: forget */ }
  }
  function remove(key) {
    try { window.localStorage.removeItem(key); } catch (e) { /* ignore */ }
  }

  // ---------- Theme ----------

  function currentTheme() {
    var set = document.documentElement.getAttribute("data-theme");
    if (set) return set;
    return window.matchMedia && window.matchMedia("(prefers-color-scheme: dark)").matches ? "dark" : "light";
  }
  document.querySelectorAll("[data-theme-toggle]").forEach(function (button) {
    button.addEventListener("click", function () {
      var next = currentTheme() === "dark" ? "light" : "dark";
      document.documentElement.setAttribute("data-theme", next);
      write(THEME_KEY, next);
    });
  });

  // ---------- Progress ----------

  function load() {
    try {
      var data = JSON.parse(read(STORE_KEY) || "{}");
      return data && typeof data.done === "object" && data.done ? data : { done: {} };
    } catch (e) {
      return { done: {} };
    }
  }
  var progress = load();
  function save() { write(STORE_KEY, JSON.stringify(progress)); }

  function totalXp() {
    var sum = 0;
    for (var key in progress.done) {
      if (Object.prototype.hasOwnProperty.call(progress.done, key)) sum += Number(progress.done[key]) || 0;
    }
    return sum;
  }
  function levelOf(xp) {
    var index = 0;
    for (var i = 0; i < LEVELS.length; i++) if (xp >= LEVELS[i].xp) index = i;
    var next = LEVELS[index + 1];
    var share = next ? (xp - LEVELS[index].xp) / (next.xp - LEVELS[index].xp) : 1;
    return { number: index + 1, title: LEVELS[index].title, share: share, next: next };
  }

  function render() {
    var xp = totalXp();
    var level = levelOf(xp);
    document.querySelectorAll("[data-xp-level]").forEach(function (el) { el.textContent = "Lv " + level.number; });
    document.querySelectorAll("[data-xp-title]").forEach(function (el) { el.textContent = level.title + " · " + xp + " XP"; });
    document.querySelectorAll("[data-xp-fill]").forEach(function (el) { el.style.width = Math.round(level.share * 100) + "%"; });
    document.querySelectorAll("[data-xp-sr]").forEach(function (el) {
      el.textContent = "Level " + level.number + ", " + level.title + ", " + xp + " XP";
    });

    document.querySelectorAll("[data-key]").forEach(function (el) {
      el.classList.toggle("is-done", Boolean(progress.done[el.getAttribute("data-key")]));
    });
    document.querySelectorAll("[data-done]").forEach(function (button) {
      var done = Boolean(progress.done[button.getAttribute("data-key")]);
      button.setAttribute("aria-pressed", done ? "true" : "false");
    });

    // "3 of 11 done" on section pages and in the sidebar.
    document.querySelectorAll("[data-section-progress]").forEach(function (el) {
      var section = el.getAttribute("data-section-progress");
      var total = Number(el.getAttribute("data-total")) || 0;
      var count = countDone(section);
      el.textContent = total ? (count === total ? "★ All " + total + " done! Amazing." : count + " of " + total + " done") : "";
    });
    document.querySelectorAll("[data-section-count]").forEach(function (el) {
      var list = el.closest("[data-sidebar]");
      var total = list ? list.querySelectorAll(".sidebar__list a").length : 0;
      el.textContent = countDone(el.getAttribute("data-section-count")) + "/" + total;
    });
  }
  function countDone(section) {
    var prefix = "/" + section + "/";
    var count = 0;
    for (var key in progress.done) {
      if (progress.done[key] && key.indexOf(prefix) !== -1 && key.slice(key.indexOf(prefix)) !== prefix) count++;
    }
    return count;
  }

  document.querySelectorAll("[data-done]").forEach(function (button) {
    button.addEventListener("click", function () {
      var key = button.getAttribute("data-key");
      var xp = Number(button.getAttribute("data-xp")) || 0;
      var before = levelOf(totalXp()).number;
      if (progress.done[key]) {
        delete progress.done[key];
        save();
        render();
        toast("Unticked. No worries!");
        return;
      }
      progress.done[key] = xp;
      save();
      render();
      var after = levelOf(totalXp());
      bumpXp();
      confetti(button);
      toast(after.number > before ? "LEVEL UP! You're a " + after.title + " now!" : "+" + xp + " XP. Nice!");
    });
  });

  document.querySelectorAll("[data-reset-progress]").forEach(function (button) {
    button.addEventListener("click", function () {
      if (!window.confirm("Reset all your XP and checkmarks in this browser?")) return;
      progress = { done: {} };
      remove(STORE_KEY);
      render();
      toast("Fresh start!");
    });
  });

  // ---------- Juice ----------

  var reduceMotion = window.matchMedia && window.matchMedia("(prefers-reduced-motion: reduce)").matches;
  var toastTimer = 0;
  function toast(text) {
    var el = document.getElementById("toast");
    if (!el) return;
    el.textContent = text;
    el.classList.add("is-shown");
    clearTimeout(toastTimer);
    toastTimer = setTimeout(function () { el.classList.remove("is-shown"); }, 2200);
  }
  function bumpXp() {
    document.querySelectorAll(".xp").forEach(function (el) {
      el.classList.remove("is-bumped");
      void el.offsetWidth;
      el.classList.add("is-bumped");
    });
  }
  function confetti(from) {
    if (reduceMotion) return;
    var colors = ["#e8486b", "#3f8cff", "#2fb86f", "#ffc233", "#9b5cff", "#19b39a"];
    var box = from.getBoundingClientRect();
    var x0 = box.left + box.width / 2;
    var y0 = box.top + box.height / 2;
    for (var i = 0; i < 36; i++) {
      var bit = document.createElement("span");
      bit.className = "confetti";
      bit.style.background = colors[i % colors.length];
      document.body.appendChild(bit);
      var angle = Math.random() * Math.PI * 2;
      var speed = 140 + Math.random() * 260;
      var dx = Math.cos(angle) * speed;
      var dy = Math.sin(angle) * speed - 220;
      var animation = bit.animate(
        [
          { transform: "translate(" + x0 + "px," + y0 + "px)", opacity: 1 },
          { transform: "translate(" + (x0 + dx) + "px," + (y0 + dy + 420) + "px)", opacity: 0 }
        ],
        { duration: 900 + Math.random() * 500, easing: "cubic-bezier(.2,.6,.4,1)" }
      );
      animation.onfinish = (function (node) { return function () { node.remove(); }; })(bit);
    }
  }

  // ---------- Tabs ----------

  function selectTab(group, name) {
    var found = false;
    group.querySelectorAll("[data-tab]").forEach(function (tab) {
      var on = tab.getAttribute("data-tab") === name;
      if (on) found = true;
      tab.setAttribute("aria-selected", on ? "true" : "false");
      tab.tabIndex = on ? 0 : -1;
    });
    if (!found) return false;
    group.querySelectorAll("[data-panel]").forEach(function (panel) {
      panel.hidden = panel.getAttribute("data-panel") !== name;
    });
    return true;
  }
  var savedTab = read(TAB_KEY);
  document.querySelectorAll("[data-tabs]").forEach(function (group) {
    if (savedTab) selectTab(group, savedTab);
    group.querySelectorAll("[data-tab]").forEach(function (tab) {
      tab.addEventListener("click", function () {
        var name = tab.getAttribute("data-tab");
        write(TAB_KEY, name);
        document.querySelectorAll("[data-tabs]").forEach(function (other) { selectTab(other, name); });
      });
      tab.addEventListener("keydown", function (event) {
        if (event.key !== "ArrowRight" && event.key !== "ArrowLeft") return;
        var tabs = Array.prototype.slice.call(group.querySelectorAll("[data-tab]"));
        var index = tabs.indexOf(tab) + (event.key === "ArrowRight" ? 1 : -1);
        var next = tabs[(index + tabs.length) % tabs.length];
        next.focus();
        next.click();
      });
    });
  });

  // ---------- Copy buttons ----------

  document.querySelectorAll("[data-copy]").forEach(function (button) {
    button.addEventListener("click", function () {
      var block = button.closest(".code");
      var code = block && block.querySelector("pre code");
      if (!code || !navigator.clipboard) return;
      // textContent, not innerText: the highlighter makes every line a flex box, and innerText
      // adds a line break after each box on top of the line's own newline.
      navigator.clipboard.writeText(code.textContent.replace(/\n$/, "")).then(function () {
        button.textContent = "Copied!";
        button.classList.add("is-copied");
        setTimeout(function () { button.textContent = "Copy"; button.classList.remove("is-copied"); }, 1500);
      });
    });
  });

  // ---------- Sidebar on small screens starts closed ----------

  if (window.matchMedia && window.matchMedia("(max-width: 960px)").matches) {
    document.querySelectorAll("[data-sidebar]").forEach(function (box) { box.open = false; });
  }

  render();
})();
