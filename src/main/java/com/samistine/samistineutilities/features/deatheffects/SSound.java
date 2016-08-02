/*
 * The MIT License
 *
 * Copyright 2016 Samuel Seidel.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.samistine.samistineutilities.features.deatheffects;

import java.util.Map;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 *
 * @author Samuel Seidel
 */
final class SSound {

    private final Sound sound;
    private final float volume;
    private final float pitch;

    public SSound(Sound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public SSound(Map<String, Object> section) {
        String _sound = (String) section.get("sound");
        float _volume = (float) ((double) section.getOrDefault("volume", 10));
        float _pitch = (float) ((double) section.getOrDefault("pitch", 1));

        Sound __sound = Sound.valueOf(_sound.toUpperCase());

        this.sound = __sound;
        this.volume = _volume;
        this.pitch = _pitch;
    }

    public Sound getSound() {
        return sound;
    }

    public float getVolume() {
        return volume;
    }

    public float getPitch() {
        return pitch;
    }

    public void playToPlayer(Player player) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    public void playToWorld(Player player) {
        player.getWorld().playSound(player.getLocation(), sound, volume, pitch);
    }
}
