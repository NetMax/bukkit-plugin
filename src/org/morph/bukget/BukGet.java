package org.morph.bukget;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.morph.bukget.data.PluginListCacheFile;

/**
 *
 * @author Morphesus
 */
public class BukGet extends JavaPlugin {
    public static boolean DEBUG_MODE = true;
    
    public static BukGetManager manager;
    public static BukGet instance;
    private BukGetDaemon daemon;
    
    // Files
    public static final String BUKGET_NAME_CACHE = "name_cache.dat";
    public static final String BUKGET_DATA_CACHE = "data_cache.dat";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // -- Root Command
        if (label.equalsIgnoreCase("bukget")) {
            // -- Check for subcommand
            if (args.length > 0) {
                final String subcmd = args[0];
                // -- Parse subcommand
                if (subcmd.equalsIgnoreCase("cache")) {
                    if (args.length > 1) {
                        final String action = args[1];
                        
                        // -- Parse action
                        if (action.equalsIgnoreCase("update") && sender.hasPermission("bukget.cache.update")) {
                            try {
                                if (args.length > 2) {
                                    final String updateType = args[2];
                                    if (updateType.equalsIgnoreCase("names") && sender.hasPermission("bukget.cache.update.names")) { // Update names
                                        sender.sendMessage("BukGet is now updating the local cache ...");
                                        manager.updateLocalCache();
                                        sender.sendMessage("The cache was successfully updated!");

                                        return true;
                                    } else if (updateType.equalsIgnoreCase("full") && sender.hasPermission("bukget.cache.update.names")) {
                                        // Full update
                                        sender.sendMessage(ChatColor.DARK_RED + "BukGet is performing a full update - Your server could lag!");
                                        manager.updateLocalDataCache();
                                        sender.sendMessage("The full-cache was successfully updated!");

                                        return true;
                                    } else {
                                        sender.sendMessage(ChatColor.DARK_RED + "Unknown subcommand or missing permission.");
                                        return true;
                                    }
                                }
                            } catch (IOException ex) {
                                this.getLogger().log(Level.SEVERE, null, ex);
                                
                                sender.sendMessage(ChatColor.DARK_RED + "Ooops, something went wrong :(");
                                sender.sendMessage(ChatColor.DARK_RED + "Please check your server.log and contact the plugin developer!");
                            }
                        } else if (action.equalsIgnoreCase("delete") && sender.hasPermission("bukget.cache.delete")) {
                            File cacheFile = new File(getDataFolder(), BUKGET_NAME_CACHE);
                            if (cacheFile.exists()) {
                                if (cacheFile.delete()) {
                                    sender.sendMessage(ChatColor.DARK_GREEN + "Cache was successfully deleted!");
                                    return true;
                                } else {
                                    sender.sendMessage(ChatColor.DARK_RED + "Something went wrong :/");
                                    return true;
                                }
                            } else {
                                sender.sendMessage(ChatColor.DARK_GREEN + "There is currently no cache file ;)");
                                return true;
                            }
                        } else if (action.equalsIgnoreCase("list") && sender.hasPermission("bukget.cache.list")) {
                            try {
                                PluginListCacheFile cache = manager.getCache();
                                int pluginsPerPage = 10;
                                int pagesCount     = (cache.getPluginNames().size() / pluginsPerPage);
                                int requestedPage  = 1;
                                
                                if (args.length > 2) {
                                    try {
                                        requestedPage = Integer.parseInt(args[2]);
                                    } catch (NumberFormatException e) {
                                        sender.sendMessage("Usage: /bukget cache list [page]");
                                        return true;
                                    }
                                }
                                
                                if (requestedPage > 0 && requestedPage <= pagesCount) {
                                    int startPage = (requestedPage * pluginsPerPage);
                                    int endPage   = startPage + pluginsPerPage;
                                    
                                    List<String> pluginNamesList = new ArrayList<String>();
                                    for (int i = startPage; i < endPage; i++) {
                                        try {
                                            pluginNamesList.add(cache.getPluginNames().get(i));
                                        } catch (IndexOutOfBoundsException e) { }
                                    }
                                    
                                    String[] pluginNames = pluginNamesList.toArray(new String[pluginNamesList.size()]);
                                    
                                    sender.sendMessage(String.format("== Plugin List (Page %d / %d - %d Plugins) ==", requestedPage, pagesCount, cache.getPluginNames().size()));
                                    sender.sendMessage(pluginNames);
                                } else {
                                    sender.sendMessage("Could not find page!");
                                    return true;
                                }
                                
                                return true;
                            } catch (IOException ex) {
                                this.getLogger().log(Level.SEVERE, null, ex);
                                
                                sender.sendMessage(ChatColor.DARK_RED + "Ooops, something went wrong :(");
                                sender.sendMessage(ChatColor.DARK_RED + "Please check your server.log and contact the plugin developer!");
                            }
                        }
                    } else {
                        sender.sendMessage("Usage: /bukget cache <update|delete|list>");
                        return true;
                    }
                }
            } else {
                sender.sendMessage("Usage: /bukget <subcommand> [subcommand params]");
                return true;
            }
        }
        
        return super.onCommand(sender, command, label, args);
    }

    @Override
    public void onEnable() {
        instance = this;
        
        // Create important files / directories
        this.getDataFolder().mkdirs();
        
        // Create core objects
        manager = new BukGetManager();
        daemon  = new BukGetDaemon();
        
        // Startup daemon
        daemon.setDaemon(true);
        daemon.start();
    }
    
    public static void debug(String msg) {
        if (DEBUG_MODE) {
            BukGet.instance.getLogger().info(String.format("[DEBUG] %s", msg));
        }
    }
}
