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
package com.samistine.samistineutilities.features;

import com.samistine.samistineutilities.api.objects.FeatureInfo;
import com.samistine.samistineutilities.api.SFeature;
import com.samistine.samistineutilities.api.SListener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.weather.WeatherChangeEvent;

import java.util.logging.Level;
import java.util.Arrays;
import java.util.HashSet;

/**
 * <h1>NoRainFall</h1>
 *
 * <div>
 * <h2>Description:</h2>
 * <blockquote>
 * <p>
 * This is a utility that allows server owners to disable rain.
 * </blockquote>
 * </div>
 *
 * <div>
 * <h2>Usage:</h2>
 * <blockquote>
 * <p>
 * This utility can be enabled/disabled in the main configuration file. There
 * are also multiple options to choose from, see below.</p>
 * <hr>
 * </blockquote>
 * </div>
 *
 * @author Samuel Seidel
 * @version 1.0
 */
@FeatureInfo(name = "NoRainFall", desc = "Stops rain in specified worlds")
public final class NoRainFall extends SFeature implements SListener {

    private final boolean conf_all_worlds;
    private final HashSet<String> conf_worlds;

    public NoRainFall() {
        conf_all_worlds = this.getConfig().getBoolean("all_worlds", false);
        conf_worlds = new HashSet<>(this.getConfig().getStringList("worlds"));

        if (!conf_all_worlds) {
            conf_worlds.stream()
                    .filter(world -> getServer().getWorld(world) == null)
                    .forEach(world -> getLogger().log(Level.WARNING, "The world {0} was not found.", world));
        }

        getLogger().log(Level.INFO, "Weather is disabled in {0}", (conf_all_worlds ? "all worlds." : ":" + Arrays.toString(conf_worlds.toArray())));
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onRainStart(WeatherChangeEvent event) {
        if (event.toWeatherState()
                && (conf_all_worlds || conf_worlds.contains(event.getWorld().getName()))) {
            event.setCancelled(true);
        }
    }

}
