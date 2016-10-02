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
package com.samistine.samistineutilities.features.permissionutils;

import com.samistine.samistineutilities.SamistineUtilities;
import com.samistine.mcplugins.api.SFeature;
import com.samistine.mcplugins.api.FeatureInfo;
import com.samistine.mcplugins.api.utils.BukkitUtils;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 *
 * @author Samuel Seidel
 */
@FeatureInfo(name = "PermissionUtils", desc = "Useful utilities related to permissions")
public final class PermissionUtils extends SFeature implements Listener {

    private final List<Config2> trackingList = new ArrayList<>();

    @Override
    protected void onDisable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void onEnable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public PermissionUtils(SamistineUtilities main) {
        super(main, "PermissionUtils", "Useful utilities related to permissions");
        getLogger().setLevel(Level.ALL);
        //Override default permission messages with custom specified ones
        ConfigurationSection permOverrides = getConfig().getConfigurationSection("PermissionsOverride");
        if (permOverrides != null) {

            ConfigurationSection pluginYML = permOverrides.getConfigurationSection("PluginYML");
            if (pluginYML != null) {
                changePermissionMessages(
                        BukkitUtils.configSectionStringValues(permOverrides.getConfigurationSection("PluginYML"))
                );
            }

            ConfigurationSection tracking = permOverrides.getConfigurationSection("Tracking");
            if (tracking != null) {
                for (String command : tracking.getValues(false).keySet()) {
                    String permission = tracking.getString(command + ".permission");
                    String permission_message = ChatColor.translateAlternateColorCodes('&', tracking.getString(command + ".permission-message"));
                    if (permission != null && permission_message != null) {
                        getLogger().log(Level.FINE, "Tracking command for player permission message, command={0}", command);
                        trackingList.add(new Config2(command, permission, permission_message));
                    }
                }
            }
        }
    }

    private void changePermissionMessages(Map<String, String> commandAndReplacementPermissionMessage) {
        commandAndReplacementPermissionMessage.entrySet().stream().forEach((entry) -> {
            String commandName = entry.getKey();
            String replacementPermissionMessage = entry.getValue();
            PluginCommand command = Bukkit.getServer().getPluginCommand(commandName);
            if (command != null) {
                command.setPermissionMessage(replacementPermissionMessage);
                getLogger().log(Level.FINE, "Replaced permission message for command {0} to {1}", new Object[]{commandName, replacementPermissionMessage});
            } else {
                getLogger().log(Level.SEVERE, "Could not get command {0}", commandName);
            }
        });
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
//        String base = event.getMessage().substring(1);
//        Optional<Config2> c2 = trackingList.stream().map(Config2::trackingList).filter(tracking -> base.startsWith(tracking.command)).findFirst();
//        
//        if (c2.isPresent()) {
//            String matched = c2.get()
//        }
//
//        String[] commandAndArgs = base.split(" ", 2);
//        String command = commandAndArgs[0];
//        String[] args = commandAndArgs.length > 1 ? commandAndArgs[1].split(" ") : null;
//
//        if (c2 != null && !event.getPlayer().hasPermission(c2.permission)) {
//            String message = MessageFormat.format(c2.permission_message, command, args != null ? String.join(" ", args) : null);
//            event.getPlayer().sendMessage(message);
//            event.setCancelled(true);
//        }
    }

}
