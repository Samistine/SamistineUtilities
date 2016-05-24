package com.samistine.samistineutilities.features.physicsdisabler;

import org.bukkit.Location;
import org.bukkit.World;

public final class JCPDRegion {

    private World world;
    private boolean v;
    private int minX;
    private int minY;
    private int minZ;
    private int maxX;
    private int maxY;
    private int maxZ;

    public JCPDRegion(World world) {
        this.world = world;
        this.v = false;
    }

    public JCPDRegion(World world, int x1, int x2, int x3, int y1, int y2, int y3) {
        this.world = world;
        this.v = true;

        if (x1 > y1) {
            minX = y1;
            maxX = x1;
        } else {
            minX = x1;
            maxX = y1;
        }

        if (x2 > y2) {
            minY = y2;
            maxY = x2;
        } else {
            minY = x2;
            maxY = y2;
        }

        if (x3 > y3) {
            minZ = y3;
            maxZ = x3;
        } else {
            minZ = x3;
            maxZ = y3;
        }
    }

    public boolean isInRegion(Location l) {
        return l.getWorld() == world && (!v
                || minX <= l.getBlockX() && l.getBlockX() <= maxX
                && minY <= l.getBlockY() && l.getBlockY() <= maxY
                && minZ <= l.getBlockZ() && l.getBlockZ() <= maxZ);
    }
}
