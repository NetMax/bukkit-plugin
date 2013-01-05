package org.morph.bukget;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.morph.bukget.commands.BukGetCommand;
import org.morph.bukget.commands.BukGetCommandManager;

/**
 *
 * @author Morphesus
 */
public class BukGet extends JavaPlugin {
    public static boolean DEBUG_MODE = true;
    public static BukGet         instance;
    
    // Core objects
    private BukGetManager        manager;
    private BukGetDaemon         daemon;
    private BukGetCommandManager cmdManager;
    
    // Files
    public static final String BUKGET_NAME_CACHE = "name_cache.dat";
    public static final String BUKGET_DATA_CACHE = "data_cache.dat";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("bukget")) {
            if (cmdManager.isRegistered(label)) {
                final BukGetCommand cmd = cmdManager.getCommand(label);
                if (sender.hasPermission(cmd.getPermission())) {
                    return cmd.exec(this, sender, args);
                } else {
                    sender.sendMessage(ChatColor.RED + "Sorry, you don't have the permission to use this command! :(");
                    return true;
                }
            } else {
                sender.sendMessage("Sorry, I could not find this command ... :(");
                return true;
            }
        } else {
            sender.sendMessage("Usage: /bukget <cache|plugin> [params]");
            return true;
        }
    }

    @Override
    public void onEnable() {
        instance = this;
        
        // Create important files / directories
        this.getDataFolder().mkdirs();
        
        // Create core objects
        this.manager    = new BukGetManager();
        this.daemon     = new BukGetDaemon();
        this.cmdManager = new BukGetCommandManager();
        
        // Startup daemon
        this.daemon.setDaemon(true);
        this.daemon.start();
    }

    @Override
    public void onDisable() {
        daemon.interrupt();
    }
    
    public BukGetManager getManager() {
        return this.manager;
    }
    
    public BukGetDaemon getDaemon() {
        return this.daemon;
    }
    
    public BukGetCommandManager getCmdManager() {
        return this.cmdManager;
    }
    
    public static void debug(String msg) {
        if (DEBUG_MODE) {
            BukGet.instance.getLogger().info(String.format("[DEBUG] %s", msg));
        }
    }
}
