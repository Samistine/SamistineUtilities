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
import com.samistine.samistineutilities.utils.annotations.config.ConfigPath;
import com.samistine.samistineutilities.utils.annotations.config.ConfigPathProcessor;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.weather.WeatherChangeEvent;

import java.util.logging.Level;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.HashSet;
import java.util.Iterator;

/**
 * <h1>NoRainFall</h1>
 *
 * <div>
 * <h2>Description:</h2>
 * <blockquote>
 * <p>
 * This is a utility that allows server owners to disable rain.
 * <p>
 * This utility does not affect server performance if left disabled. It is
 * disabled by default in the configuration.</p>
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
 * <pre>
 *
 * </pre>
 * </blockquote>
 * </div>
 *
 * @author Samuel Seidel
 * @version 1.0
 */
@FeatureInfo(name = "NoRainFall", desc = "Stops rain in specified worlds")
public final class NoRainFall extends SFeature {

    @ConfigPath(path = "enabled")
    boolean conf_enabled = false;

    @ConfigPath(path = "all_worlds")
    boolean conf_all_worlds = false;

    @ConfigPath(path = "worlds")
    List<String> conf_worlds = Collections.emptyList();

    public NoRainFall() {
        new ConfigPathProcessor(getLogger()).loadValues(getRootConfig().getConfigurationSection("NoRainFall"), this);

        if (conf_enabled && !conf_all_worlds) {
            Iterator<String> it = conf_worlds.iterator();
            while (it.hasNext()) {
                String world_name = it.next();
                if (getServer().getWorld(world_name) == null) {
                    getLogger().log(Level.WARNING, "The world {0} was not found.", world_name);
                    it.remove();
                }
            }
        }

        if (conf_enabled) {
            new NRFListener(conf_all_worlds, new HashSet<>(conf_worlds)).registerListener(this);
            getLogger().log(Level.INFO, "Loaded, Weather is disabled in {0}", (conf_all_worlds ? "all worlds." : ":" + Arrays.toString(conf_worlds.toArray())));
        }
    }

    /**
     * The listener used by {@link NoRainFall} for blocking rain.
     *
     * @author Samuel Seidel
     */
    private static final class NRFListener implements SListener {

        final boolean conf_all_worlds;
        final Collection<String> conf_worlds;

        NRFListener(boolean conf_all_worlds, Collection<String> conf_worlds) {
            this.conf_all_worlds = conf_all_worlds;
            this.conf_worlds = conf_worlds;
        }

        @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
        public void onRainStart(WeatherChangeEvent event) {
            if (event.toWeatherState()
                    && (conf_all_worlds || conf_worlds.contains(event.getWorld().getName()))) {
                event.setCancelled(true);
            }
        }
    }

}
