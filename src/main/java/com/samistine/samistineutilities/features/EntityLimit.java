package com.samistine.samistineutilities.features;

import com.samistine.mcplugins.api.FeatureInfo;
import com.samistine.mcplugins.api.SFeatureListener;
import com.samistine.samistineutilities.SamistineUtilities;
import org.bukkit.Chunk;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.world.ChunkLoadEvent;

@FeatureInfo(
        name = "EntityLimit",
        desc = "Clears all entities in a chunk if more than 1000 exist",
        category = "Exploit Prevention"
)
public final class EntityLimit extends SFeatureListener {

    public EntityLimit(SamistineUtilities plugin) {
        super(plugin);
    }

    private int max_tiles = 2000;

    @Override
    protected void onEnable() {
        this.max_tiles = getConfig().getInt("max_entities", max_tiles);
        super.onEnable();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    protected void onBlockEvent(BlockPhysicsEvent event) {
        if (Math.random() < 0.2) {
            BlockState[] tiles = event.getBlock().getChunk().getTileEntities();
            if (tiles.length > max_tiles) {
                Chunk chunk = event.getBlock().getChunk();
                chunk.getWorld().regenerateChunk(chunk.getX(), chunk.getZ());
                getLogger().warning("BlockPhysicsEvent: Regened Chunk");
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    protected void onChunkLoad(ChunkLoadEvent event) {
        BlockState[] tiles = event.getChunk().getTileEntities();
        if (tiles.length > max_tiles) {
            Chunk chunk = event.getChunk();
            chunk.getWorld().regenerateChunk(chunk.getX(), chunk.getZ());
            getLogger().warning("ChunkLoadEvent: Regened Chunk");
        }
    }
}
