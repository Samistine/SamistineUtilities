package com.samistine.samistineutilities;

import java.util.logging.Level;
import org.bukkit.Server;
import org.bukkit.command.PluginCommand;
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

    public abstract void onEnable();

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
