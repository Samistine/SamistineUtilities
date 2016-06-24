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
package com.samistine.samistineutilities.features;

import com.dictiography.collections.IndexedTreeMap;
import com.samistine.samistineutilities.SamistineUtilities;
import com.samistine.samistineutilities.api.SFeature;
import com.samistine.samistineutilities.utils.Pair;
import com.samistine.samistineutilities.utils.TimeUtils;
import java.text.MessageFormat;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.ChunkLoadEvent;
import com.samistine.samistineutilities.utils.annotations.command.Command;
import com.samistine.samistineutilities.utils.annotations.command.CommandTabCompletion;
import com.samistine.samistineutilities.utils.annotations.command.SubCommand;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

/**
 *
 * @author Samuel Seidel
 */
//@FeatureInfo(name = "FindTiles", desc = "Find laggy chunks", commands = "/findtiles")
public final class FindTiles extends SFeature implements Listener {

    public FindTiles(SamistineUtilities main) {
        super(main,
                "FindTiles",
                "Find laggy chunks"
        );
    }

    private IndexedTreeMap<Long, Pair<Location, Integer>> locs;

    @Override
    protected void onEnable() {
        locs = new IndexedTreeMap<>();
        registerCommand(this);
        getServer().getPluginManager().registerEvents(this, featurePlugin);
    }

    @Override
    protected void onDisable() {
        HandlerList.unregisterAll(this);
        unregisterCommand(this);
        locs.clear();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    protected void onChunkLoad(ChunkLoadEvent e) {
        int tileEntities = e.getChunk().getTileEntities().length;
        if (tileEntities > 500) {
            getLogger().log(Level.WARNING, "{0} Tile Entities detected at x:{1} z:{2}", new Object[]{e.getChunk().getX(), e.getChunk().getZ()});

            getServer().broadcast(MessageFormat.format("{0} Tile Entities detected at x:{1} z:{2}", tileEntities, e.getChunk().getX(), e.getChunk().getZ()),
                    "SamistineUtilities.findtiles");
            Location loc = e.getChunk().getBlock(7, 100, 7).getLocation();
            if (!locs.values().stream().anyMatch(pair -> pair.first.equals(loc))) {
                locs.put(System.currentTimeMillis(), new Pair<>(loc, tileEntities));
            }
        }
    }

    @Command(name = "findtiles", description = "Find chunks that may contain too many tile entities", usage = "[ list | tp (number) ]")
    @CommandTabCompletion("list|tp")
    protected void findTilesCommand(CommandSender sender, String label, String[] args) {
        sender.sendMessage("/findtiles list   List laggy chunks");
        sender.sendMessage("/findtiles tp     TP to last laggy chunk");
    }

    @SubCommand(
            parent = "findtiles",
            name = "list",
            description = "List locations where too many entities were previously detected")
    protected void findTilesTPCommand(CommandSender sender, String label, String[] args) {
        sender.sendMessage(ChatColor.GOLD + "List with no duplicates");
        int i = 0;
        for (Map.Entry<Long, Pair<Location, Integer>> entry : locs.entrySet()) {
            String msg = MessageFormat.format(ChatColor.GOLD + "{0}: {1} Tile Entities detected {2} ago", i, entry.getValue().second, TimeUtils.millisToLongDHMS(System.currentTimeMillis() - entry.getKey()));
            sender.sendMessage(msg);
            i++;
        }
    }

    @SubCommand(
            parent = "findtiles",
            name = "tp",
            usage = "(number)", min = 1,
            description = "Teleport to a location where too many entities have previously been detected")
    protected void findTilesListCommand(CommandSender sender, String label, String[] args) {
        try {
            Map.Entry<Long, Pair<Location, Integer>> entry = locs.exactEntry(Integer.parseInt(args[0]));
            String msg = MessageFormat.format("{0} Tile Entities detected here {1} ago", entry.getValue().second, TimeUtils.millisToLongDHMS(System.currentTimeMillis() - entry.getKey()));
            sender.sendMessage(msg);
            if (sender instanceof Player) ((Player) sender).teleport(entry.getValue().first);
        } catch (NumberFormatException ex) {
            sender.sendMessage(ChatColor.RED + args[0] + " is not an integer.");
        } catch (Exception ex) {
            sender.sendMessage("Error occured: " + ex.toString());
        }
    }

}
