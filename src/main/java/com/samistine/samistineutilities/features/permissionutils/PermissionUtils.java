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

import com.samistine.samistineutilities.api.SFeature;
import com.samistine.samistineutilities.api.SListener;
import com.samistine.samistineutilities.api.objects.FeatureInfo;
import com.samistine.samistineutilities.utils.BukkitUtils;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 *
 * @author Samuel Seidel
 */
@FeatureInfo(name = "PermissionUtils", desc = "Useful utilities related to permissions")
public final class PermissionUtils extends SFeature implements SListener {

    private final Map<String, Config2> trackingMap = new HashMap<>();

    public PermissionUtils() {
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
                        trackingMap.put(command, new Config2(permission, permission_message));
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
        String[] commandAndArgs = event.getMessage().substring(1).split(" ", 2);

        String command = commandAndArgs[0];
        String[] args = commandAndArgs.length > 1 ? commandAndArgs[1].split(" ") : null;
        Config2 c2 = trackingMap.get(command);

        if (c2 != null && !event.getPlayer().hasPermission(c2.permission)) {
            String message = MessageFormat.format(c2.permission_message, command, args != null ? String.join(" ", args) : null);
            event.getPlayer().sendMessage(message);
            event.setCancelled(true);
        }
    }

}
