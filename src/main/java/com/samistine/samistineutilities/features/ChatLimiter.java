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

import com.samistine.mcplugins.api.FeatureInfo;
import com.samistine.mcplugins.api.SFeature;
import com.samistine.samistineutilities.SamistineUtilities;
import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author Samuel Seidel
 */
@FeatureInfo(
        name = "ChatLimiter",
        desc = "Limits how often a player can chat and issue commands respectively",
        category = "Moderation"
)
public final class ChatLimiter extends SFeature implements Listener {

    private final Map<Player, PlayerDataHolder> data = new ConcurrentHashMap<>();

    public ChatLimiter(SamistineUtilities main) {
        super(main);
    }

    private int min_players = 8;
    private int delay = 2;
    private boolean block_dupe_messages = true;
    private boolean block_dupe_commands = false;
    private boolean block_until_moved = false;
    private String message = "You must wait {0} seconds between messages! ({1} remaining)";
    private String message_move_chat = "&4You must move before chatting!";
    private String message_move_comm = "&4You must move before sending commands!";
    private String message_dupl_chat = "&4Can not send duplicate messages!&4You must move before chatting!;";
    private String message_dupl_comm = "&4Can not send duplicate commands!";

    @Override
    protected void onEnable() {
        this.min_players = getConfig().getInt("min_players", min_players);
        this.delay = getConfig().getInt("delay", delay);
        this.block_dupe_messages = getConfig().getBoolean("block_dupe_messages", block_dupe_messages);
        this.block_dupe_commands = getConfig().getBoolean("block_dupe_commands", block_dupe_commands);
        this.block_until_moved = getConfig().getBoolean("block_until_moved", block_until_moved);
        this.message = ChatColor.translateAlternateColorCodes('&', getConfig().getString("message", message));
        this.message_move_chat = ChatColor.translateAlternateColorCodes('&', getConfig().getString("message_move_chat", message_move_chat));
        this.message_move_comm = ChatColor.translateAlternateColorCodes('&', getConfig().getString("message_move_comm", message_move_comm));
        this.message_dupl_chat = ChatColor.translateAlternateColorCodes('&', getConfig().getString("message_dupl_chat", message_dupl_chat));
        this.message_dupl_comm = ChatColor.translateAlternateColorCodes('&', getConfig().getString("message_dupl_comm", message_dupl_comm));
        getServer().getOnlinePlayers().forEach(player -> data.put(player, new PlayerDataHolder()));//This should be safe for parallel streams
        getServer().getPluginManager().registerEvents(this, featurePlugin);
    }

    @Override
    protected void onDisable() {
        HandlerList.unregisterAll(this);
        data.clear();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    protected void onPlayerJoin(PlayerJoinEvent e) {
        data.put(e.getPlayer(), new PlayerDataHolder());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    protected void onPlayerQuit(PlayerQuitEvent e) {
        data.remove(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    protected void onPlayerMove(PlayerMoveEvent e) {
        getPlayerHolder(e.getPlayer()).hasMoved = true;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    protected void onPlayerChatEventMonitor(AsyncPlayerChatEvent e) {
        PlayerDataHolder holder = getPlayerHolder(e.getPlayer());
        holder.lastMessage = e.getMessage();
        holder.lastMessageTime = System.currentTimeMillis() / 1000L;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    protected void onPlayerCommandPreprocessMonitor(PlayerCommandPreprocessEvent e) {
        PlayerDataHolder holder = getPlayerHolder(e.getPlayer());
        holder.lastCommand = e.getMessage();
        holder.lastMessageTime = System.currentTimeMillis() / 1000L;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    protected void onPlayerChatEventLow(AsyncPlayerChatEvent e) {
        final long current = System.currentTimeMillis() / 1000L;
        Player player = e.getPlayer();

        if (min_players >= getServer().getOnlinePlayers().size() || player.hasPermission("samistine.chatlimiter.ignore")) {
            return;
        }

        PlayerDataHolder holder = getPlayerHolder(player);

        if (block_until_moved && !holder.hasMoved) {
            e.setCancelled(true);
            player.sendMessage(message_move_chat);
        } else if ((current - holder.lastMessageTime) < delay) {
            e.setCancelled(true);
            player.sendMessage(getTimeWarning(current, holder.lastMessageTime));
        } else if (block_dupe_messages && checkIfDupeMessage(e.getMessage(), holder)) {
            e.setCancelled(true);
            player.sendMessage(message_dupl_chat);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    protected void onPlayerCommandPreprocessLow(PlayerCommandPreprocessEvent e) {
        final long current = System.currentTimeMillis() / 1000L;
        Player player = e.getPlayer();

        if (min_players >= getServer().getOnlinePlayers().size() || player.hasPermission("samistine.chatlimiter.ignore")) {
            return;
        }

        PlayerDataHolder holder = getPlayerHolder(player);

        if (block_until_moved && !holder.hasMoved) {
            e.setCancelled(true);
            player.sendMessage(message_move_comm);
        } else if ((current - holder.lastMessageTime) < delay) {
            e.setCancelled(true);
            player.sendMessage(getTimeWarning(current, holder.lastMessageTime));
        } else if (block_dupe_commands && checkIfDupeCommand(holder, e.getMessage())) {
            e.setCancelled(true);
            player.sendMessage(message_dupl_comm);
        }
    }

    private boolean checkIfDupeMessage(String msg, PlayerDataHolder holder) {
        return msg.equalsIgnoreCase(holder.lastMessage);
    }

    private boolean checkIfDupeCommand(PlayerDataHolder holder, String cmd) {
        return cmd.equalsIgnoreCase(holder.lastCommand);
    }

    private String getTimeWarning(long current, long holderTime) {
        return MessageFormat.format(message, delay, (delay - (current - holderTime)));
    }

    private PlayerDataHolder getPlayerHolder(Player p) {
        PlayerDataHolder holder = data.get(p);
        if (holder == null) {
            getLogger().log(Level.WARNING, "Player {0} was not in HashMap, Reload?", p);
            holder = new PlayerDataHolder();
            data.put(p, holder);
        }
        return holder;
    }

    private static class PlayerDataHolder {

        boolean hasMoved = false;
        long lastMessageTime = 0L;
        String lastMessage = "";
        String lastCommand = "";

    }

}
