package com.samistine.samistineutilities.features.physicsdisabler;

import com.samistine.mcplugins.api.utils.annotations.command.Command;

import org.bukkit.command.CommandSender;

final class JCPDCommandExecutor {

    private final JCPhysicsDisabler main;

    public JCPDCommandExecutor(JCPhysicsDisabler plugin) {
        this.main = plugin;
    }

    @Command(name = "disablephysics", description = "Disable physics in all or specific areas", usage = "[ mode [on|off|region] | addregion (regionname) (worldname) [(x)|(y)|(z) (x)|(y)|(z)] | removeregion (regionname) ]")
    public void onCommand(CommandSender sender, String label, String[] args) {
        if (args.length >= 2) {
            switch (args[0]) {
                case "mode":
                    switch (args[1]) {
                        case "on":
                            main.setMode(1);
                            sender.sendMessage("Block updates are disabled.");
                            return;
                        case "region":
                            main.setMode(2);
                            sender.sendMessage("Block updates will be disabled in all selected regions.");
                            return;
                        case "off":
                            main.setMode(0);
                            sender.sendMessage("Everything is turned on.");
                            return;
                    }
                    break;
                case "addregion":
                    JCPDRegion r = null;
                    if (args.length == 3) {
                        r = new JCPDRegion(main.getServer().getWorld(args[2]));
                    } else if (args.length == 5) {
                        String[] v1 = args[3].split("\\|", 3);
                        String[] v2 = args[4].split("\\|", 3);

                        try {
                            r = new JCPDRegion(main.getServer().getWorld(args[2]), Integer.parseInt(v1[0]), Integer.parseInt(v1[1]), Integer.parseInt(v1[2]), Integer.parseInt(v2[0]), Integer.parseInt(v2[1]), Integer.parseInt(v2[2]));
                        } catch (NumberFormatException e) {
                            sender.sendMessage("Invalid parameters. ~~NumberFormatException~~");
                            return;
                        }
                    }
                    if (r == null) {
                        sender.sendMessage("World does not exist or invalid parameters.");
                    } else if (main.existsRegion(args[1])) {
                        sender.sendMessage("A region of the same name already exists.");
                    } else {
                        main.addRegion(args[1], r);
                        sender.sendMessage("Region added.");
                    }
                    return;
                case "removeregion":
                    if (args.length == 2) {
                        if (!main.existsRegion(args[1])) {
                            sender.sendMessage("Region does not exist.");
                        } else {
                            main.removeRegion(args[1]);
                            sender.sendMessage("Region removed.");
                        }
                    }
                    return;
            }
        }
        sender.sendMessage("/disablephysics [ mode [on|off|region] | addregion (regionname) (worldname) [(x)|(y)|(z) (x)|(y)|(z)] | removeregion (regionname) ]");
    }

}
