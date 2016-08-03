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

import com.samistine.samistineutilities.SamistineUtilities;
import com.samistine.samistineutilities.FeatureInfo;
import com.samistine.samistineutilities.SFeature;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 *
 * @author Samuel Seidel
 */
@FeatureInfo(name = "AutoRespawn", desc = "Respawns the player automatically after he dies")
public class AutoRespawn extends SFeature implements Listener {

    private long delay;

    public AutoRespawn(SamistineUtilities main) {
        super(main);
    }

    @Override
    protected void onEnable() {
        delay = getConfig().getLong("delay", 10);
        getServer().getPluginManager().registerEvents(this, featurePlugin);
    }

    @Override
    protected void onDisable() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    protected void onPlayerDeath(PlayerDeathEvent e) {
        //Send delayed task to respawn the dead player, bypassing the respawn menu
        getServer().getScheduler().scheduleSyncDelayedTask(featurePlugin, new DeathRespawnTask(e.getEntity()), delay);
    }

    private static class DeathRespawnTask implements Runnable {

        private final Player player;

        public DeathRespawnTask(Player p) {
            this.player = p;
        }

        @Override
        public void run() {
            if (player != null && player.isOnline()) {
                player.spigot().respawn();
            }
        }
    }
}
