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

import com.samistine.samistineutilities.SamistineUtilities;
import com.samistine.samistineutilities.api.FeatureInfo;
import com.samistine.samistineutilities.api.SFeature;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.bukkit.Effect;
import org.bukkit.Location;
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
@FeatureInfo(name = "DeathEffects", desc = "Play effects when and where a player dies.")
public final class DeathEffects extends SFeature implements Listener {

    private boolean explosion;
    private List<SSound> soundEffects;
    private List<Effect> effects;

    public DeathEffects(SamistineUtilities main) {
        super(main);
    }

    @Override
    protected void onEnable() {
        this.explosion = getConfig().getBoolean("explosion", false);
        this.soundEffects = ((List<Map<String, Object>>) getConfig().getList("sounds")).stream()
                .map(SSound::new)
                .collect(Collectors.toList());
        this.effects = getConfig().getStringList("effects").stream()
                .map(t -> Effect.valueOf(t))
                .collect(Collectors.toList());
        getServer().getPluginManager().registerEvents(this, featurePlugin);
    }

    @Override
    protected void onDisable() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    protected void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();
        Location pLoc = player.getLocation();

        if (killer != null) {

            if (killer.isOp() || player.isOp()) {
                player.getWorld().strikeLightningEffect(pLoc);
            }

            soundEffects.forEach(sound -> sound.playToWorld(player));

            effects.forEach(effect -> {
                player.getWorld().playEffect(pLoc, effect, 0);
            });

            if (explosion) {
                player.getWorld().createExplosion(pLoc, 0.0F);
            }

        }
    }

}
