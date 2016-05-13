package com.samistine.samistineutilities.physicsdisabler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

final class JCPDCommandExecutor implements CommandExecutor {

    private JCPhysicsDisabler plugin;

    public JCPDCommandExecutor(JCPhysicsDisabler plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 2) {
            switch (args[0]) {
                case "mode":
                    switch (args[1]) {
                        case "on":
                            plugin.setDisabled(1);
                            sender.sendMessage("Block updates are disabled.");
                            break;
                        case "region":
                            plugin.setDisabled(2);
                            sender.sendMessage("Block updates will be disabled in all selected regions.");
                            break;
                        case "off":
                            plugin.setDisabled(0);
                            sender.sendMessage("Everything is turned on.");
                            break;
                        default:
                            return false;
                    }
                    break;
                case "addregion":
                    JCPDRegion r = null;
                    if (args.length == 3) {
                        r = new JCPDRegion(plugin.getServer().getWorld(args[2]));
                    } else if (args.length == 5) {
                        String[] v1 = args[3].split("\\|", 3);
                        String[] v2 = args[4].split("\\|", 3);

                        try {
                            r = new JCPDRegion(plugin.getServer().getWorld(args[2]), Integer.parseInt(v1[0]), Integer.parseInt(v1[1]), Integer.parseInt(v1[2]), Integer.parseInt(v2[0]), Integer.parseInt(v2[1]), Integer.parseInt(v2[2]));
                        } catch (NumberFormatException e) {
                            sender.sendMessage("Invalid parameters. ~~NumberFormatException~~");
                            return true;
                        }
                    }
                    if (r == null) {
                        sender.sendMessage("World does not exist or invalid parameters.");
                    } else if (plugin.existsRegion(args[1])) {
                        sender.sendMessage("A region of the same name already exists.");
                    } else {
                        plugin.addRegion(args[1], r);
                        sender.sendMessage("Region added.");
                    }
                    break;
                case "removeregion":
                    if (args.length == 2) {
                        if (!plugin.existsRegion(args[1])) {
                            sender.sendMessage("Region does not exist.");
                        } else {
                            plugin.removeRegion(args[1]);
                            sender.sendMessage("Region removed.");
                        }
                    } else {
                        return false;
                    }
                    break;
                default:
                    return false;
            }
        } else {
            return false;
        }

        return true;
    }
}