package com.samistine.samistineutilities.features;

import com.samistine.mcplugins.api.FeatureInfo;
import com.samistine.mcplugins.api.SFeatureListener;
import com.samistine.samistineutilities.SamistineUtilities;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

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
    private FireworkTracking fireworkTracking = new FireworkTracking();

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

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    protected void blockExcessiveFireworks(FireworkExplodeEvent event) {
        fireworkTracking.handle(event);
    }

    static class FireworkTracking {

        private Map<Chunk, Long> map = new WeakHashMap<>();

        private void evict() {
            long now = System.currentTimeMillis();
            Iterator<Map.Entry<Chunk, Long>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Chunk, Long> entry = iterator.next();
                if (now >= entry.getValue()) iterator.remove();
            }
        }

        public void handle(FireworkExplodeEvent event) {
            Chunk chunk = event.getEntity().getLocation().getChunk();

            evict();

            boolean canExplodeHere = !map.containsKey(chunk);

            if (canExplodeHere) {
                int effectSize = event.getEntity().getFireworkMeta().getEffectsSize();
                int longetivity = (int) Math.ceil(effectSize / 10.0);
                map.put(chunk, System.currentTimeMillis() + longetivity *1000);
            } else {
                event.setCancelled(true);
            }
        }

    }
}
