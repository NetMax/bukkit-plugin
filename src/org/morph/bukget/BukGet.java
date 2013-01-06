package org.morph.bukget;

import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.morph.bukget.commands.BukGetCommand;
import org.morph.bukget.commands.BukGetCommandManager;
import org.morph.bukget.commands.BukGetCommandResult;
import org.morph.bukget.commands.CommandCache;
import org.morph.bukget.commands.CommandDebugMode;

/**
 *
 * @author Morphesus
 */
public class BukGet extends JavaPlugin {
    public static BukGet         instance;
    private static boolean       debugMode = true;
    
    // Core objects
    private BukGetManager        manager;
    private BukGetDaemon         daemon;
    private BukGetCommandManager cmdManager;
    
    // Files
    public static final String BUKGET_NAME_CACHE = "name_cache.dat";
    public static final String BUKGET_DATA_CACHE = "data_cache.dat";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // -- Check root command
        if (label.equalsIgnoreCase("bukget") && args.length > 0) {
            // -- Get subCommand and check existence
            final String subCommand = args[0];
            if (cmdManager.isRegistered(subCommand)) {
                // -- If command is existing, try to execute
                try {
                    // -- Get command and execute
                    final BukGetCommand cmd = (BukGetCommand) cmdManager.getCommand(subCommand).newInstance();
                    BukGetCommandResult res = cmd.exec(this, sender, args);
                    
                    // -- Check Result
                    switch (res) {
                        case SUCCESS:
                            return true;
                        case ILLEGAL_ARGUMENT:
                            sender.sendMessage(cmd.getUsage());
                            return true;
                        case INSUFFICIENT_ARGUMENTS:
                            sender.sendMessage(cmd.getUsage());
                            return true;
                        case NO_PERMISSION:
                            sender.sendMessage(ChatColor.RED + "Sorry, you don't have the permission to use this command!");
                            return true;
                        default:
                            return false;
                    }
                } catch (InstantiationException ex) {
                    getLogger().log(Level.SEVERE, null, ex);
                    return true;
                } catch (IllegalAccessException ex) {
                    getLogger().log(Level.SEVERE, null, ex);
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
        
        this.initCommands();
    }

    @Override
    public void onDisable() {
        daemon.interrupt();
    }
    
    private void initCommands() {
        getCmdManager().registerCommand("cache", CommandCache.class);
        getCmdManager().registerCommand("debug", CommandDebugMode.class);
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
        if (debugMode) {
            BukGet.instance.getLogger().info(String.format("[DEBUG] %s", msg));
        }
    }
    
    public static void setDebugMode(boolean mode) {
        debugMode = mode;
    }
    
    public static boolean getDebugMode() {
        return debugMode;
    }
}
