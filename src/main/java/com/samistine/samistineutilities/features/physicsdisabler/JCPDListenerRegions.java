package com.samistine.samistineutilities.features.physicsdisabler;

import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;

final class JCPDListenerRegions implements Listener {

    private final HashMap<String, JCPDRegion> regions = new HashMap<>();

    public void addRegion(String name, JCPDRegion region) {
        this.regions.put(name, region);
    }

    public void removeRegion(String name) {
        this.regions.remove(name);
    }

    public boolean existsRegion(String name) {
        return this.regions.containsKey(name);
    }

    private boolean isInRegion(Location loc) {
        return this.regions.values().stream().anyMatch(region -> region.isInRegion(loc));//Parallelizable?
    }

    @EventHandler
    protected void onBlockPhysics(BlockPhysicsEvent event) {
        if (isInRegion(event.getBlock().getLocation())) event.setCancelled(true);
    }

    @EventHandler
    protected void onCreatureSpawn(CreatureSpawnEvent event) {
        if (isInRegion(event.getLocation())) event.setCancelled(true);
    }

    @EventHandler
    protected void onItemSpawn(ItemSpawnEvent event) {
        if (isInRegion(event.getLocation())) event.setCancelled(true);
    }

}
