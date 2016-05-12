package com.samistine.samistineutilities;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Samuel Seidel
 */
public abstract class Utility {

    private JavaPlugin plugin;

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public final Server getServer() {
        return plugin.getServer();
    }

    public PluginCommand getCommand(String name) {
        return plugin.getCommand(name);
    }

    public FileConfiguration getRootConfig() {
        return plugin.getConfig();
    }

    public abstract String getName();

    private Logger logger;

    public Logger getLogger() {
        if (logger == null) {
            this.logger = new UtilityLogger(this);
        }
        return logger;
    }

    /**
     * Called when the root plugin is enabled
     */
    public abstract void onEnable();

    /**
     * Called when the root plugin is disabled
     */
    public void onDisable() {
    }

    public final <T extends Utility> T withPlugin(JavaPlugin plugin) {
        this.plugin = plugin;
        return (T) this;
    }

    public final <T extends Utility> T start() {
        if (plugin != null) {
            onEnable();
        } else {
            plugin.getLogger().log(Level.WARNING, "Could not enable {0}. Attempted to start a utility without first calling #withPlugin(JavaPlugin)", getClass().getSimpleName());
        }
        return (T) this;
    }

    public final <T extends Utility> T stop() {
        onDisable();
        return (T) this;
    }

}
