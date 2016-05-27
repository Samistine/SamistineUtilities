package com.samistine.samistineutilities.utils.annotations.command;

import com.samistine.samistineutilities.api.SCommandExecutor;
import com.samistine.samistineutilities.utils.annotations.command.exception.CommandException;
import com.samistine.samistineutilities.utils.annotations.command.exception.IllegalSenderException;
import com.samistine.samistineutilities.utils.annotations.command.exception.PermissionException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Used internally to call the method that should handle a command, this would
 * normally call the onCommand() method.
 *
 * @author Jacek Kuzemczak
 */
class PluginCommand extends Command implements PluginIdentifiableCommand {
    
    private Plugin plugin;
    private SCommandExecutor handler;
    private Method handlerMethod;
    private HashMap<String, PluginSubCommand> subCommands;
    private String[] tabCompletion;
    
    public PluginCommand(Plugin plugin, SCommandExecutor handler, Method handlerMethod, String[] names, String description, String usage, String[] tabCompletion) {
        super(names[0], description, "/<command> " + usage, Arrays.asList(names));
        
        this.plugin = plugin;
        this.handler = handler;
        this.handlerMethod = handlerMethod;
        this.subCommands = new HashMap<>();
        this.tabCompletion = tabCompletion;
    }
    
    protected void registerSubCommandHandler(String name, PluginSubCommand handler) {
        this.subCommands.put(name, handler);
    }
    
    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }
    
    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        try {
            Method handlerMethod_;
            SCommandExecutor handler_;
            
            if (args.length > 0 && this.subCommands.containsKey(args[0])) {
                handlerMethod_ = this.subCommands.get(args[0]).handlerMethod;
                handler_ = this.subCommands.get(args[0]).handler;
                
                String[] subArgs = new String[args.length - 1];
                System.arraycopy(args, 1, subArgs, 0, subArgs.length);
                
                args = subArgs;
            } else {
                handlerMethod_ = this.handlerMethod;
                handler_ = this.handler;
            }
            
            handlerMethod_.setAccessible(true);
            handlerMethod_.invoke(handler_, sender, label, args);
        } catch (CommandException ex) {
            if (ex instanceof PermissionException) {
                sender.sendMessage("");
            } else if (ex instanceof IllegalSenderException) {
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return true;
    }
    
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        Method handlerMethod_;
        SCommandExecutor handler_;
        String[] tabCompletion_;
        
        if (args.length > 1 && this.subCommands.containsKey(args[0])) {
            handlerMethod_ = this.subCommands.get(args[0]).handlerMethod;
            handler_ = this.subCommands.get(args[0]).handler;
            tabCompletion_ = this.subCommands.get(args[0]).tabCompletion;
            
            String[] subArgs = new String[args.length - 1];
            System.arraycopy(args, 1, subArgs, 0, subArgs.length);
            
            args = subArgs;
        } else {
            handlerMethod_ = this.handlerMethod;
            handler_ = this.handler;
            tabCompletion_ = this.tabCompletion;
        }
        
        ArrayList<String> completions = new ArrayList<>();
        
        boolean empty = args[args.length - 1].isEmpty();
        
        if (args.length <= tabCompletion_.length) {
            String tab = tabCompletion_[args.length - 1];
            String last = args[args.length - 1].toLowerCase();
            
            if (tab.equalsIgnoreCase("<online_player>")) {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    String playerName = player.getName();
                    String testName = playerName.toLowerCase();
                    
                    if (empty || testName.startsWith(last)) {
                        completions.add(playerName);
                    }
                }
            } else if (tab.equalsIgnoreCase("<player>")) {
                for (OfflinePlayer player : plugin.getServer().getOfflinePlayers()) {
                    String playerName = player.getName();
                    String testName = playerName.toLowerCase();
                    
                    if (empty || testName.startsWith(last)) {
                        completions.add(playerName);
                    }
                }
            } else if (tab.startsWith("[") && tab.endsWith("]")) {
                try {
                    Method tabHandler = handlerMethod_.getDeclaringClass().getMethod(tab.substring(1, tab.length() - 1), CommandSender.class, String[].class);
                    
                    if (tabHandler.getReturnType().equals(List.class)) {
                        for (String value : (List<String>) tabHandler.invoke(handler_, sender, args)) {
                            String testValue = value.toLowerCase();
                            
                            if (empty || testValue.startsWith(last)) {
                                completions.add(value);
                            }
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            } else {
                for (String value : tab.split("\\|")) {
                    String testValue = value.toLowerCase();
                    
                    if (empty || testValue.startsWith(last)) {
                        completions.add(value);
                    }
                }
            }
        }
        
        return completions;
    }
    
}
