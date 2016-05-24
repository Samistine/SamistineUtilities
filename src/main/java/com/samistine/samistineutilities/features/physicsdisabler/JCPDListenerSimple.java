package com.samistine.samistineutilities.features.physicsdisabler;

import com.samistine.samistineutilities.api.SListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;

class JCPDListenerSimple implements SListener {

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        event.setCancelled(true);
    }
}
