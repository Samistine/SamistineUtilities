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
package com.samistine.samistineutilities.nosandfall;

import com.samistine.samistineutilities.Reloadable;
import com.samistine.samistineutilities.Utility;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.World;
import org.bukkit.event.HandlerList;

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
 * <pre>
 *
 * </pre>
 * </blockquote>
 * </div>
 *
 * @author Samuel Seidel
 * @version 1.0
 */
public final class NoSandFall extends Utility implements Reloadable {

    private NoSandFallListener listener;
    boolean conf_enabled;
    boolean conf_all_worlds;
    List<String> conf_worlds;

    private void loadConfig() {
        this.conf_enabled = getRootConfig().getBoolean("NoSandFall.enabled", false);
        this.conf_all_worlds = getRootConfig().getBoolean("NoSandFall.all_worlds", false);
        if (!conf_all_worlds) {
            this.conf_worlds = new ArrayList<>();
            for (String s : getRootConfig().getStringList("NoSandFall.worlds")) {
                World w = getServer().getWorld(s);
                if (w == null) {
                    getLogger().log(Level.WARNING, "[NoSandFall] The world {0} was not found.", s);
                } else {
                    this.conf_worlds.add(w.getName());
                }
            }
        }
    }

    private void loadListener() {
        if (conf_enabled) {
            //We should enable or be enabled
            //Lets check which one
            if (listener == null) {
                //We are enabling, lets setup :)
                listener = new NoSandFallListener(this);
                getServer().getPluginManager().registerEvents(listener, getPlugin());
            } else {
                //We are enabled; we don't need to do any work
            }
            getLogger().log(Level.INFO, "Loaded, Block updates are disabled in {0}", (conf_all_worlds ? "all worlds." : ":" + Arrays.toString(conf_worlds.toArray())));
        } else {
            //We should disable or be disabled
            //Lets check which one
            if (listener != null) {
                //We are disabling
                HandlerList.unregisterAll(listener);
                listener = null;
            } else {
                //We are disabled
            }
        }
    }

    @Override
    public void onEnable() {
        loadConfig();
        loadListener();
    }

    @Override
    public void onReload() {
        getLogger().log(Level.INFO, "Calling com.samistine.samistineutilities.nosandfall.NoSandFall.loadConfig()");
        loadConfig();
        getLogger().log(Level.INFO, "Calling com.samistine.samistineutilities.nosandfall.NoSandFall.loadListener()");
        loadListener();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(listener);
        listener = null;
    }

    @Override
    public String getName() {
        return "NoSandFall";
    }
}
