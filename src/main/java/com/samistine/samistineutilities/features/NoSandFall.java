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

import com.samistine.samistineutilities.utils.annotations.config.ConfigPath;
import com.samistine.samistineutilities.api.objects.FeatureInfo;
import com.samistine.samistineutilities.api.SFeature;
import com.samistine.samistineutilities.api.SListener;
import com.samistine.samistineutilities.utils.annotations.config.ConfigPathProcessor;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

/**
 * <h1>NoSandFall</h1>
 *
 * <div>
 * <h2>Description:</h2>
 * <blockquote>
 * <p>
 * This is a utility that allows server owners to disable blocks from falling.
 * It is primarily designed to function as a backup to WorldGuard and other
 * plugins incase they stop functioning.</p>
 * <p>
 * This project is a complete rewrite of a similar plugin also called
 * NoSandFall.</p>
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
 * </blockquote>
 * </div>
 *
 * @author Samuel Seidel
 * @version 1.0
 */
@FeatureInfo(name = "NoSandFall", desc = "Stops falling blocks in specified worlds")
public final class NoSandFall extends SFeature {

    @ConfigPath(path = "all_worlds")
    boolean conf_all_worlds = false;

    @ConfigPath(path = "worlds")
    List<String> conf_worlds = Collections.emptyList();

    public NoSandFall() {
        new ConfigPathProcessor(getLogger()).loadValues(getRootConfig().getConfigurationSection("NoSandFall"), this);

        if (!conf_all_worlds) {
            Iterator<String> it = conf_worlds.iterator();
            while (it.hasNext()) {
                String world_name = it.next();
                if (getServer().getWorld(world_name) == null) {
                    getLogger().log(Level.WARNING, "The world {0} was not found.", world_name);
                    it.remove();
                }
            }
        }

        new NSFListener(conf_all_worlds, new HashSet<>(conf_worlds)).registerListener(this);
        getLogger().log(Level.INFO, "Loaded, Falling blocks are disabled in {0}", (conf_all_worlds ? "all worlds." : ":" + Arrays.toString(conf_worlds.toArray())));
    }

    /**
     * The listener used by {@link NoSandFall} for blocking falling blocks.
     *
     * @author Samuel Seidel
     */
    private static class NSFListener implements SListener {

        final boolean conf_all_worlds;
        final Collection<String> conf_worlds;

        NSFListener(boolean conf_all_worlds, Collection<String> conf_worlds) {
            this.conf_all_worlds = conf_all_worlds;
            this.conf_worlds = conf_worlds;
        }

        @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
        public void onFall(EntityChangeBlockEvent event) {
            if (event.getEntityType() == EntityType.FALLING_BLOCK
                    && (conf_all_worlds || conf_worlds.contains(event.getBlock().getWorld().getName()))) {
                event.setCancelled(true);
            }
        }
    }

}
