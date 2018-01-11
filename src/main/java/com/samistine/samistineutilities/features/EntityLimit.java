package com.samistine.samistineutilities.features;

import com.samistine.mcplugins.api.FeatureInfo;
import com.samistine.mcplugins.api.SFeatureListener;
import com.samistine.samistineutilities.SamistineUtilities;
import java.util.List;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.scheduler.BukkitRunnable;

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

    @EventHandler(ignoreCancelled = true)
    protected void removeDroppedEntitiesOnChunkUnload(ChunkUnloadEvent event) {
        for (Entity entity : event.getChunk().getEntities()) {
            if (entity.getType() == EntityType.DROPPED_ITEM) {
                entity.remove();
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    protected void blockExcessiveFireworks(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem().getType() == Material.FIREWORK) {

                for (Entity entity : event.getClickedBlock().getChunk().getEntities()) {
                    if (entity.getType() == EntityType.FIREWORK) {
                        event.setCancelled(true);
                    }
                }

            }
        }
    }
}
