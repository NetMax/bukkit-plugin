package org.morph.bukget.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.morph.bukget.BukGet;

/**
 *
 * @author Morphesus
 */
public class CommandDebugMode implements BukGetCommand {

    @Override
    public BukGetCommandResult exec(JavaPlugin plugin, CommandSender sender, String[] args) {
        if (!sender.hasPermission(getPermission())) {
            return BukGetCommandResult.NO_PERMISSION;
        }
        
        if (args.length > 1) {
            final String mode = args[1];
            
            if (mode.equalsIgnoreCase("on")) {
                BukGet.setDebugMode(true);
                return BukGetCommandResult.SUCCESS;
            } else if (mode.equalsIgnoreCase("off")) {
                BukGet.setDebugMode(false);
                return BukGetCommandResult.SUCCESS;
            } else {
                return BukGetCommandResult.ILLEGAL_ARGUMENT;
            }
        } else {
            return BukGetCommandResult.INSUFFICIENT_ARGUMENTS;
        }
    }

    @Override
    public String getName() {
        return "debug";
    }

    @Override
    public String getPermission() {
        return "bukget.debug";
    }

    @Override
    public String[] getUsage() {
        return new String[] {
            "/bukget debug <on|off>"
        };
    }
}
