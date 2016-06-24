package com.samistine.samistineutilities.features.physicsdisabler;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;

final class JCPDListenerSimple implements Listener {

    @EventHandler
    protected void onBlockPhysics(BlockPhysicsEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    protected void onCreatureSpawn(CreatureSpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    protected void onItemSpawn(ItemSpawnEvent event) {
        event.setCancelled(true);
    }

}
