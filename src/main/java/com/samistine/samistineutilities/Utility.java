package com.samistine.samistineutilities;

import com.samistine.samistineutilities.utils.Pair;
import java.util.Set;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.Validate;
import org.bukkit.Server;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Samuel Seidel
 */
public abstract class Utility {

    private final JavaPlugin plugin = SamistineUtilities.getPlugin(SamistineUtilities.class);

    private final String name;
    private final String desc;
    private final String[] commands;

    private final Logger logger;

    private final Set<Listener> listeners;
    private final Set<Pair<String, CommandExecutor>> commandHandles;

    public Utility() {
        UtilityInfo annotation = getClass().getAnnotation(UtilityInfo.class);
        Validate.notNull(annotation, "Implementing class must contain UtilityInfo.java anotation");
        this.name = annotation.name();
        this.desc = annotation.desc();
        this.commands = annotation.commands();
        this.logger = new UtilityLogger(this);
        this.listeners = new HashSet<>();
        this.commandHandles = new HashSet<>();
    }

    public final String getName() {
        return this.name;
    }

    public final String getDesc() {
        return desc;
    }

    public Logger getLogger() {
        return logger;
    }

    /**
     * Called when this utility should be enabled.
     * <p>
     * Commands are registered immediately after this method</p>
     */
    protected void onEnable() {
    }

    /**
     * Called when this utility should be disabled.
     */
    protected void onDisable() {
    }

    /**
     * Default logic for registering commands declared in annotations.
     * <br>
     * Note: Even if you override this method you still should/need to declare
     * the commands in the annotations.
     */
    protected void registerCommands() {
        for (String command : commands) {
            if (command != null && !command.isEmpty()) {
                if (this instanceof CommandExecutor) {
                    CommandExecutor exec = (CommandExecutor) this;
                    registerCommand(command, exec);
                } else {
                    getLogger().log(Level.WARNING, "Attempted to register command ''{0}'' but this class does not implement CommandExecutor", command);
                }
            }
        }
    }

//
    //Begin methods other
//
    public final JavaPlugin getPlugin() {
        return plugin;
    }

    public final Server getServer() {
        return plugin.getServer();
    }

    public FileConfiguration getRootConfig() {
        return plugin.getConfig();
    }

    /**
     * All listeners need to be registered via this method to allow them to be
     * properly disabled upon {@link #onDisable()}
     *
     * @param listener the listener to register
     */
    public final void registerListener(Listener listener) {
        Validate.notNull(listener, "You cannot register a null listener");
        listeners.add(listener);
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    /**
     * All listeners should be unregistered via this method to allow them to be
     * removed from internal tracking.
     *
     * @param listener listener to unregister
     * @return true if listener was unregistered as a result of this method
     */
    public final boolean unRegisterListener(Listener listener) {
        if (listeners.remove(listener)) {
            HandlerList.unregisterAll(listener);
            return true;
        }
        return false;
    }

    /**
     * All commands need to be registered via this method to allow them to be
     * properly disabled upon {@link #onDisable()}
     *
     * @param command the command to register
     * @param executor the executor to register the command to
     */
    public final void registerCommand(String command, CommandExecutor executor) {
        Validate.notEmpty(command, "You cannot register null/empty command(s)");
        Validate.notNull(executor, "You cannot register command(s) a null CommandExecutor");
        PluginCommand plm_command = plugin.getCommand(command.replaceFirst("\\/", ""));//todo: if null throw an exception about not being in the plugin config
        plm_command.setExecutor(executor);
        commandHandles.add(Pair.create(command, executor));
    }

    /**
     * Starts the utility.
     * <br>
     * This executes necessary code to get the utility ready to be enabled, then
     * it calls {@link #onEnable()}
     *
     * @param <T>
     * @return the utility, for easy chaining of method calls
     */
    public final <T extends Utility> T start() {
        onEnable();
        registerCommands();
        return (T) this;
    }

    /**
     * Stops the utility.
     * <br>
     * This calls {@link #onDisable()}, then it executes necessary code to fully
     * stop the feature. For example: Unregistering listeners and commands.
     *
     * @param <T>
     * @return the utility, for easy chaining of method calls
     */
    public final <T extends Utility> T stop() {
        onDisable();
        return (T) this;
    }

}
