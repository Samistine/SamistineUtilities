package com.samistine.samistineutilities.utils.annotations.command;

import com.samistine.samistineutilities.api.SCommandExecutor;
import com.samistine.samistineutilities.utils.BukkitUtils;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import com.samistine.samistineutilities.utils.ReflectionUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Used to manage the commands for this plugin.
 *
 * @author Jacek Kuzemczak
 * @author Samuel Seidel
 */
public final class CommandManager {

    private final Plugin plugin;
    private final Map<SCommandExecutor, PluginCommand> mappings = new HashMap<>();

    private CommandMap commandMap;

    /**
     * @param plugin The plugin that this manager is for.
     */
    public CommandManager(Plugin plugin) {
        this.plugin = plugin;

        try {
            this.commandMap = ReflectionUtils.getFieldValue(SimplePluginManager.class, "commandMap", CommandMap.class, plugin.getServer().getPluginManager());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean registerCommand(PluginCommand command) {
        return this.commandMap.register(plugin.getDescription().getName(), command);
    }

    /**
     * Registers a new command executor.
     *
     * @param executor	The command executor to register.
     * @throws CommandRegistrationException if the registration fails
     */
    public void registerCommandExecutor(SCommandExecutor executor) {
        @SuppressWarnings("unchecked")
        Class<SCommandExecutor> cls = (Class<SCommandExecutor>) executor.getClass();

        for (Method method : cls.getDeclaredMethods()) {
            Command commandInfo = method.getAnnotation(Command.class);
            CommandTabCompletion tabInfo = method.getAnnotation(CommandTabCompletion.class);

            if (commandInfo != null) {
                if (!method.getReturnType().equals(Void.TYPE)) {
                    throw new CommandRegistrationException("Incorrect return type for command method " + method.getName() + " in " + cls.getName());
                } else if (!Arrays.equals(method.getParameterTypes(), new Class<?>[]{CommandSender.class, String.class, String[].class})) {
                    throw new CommandRegistrationException("Incorrect arguments for command method " + method.getName() + " in " + cls.getName());
                } else {
                    PluginCommand command = new PluginCommand(plugin, executor, method, commandInfo.names(), commandInfo.description(), commandInfo.usage(), ((tabInfo == null) ? new String[0] : tabInfo.value()));

                    if (!this.registerCommand(command)) {
                        throw new CommandRegistrationException("Failed to register command for method " + method.getName() + " in " + cls.getName());
                    } else {
                        mappings.put(executor, command);
                    }
                }
            }
        }

        for (Method method : cls.getDeclaredMethods()) {
            SubCommand subCommandInfo = method.getAnnotation(SubCommand.class);
            CommandTabCompletion tabInfo = method.getAnnotation(CommandTabCompletion.class);

            if (subCommandInfo != null) {
                if (!method.getReturnType().equals(Void.TYPE)) {
                    throw new CommandRegistrationException("Incorrect return type for command method " + method.getName() + " in " + cls.getName());
                } else if (!Arrays.equals(method.getParameterTypes(), new Class<?>[]{CommandSender.class, String.class, String[].class})) {
                    throw new CommandRegistrationException("Incorrect arguments for command method " + method.getName() + " in " + cls.getName());
                } else {
                    PluginCommand parent = (PluginCommand) this.commandMap.getCommand(subCommandInfo.parent());

                    if (parent == null) {
                        throw new CommandRegistrationException("Attempted to register sub-command of " + subCommandInfo.parent() + " before main handler.");
                    }

                    parent.registerSubCommandHandler(subCommandInfo.name(), new PluginSubCommand(executor, method, ((tabInfo == null) ? new String[0] : tabInfo.value())));
                }
            }
        }
    }

    private void unRegisterCommand(PluginCommand cmd) {
        try {
            Map<String, org.bukkit.command.Command> knownCommands = ReflectionUtils.getFieldValue(commandMap.getClass(), "knownCommands", Map.class, commandMap);
            knownCommands.remove(cmd.getName()).unregister(commandMap);

            //Removes other references to the command, such as "pluginname:command"
            for (Iterator<Map.Entry<String, org.bukkit.command.Command>> it = knownCommands.entrySet().iterator(); it.hasNext();) {
                Map.Entry<String, org.bukkit.command.Command> entry = it.next();
                if (entry.getValue() == cmd) {
                    it.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        BukkitUtils.unregisterHelpTopic(cmd);
    }

    //TODO: Add error handling
    public void unRegisterCommandExecutor(SCommandExecutor executor) {
        PluginCommand pluginCommand = mappings.get(executor);
        unRegisterCommand(pluginCommand);
    }

}
