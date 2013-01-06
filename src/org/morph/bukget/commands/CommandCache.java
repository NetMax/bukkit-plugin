package org.morph.bukget.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The '/bukget cache' Command
 * @author Morphesus
 */
public class CommandCache implements BukGetCommand {
    @Override
    public String getName() {
        return "cache";
    }

    @Override
    public String getPermission() {
        return "bukget.cache";
    }

    @Override
    public String[] getUsage() {
        String[] usage = new String[] {
            "/bukget cache <update|delete|list> [params]",
            "|",
            "+-> /bukget cache update [names|full]",
            "+-> /bukget cache delete <names|full>",
            "+-> /bukget cache list"
        };
        
        return usage;
    }

    @Override
    public BukGetCommandResult exec(JavaPlugin plugin, CommandSender sender, String[] args) {
        // -- Check root permission node for bukget.cache
        if (!sender.hasPermission("bukget.cache")) {
            return BukGetCommandResult.NO_PERMISSION;
        }
        
        // -- Check argument length to get the action
        if (args.length > 1) {
            final String action = args[1];
            
            // -- Action: Update
            if (action.equalsIgnoreCase("update")) {
                if (!sender.hasPermission("bukget.cache.update")) {
                    return BukGetCommandResult.NO_PERMISSION;
                }
                
                if (args.length > 2) {
                    final String subAction = args[2];
                    return this.update(subAction);
                } else {
                    return this.update("names");
                }
            }
            
            // -- Action: Delete
            else if (action.equalsIgnoreCase("delete")) {
                if (!sender.hasPermission("bukget.cache.delete")) {
                    return BukGetCommandResult.NO_PERMISSION;
                }
                
                if (args.length > 2) {
                    final String subAction = args[2];
                    return this.delete(subAction);
                } else {
                    return BukGetCommandResult.INSUFFICIENT_ARGUMENTS;
                }
            }
            
            // -- Action: List
            else if (action.equalsIgnoreCase("list")) {
                if (!sender.hasPermission("bukget.cache.list")) {
                    return BukGetCommandResult.NO_PERMISSION;
                }
                
                return this.list();
            }
            
            // -- Action: Unknown
            else {
                sender.sendMessage("Unknown action: " + action);
                return BukGetCommandResult.ILLEGAL_ARGUMENT;
            }
        } else {
            return BukGetCommandResult.INSUFFICIENT_ARGUMENTS;
        }
    }
    
    private BukGetCommandResult update(String cacheType) {
        return BukGetCommandResult.SUCCESS;
    }
    
    private BukGetCommandResult delete(String cacheType) {
        return BukGetCommandResult.SUCCESS;
    }
    
    private BukGetCommandResult list() {
        return BukGetCommandResult.SUCCESS;
    }
}
