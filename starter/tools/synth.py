#!/usr/bin/env python3
"""Makes Gem Miner's sound effects (WAV) and its music loop (WAV, then OGG through ffmpeg).

Usage, from the starter folder:

    python3 tools/synth.py            # writes into assets/

Only the Python standard library is needed for the WAV files. The music is written as
assets/music.ogg when ffmpeg is installed, otherwise as music.wav and you convert it yourself.
The output is the same on every run: there is a fixed random seed and nothing reads the clock.
"""
import math
import random
import shutil
import struct
import subprocess
import wave
from pathlib import Path

RATE = 44100
OUT = Path(__file__).resolve().parent.parent / "assets"


def write_wav(path, samples, peak=0.8):
    top = max(1e-9, max(abs(s) for s in samples))
    with wave.open(str(path), "wb") as f:
        f.setnchannels(1)
        f.setsampwidth(2)
        f.setframerate(RATE)
        f.writeframes(b"".join(struct.pack("<h", int(s / top * peak * 32767)) for s in samples))


def square(phase):
    return 1.0 if (phase % 1.0) < 0.5 else -1.0


def tone(freq, seconds, wave_fn=math.sin, decay=8.0, volume=1.0):
    """One note: a wave at freq that starts at once and dies away."""
    n = int(RATE * seconds)
    out = []
    for i in range(n):
        t = i / RATE
        env = math.exp(-decay * t) * min(1.0, t / 0.003)
        phase = freq * t
        value = wave_fn(phase) if wave_fn is square else wave_fn(2 * math.pi * phase)
        out.append(value * env * volume)
    return out


def mix(*parts):
    """Adds (start_seconds, samples) pairs into one track."""
    end = max(int(start * RATE) + len(s) for start, s in parts)
    out = [0.0] * end
    for start, s in parts:
        offset = int(start * RATE)
        for i, v in enumerate(s):
            out[offset + i] += v
    return out


def ding():
    # Two bright notes a fifth apart: the "you got a gem!" sparkle.
    return mix((0.0, tone(1318.5, 0.25, decay=14)), (0.06, tone(1975.5, 0.35, decay=10)))


def bonk(rng):
    # A low thud with a crunchy noise burst on top: a rock landing in the cart.
    n = int(RATE * 0.25)
    out = []
    for i in range(n):
        t = i / RATE
        freq = 140 * (60 / 140) ** (i / n)
        thud = math.sin(2 * math.pi * freq * t) * math.exp(-14 * t)
        crunch = (rng.random() * 2 - 1) * math.exp(-30 * t) * 0.6
        out.append(thud + crunch)
    return out


def start():
    # A rising arpeggio: C E G C.
    notes = [523.3, 659.3, 784.0, 1046.5]
    return mix(*[(k * 0.08, tone(f, 0.3, square, decay=9, volume=0.4)) for k, f in enumerate(notes)])


def game_over():
    # A falling arpeggio: G E C, then a low C.
    notes = [784.0, 659.3, 523.3, 261.6]
    return mix(*[(k * 0.16, tone(f, 0.45, square, decay=5, volume=0.4)) for k, f in enumerate(notes)])


def music():
    # Eight bars of a cheerful mining tune at 120 bpm: square-wave melody over a bouncing bass.
    beat = 0.25
    melody = [72, 76, 79, 76, 74, 77, 81, 77, 72, 76, 79, 84, 83, 79, 74, 0] * 2
    bass = [48, 55, 50, 57, 48, 55, 43, 50] * 2
    parts = []
    for k, note in enumerate(melody):
        if note:
            parts.append((k * beat * 2, tone(440 * 2 ** ((note - 69) / 12), 0.4, square, decay=6, volume=0.18)))
    for k, note in enumerate(bass):
        for half in range(2):
            parts.append((k * beat * 4 + half * beat * 2, tone(440 * 2 ** ((note - 69) / 12), 0.45, math.sin, decay=4, volume=0.5)))
    track = mix(*parts)
    length = int(len(melody) * beat * 2 * RATE)
    return (track + [0.0] * length)[:length]


def main():
    rng = random.Random(7)
    OUT.mkdir(parents=True, exist_ok=True)
    write_wav(OUT / "ding.wav", ding())
    write_wav(OUT / "bonk.wav", bonk(rng))
    write_wav(OUT / "start.wav", start())
    write_wav(OUT / "gameover.wav", game_over())
    wav = OUT / "music.wav"
    write_wav(wav, music(), peak=0.5)
    if shutil.which("ffmpeg"):
        subprocess.run(
            ["ffmpeg", "-y", "-loglevel", "error", "-i", str(wav), "-c:a", "libvorbis", "-q:a", "4", str(OUT / "music.ogg")],
            check=True,
        )
        wav.unlink()
        print("Wrote ding.wav, bonk.wav, start.wav, gameover.wav and music.ogg to", OUT)
    else:
        print("Wrote the sounds and music.wav to", OUT, "- convert music.wav to music.ogg (Ogg Vorbis) yourself.")


if __name__ == "__main__":
    main()
