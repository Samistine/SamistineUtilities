/*
 * Licensed as GPLv3 
 */
package com.samistine.samistineutilities.features;

import com.samistine.samistineutilities.SamistineUtilities;
import com.samistine.samistineutilities.api.FeatureInfo;
import com.samistine.samistineutilities.api.SFeature;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 *
 * @author xCopyright & Samistine
 */
@FeatureInfo(name = "AntiPortalTrap", desc = "Prevents players from being portal trapped")
public final class AntiPortalTrap extends SFeature implements Listener {

    Map<Player, XZLocation> portalloc = new HashMap<>();

    HashSet<Integer> allowedBottomBlocks;
    HashSet<Integer> allowedTopperBlocks;

    public AntiPortalTrap(SamistineUtilities main) {
        super(main);
    }

    @Override
    protected void onEnable() {
        getServer().getPluginManager().registerEvents(this, featurePlugin);
        this.allowedBottomBlocks = new HashSet<>(getConfig().getIntegerList("Allowed Blocks.Bottom"));
        this.allowedTopperBlocks = new HashSet<>(getConfig().getIntegerList("Allowed Blocks.Top"));
    }

    @Override
    protected void onDisable() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    protected void onPortalEvent(PlayerPortalEvent event) {
        getServer().getScheduler().scheduleSyncDelayedTask(featurePlugin, new TrapCheck(event.getPlayer()), 1L);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    protected void onPlayerMoveEvent(PlayerMoveEvent event) {
        Material material = event.getPlayer().getLocation().getBlock().getType();
        if (material == Material.PORTAL || material == Material.ENDER_PORTAL) {
            getServer().getScheduler().scheduleSyncDelayedTask(featurePlugin, new TrapCheck(event.getPlayer()), 1L);
        }
    }

    private class TrapCheck implements Runnable {

        private final Player p;

        public TrapCheck(Player p) {
            this.p = p;
        }

        @Override
        public void run() {
            if (p == null || !p.isOnline()) {
                return;
            }
            Location l = p.getLocation();
            Location nblock = p.getLocation().add(0, 0, -1);
            Location midnblock = p.getLocation().add(0, 1, -1);
            Location topnblock = p.getLocation().add(0, 2, -1);
            Location sblock = p.getLocation().add(0, 0, 1);
            Location midsblock = p.getLocation().add(0, 1, 1);
            Location topsblock = p.getLocation().add(0, 2, 1);
            Location eblock = p.getLocation().add(1, 0, 0);
            Location mideblock = p.getLocation().add(1, 1, 0);
            Location topeblock = p.getLocation().add(1, 2, 0);
            Location wblock = p.getLocation().add(-1, 0, 0);
            Location midwblock = p.getLocation().add(-1, 1, 0);
            Location topwblock = p.getLocation().add(-1, 2, 0);

            while (nblock.getBlock().getType() == Material.PORTAL || nblock.getBlock().getType() == Material.ENDER_PORTAL) {
                nblock.setZ(nblock.getZ() - 1.0);
                midnblock.setZ(nblock.getZ());
                topnblock.setZ(nblock.getZ());
            }
            while (sblock.getBlock().getType() == Material.PORTAL || sblock.getBlock().getType() == Material.ENDER_PORTAL) {
                sblock.setZ(sblock.getZ() + 1.0);
                midsblock.setZ(sblock.getZ());
                topsblock.setZ(sblock.getZ());
            }
            while (eblock.getBlock().getType() == Material.PORTAL || eblock.getBlock().getType() == Material.ENDER_PORTAL) {
                eblock.setX(eblock.getX() + 1.0);
                mideblock.setX(eblock.getX());
                topeblock.setX(eblock.getX());
            }
            while (wblock.getBlock().getType() == Material.PORTAL || wblock.getBlock().getType() == Material.ENDER_PORTAL) {
                wblock.setX(wblock.getX() - 1.0);
                midwblock.setX(wblock.getX());
                topwblock.setX(wblock.getX());
            }
            if ((!allowedBottomBlocks.contains(nblock.getBlock().getTypeId()) || !allowedTopperBlocks.contains(midnblock.getBlock().getTypeId()))
                    && (!allowedBottomBlocks.contains(sblock.getBlock().getTypeId()) || !allowedTopperBlocks.contains(midsblock.getBlock().getTypeId()))
                    && (!allowedBottomBlocks.contains(eblock.getBlock().getTypeId()) || !allowedTopperBlocks.contains(mideblock.getBlock().getTypeId()))
                    && (!allowedBottomBlocks.contains(wblock.getBlock().getTypeId()) || !allowedTopperBlocks.contains(midwblock.getBlock().getTypeId()))
                    && (!allowedBottomBlocks.contains(midnblock.getBlock().getTypeId()) || !allowedTopperBlocks.contains(topnblock.getBlock().getTypeId()))
                    && (!allowedBottomBlocks.contains(midsblock.getBlock().getTypeId()) || !allowedTopperBlocks.contains(topsblock.getBlock().getTypeId()))
                    && (!allowedBottomBlocks.contains(mideblock.getBlock().getTypeId()) || !allowedTopperBlocks.contains(topeblock.getBlock().getTypeId()))
                    && (!allowedBottomBlocks.contains(midwblock.getBlock().getTypeId()) || !allowedTopperBlocks.contains(topwblock.getBlock().getTypeId()))) {

                List<Double> xloc = Arrays.asList(
                        l.getX() - 2.0,
                        l.getX() - 1.0,
                        l.getX(),
                        l.getX() + 1.0,
                        l.getX() + 2.0
                );

                List<Double> zloc = Arrays.asList(
                        l.getZ() - 2.0,
                        l.getZ() - 1.0,
                        l.getZ(),
                        l.getZ() + 1.0,
                        l.getZ() + 2.0
                );

                portalloc.put(p, new XZLocation(xloc, zloc, l));
                Block block = p.getLocation().getWorld().getBlockAt(p.getLocation());
                block.setType(Material.AIR);
            }
        }
    }

    @EventHandler
    protected void onPortalTpLeave(PlayerTeleportEvent event) {
        Player p = event.getPlayer();
        if (portalloc.containsKey(p)) {
            XZLocation coordloc = portalloc.get(p);
            List<Double> xloc = coordloc.x;
            List<Double> zloc = coordloc.z;
            if (!xloc.contains(event.getTo().getX()) && !zloc.contains(event.getTo().getZ())) {
                coordloc.loc.getBlock().setType(Material.FIRE);
                portalloc.remove(p);
            }
        }
    }

    @EventHandler
    protected void onPortalLeave(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (portalloc.containsKey(player)) {
            XZLocation coordloc = portalloc.get(player);
            if (coordloc.lowestX > event.getTo().getX() || coordloc.highestX < event.getTo().getX() || coordloc.lowestZ > event.getTo().getZ() || coordloc.highestZ < event.getTo().getZ()) {
                coordloc.loc.getBlock().setType(Material.FIRE);
                portalloc.remove(player);
            }
        }
    }

    static class XZLocation {

        final List<Double> x;
        final List<Double> z;

        final double lowestX;
        final double highestX;

        final double lowestZ;
        final double highestZ;

        final Location loc;

        public XZLocation(List<Double> x, List<Double> z, Location loc) {
            this.x = x;
            this.z = z;

            this.loc = loc;

            this.lowestX = x.get(0);
            this.highestX = x.get(4);

            this.lowestZ = z.get(0);
            this.highestZ = z.get(4);
        }

    }
}
