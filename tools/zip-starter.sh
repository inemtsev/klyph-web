#!/usr/bin/env bash
# Zips starter/ as gem-miner/ into <site folder>/downloads/gem-miner-starter.zip, the starter kit
# Quest step 1 links to. CI runs it after Hugo, so the download works whether or not this
# repository is public. Run it yourself after `hugo` to try the link locally:
#
#   hugo && tools/zip-starter.sh public
set -euo pipefail

site="${1:?Usage: tools/zip-starter.sh <site folder, such as public>}"
root="$(cd "$(dirname "$0")/.." && pwd)"
work="$(mktemp -d)"
trap 'rm -rf "$work"' EXIT

cp -R "$root/starter" "$work/gem-miner"
rm -rf "$work/gem-miner/build" "$work/gem-miner/.gradle" "$work/gem-miner/.kotlin"
mkdir -p "$site/downloads"
out="$(cd "$site/downloads" && pwd)/gem-miner-starter.zip"
rm -f "$out"
(cd "$work" && zip -qr "$out" gem-miner)
echo "Wrote $out"
