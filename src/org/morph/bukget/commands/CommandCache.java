package org.morph.bukget.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.parser.ParseException;
import org.morph.bukget.BukGet;

/**
 * The '/bukget cache' Command
 * @author Morphesus
 */
public class CommandCache implements BukGetCommand {
    public static final int PAGE_MAX_ENTRIES = 10;
    
    private CommandSender sender;
    private String[] args;
    
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
        this.sender = sender;
        this.args   = args;
        
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
        // -- Update the local name list cache
        if (cacheType.equalsIgnoreCase("names")) {
            if (this.sender.hasPermission("bukget.cache.update.names")) {
                try {
                    BukGet.instance.getManager().updateLocalNameCache();
                    return BukGetCommandResult.SUCCESS;
                } catch (IOException ex) {
                    BukGet.instance.getLogger().log(Level.SEVERE, null, ex);
                    return BukGetCommandResult.FATAL_ERROR;
                }
            } else {
                return BukGetCommandResult.NO_PERMISSION;
            }
        }
        
        // -- Full update (includes all plugin details)
        else if (cacheType.equalsIgnoreCase("full")) {
            if (this.sender.hasPermission("bukget.cache.update.full")) {
                try {
                    BukGet.instance.getManager().updateLocalDataCache();
                    return BukGetCommandResult.SUCCESS;
                } catch (IOException ex) {
                    BukGet.instance.getLogger().log(Level.SEVERE, null, ex);
                    return BukGetCommandResult.FATAL_ERROR;
                } catch (ParseException ex) {
                    BukGet.instance.getLogger().log(Level.SEVERE, null, ex);
                    return BukGetCommandResult.FATAL_ERROR;
                }
            } else {
                return BukGetCommandResult.NO_PERMISSION;
            }
        }
        
        // -- Unknown Cache Type
        else {
            return BukGetCommandResult.ILLEGAL_ARGUMENT;
        }
    }
    
    private BukGetCommandResult delete(String cacheType) {
        // -- Update the local name list cache
        if (cacheType.equalsIgnoreCase("names")) {
            if (this.sender.hasPermission("bukget.cache.delete.names")) {
                boolean result = BukGet.instance.getManager().deleteLocalNameCache();
                if (result) {
                    this.sender.sendMessage("Cache is deleted");
                } else {
                    this.sender.sendMessage("Could not delete cache");
                }

                return BukGetCommandResult.SUCCESS;
            } else {
                return BukGetCommandResult.NO_PERMISSION;
            }
        }
        
        // -- Full update (includes all plugin details)
        else if (cacheType.equalsIgnoreCase("full")) {
            if (this.sender.hasPermission("bukget.cache.delete.full")) {
                boolean result = BukGet.instance.getManager().deleteLocalDataCache();
                if (result) {
                    this.sender.sendMessage("Cache is deleted");
                } else {
                    this.sender.sendMessage("Could not delete cache");
                }

                return BukGetCommandResult.SUCCESS;
            } else {
                return BukGetCommandResult.NO_PERMISSION;
            }
        }
        
        // -- Unknown Cache Type
        else {
            return BukGetCommandResult.ILLEGAL_ARGUMENT;
        }
    }
    
    private BukGetCommandResult list() {
        // -- Validate and load plugin cache
        List<String> pluginList;
        try {
            pluginList = BukGet.instance.getManager().getLocalNameCache().getPluginNames();
        } catch (IOException ex) {
            BukGet.instance.getLogger().log(Level.SEVERE, null, ex);
            return BukGetCommandResult.FATAL_ERROR;
        }
        
        // -- Plugin List exists and is valid
        if (pluginList != null && pluginList.size() > 0) {
            List<String> view = new ArrayList<String>();
            
            // -- Calculate pages count
            int pages = pluginList.size() / PAGE_MAX_ENTRIES;
            if ((pages * PAGE_MAX_ENTRIES) < pluginList.size()) {
                pages++;
            }
            
            // -- Which page the user will see?
            int showPage = 0;
            if (args.length > 2) {
                try {
                    showPage = Integer.parseInt(args[2]) - 1;
                } catch (NumberFormatException e) {
                    showPage = 0;
                }
            }
            
            // -- Check page existence
            if (showPage < 0) {
                showPage = 0;
            } else if (showPage > pages) {
                showPage = pages - 1;
            }
            
            // -- Start- and Endpoint
            int start = showPage * PAGE_MAX_ENTRIES;
            int end   = start + PAGE_MAX_ENTRIES;
            
            // -- Put header in message array
            view.add(String.format("== Plugins %d to %d (from %d Plugins) ==", start+1, end, pluginList.size()));
            
            // -- Load plugin names into message array
            for (int i = start; i < start + PAGE_MAX_ENTRIES; i++) {
                if (i < pluginList.size()) {
                    view.add("- " + pluginList.get(i));
                }
            }
            
            // -- Send the message array
            String[] toSend = new String[view.size()];
            view.toArray(toSend);
            
            this.sender.sendMessage(toSend);
            
            return BukGetCommandResult.SUCCESS;
        } else {
            BukGet.instance.getLogger().severe("Could not get the plugin list. Please delete the name_cache.dat file to solve this issue!");
            return BukGetCommandResult.FATAL_ERROR;
        }
    }
}
