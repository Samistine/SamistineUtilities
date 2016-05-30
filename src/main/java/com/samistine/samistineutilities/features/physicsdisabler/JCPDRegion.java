package com.samistine.samistineutilities.features.physicsdisabler;

import org.bukkit.Location;
import org.bukkit.World;

public final class JCPDRegion {

    private final World world;
    private final boolean v;
    private final int minX;
    private final int minY;
    private final int minZ;
    private final int maxX;
    private final int maxY;
    private final int maxZ;

    public JCPDRegion(World world) {
        this.world = world;
        this.v = false;
        this.minX = 0;
        this.minY = 0;
        this.minZ = 0;
        this.maxX = 0;
        this.maxY = 0;
        this.maxZ = 0;
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.world.hashCode();
        if (v) {
            hash = 97 * hash + this.minX;
            hash = 97 * hash + this.minY;
            hash = 97 * hash + this.minZ;
            hash = 97 * hash + this.maxX;
            hash = 97 * hash + this.maxY;
            hash = 97 * hash + this.maxZ;
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final JCPDRegion other = (JCPDRegion) obj;

        return world == other.world && (!v
                || minX == other.minX && maxX == other.maxX
                && minY == other.minY && maxY == other.maxY
                && minZ == other.minZ && maxZ == other.maxZ);
    }

}
