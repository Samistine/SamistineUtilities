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
package com.samistine.samistineutilities;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Samuel Seidel
 */
public final class SamistineUtilities extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("utils").setExecutor(this);
        saveDefaultConfig();
        for (Features feature : Features.values()) {
            getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[SamistineUtilities]" + ChatColor.GRAY + " Loading " + feature.getName());
            SFeatureWrapper wrapper = feature.getFeatureWrapper();
            wrapper.enable();
        }
    }

    @Override
    public void onDisable() {
        for (Features feature : Features.values()) {
            getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[SamistineUtilities]" + ChatColor.GRAY + " Disabling " + feature.getName());
            SFeatureWrapper wrapper = feature.getFeatureWrapper();
            wrapper.disable();
        }
    }

    public static SamistineUtilities getInstance() {
        return getPlugin(SamistineUtilities.class);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "enable":
                    onEnable();
                    break;
                case "disable":
                    onDisable();
                    break;
            }
        }
        return true;
    }

}
