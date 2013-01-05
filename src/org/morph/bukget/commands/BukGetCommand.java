package org.morph.bukget.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Base command class
 * @author Morphesus
 */
public abstract class BukGetCommand {
    private Map<String, BukGetCommand> subCommands = new HashMap<String, BukGetCommand>();
    private BukGetCommand parent;
    
    public BukGetCommand() { }
    public BukGetCommand(BukGetCommand parent) {
        this.parent = parent;
    }
    
    public abstract boolean exec(JavaPlugin plugin, CommandSender sender, String[] args);
    public abstract String getName();
    public abstract String getPermission();
    
    /**
     * Checks, if this command has a parent command
     * @return True, if there is a parent, false if not
     */
    public boolean hasParent() {
        return this.parent != null;
    }
    
    /**
     * Gets the parent command for this command
     * @return The parent command or null, if there is no parent command
     */
    public BukGetCommand getParent() {
        return this.parent;
    }
    
    /**
     * Adds a subcommand to this command
     * @param command 
     */
    public void addCommand(BukGetCommand command) {
        if (command != null) {
            this.subCommands.put(command.getName(), command);
        } else {
            throw new NullPointerException("Could not register a 'null' command");
        }
    }
    
    /**
     * Removes a subcommand
     * @param label Command to remove
     * @return The removed command or null, if there was no command to remove
     */
    public BukGetCommand removeCommand(String label) {
        return this.subCommands.remove(label);
    }
    
    /**
     * Checks, if a specified command is registered
     * @param label Command to check
     * @return true, if the command is registered, otherwise false
     */
    public boolean isCommandRegistered(String label) {
        return this.subCommands.containsKey(label);
    }
    
    /**
     * Gets a command by his name
     * @param label The name of the command
     * @return The command for the given name or null, if the command could not found
     */
    public BukGetCommand getCommand(String label) {
        return (BukGetCommand) this.subCommands.get(label);
    }
    
    /**
     * Gets a collection of all registered subcommands for THIS command
     * To get a list of all subcommands (recursive) use the getAllSubCommands() method
     * @return A collection of all subcommands for THIS command
     */
    public Collection<BukGetCommand> getSubCommands() {
        return this.subCommands.values();
    }
    
    /**
     * Returns a - WTF - list of all (!) subcommands for this command!
     * @return The WTF list of all subcommands
     */
    public List<BukGetCommand> getAllSubCommands() {
        List<BukGetCommand> cmdList = new ArrayList<BukGetCommand>(getSubCommands());
        
        // TODO :: Add check for parent command
        
        // Init with first level subs
        int added = getSubCommands().size();
        while (added > 0) {
            // -- Check all listed commands for subcommands
            for (BukGetCommand cmd : cmdList) {
                // -- Get subcommands for each subcommand of this command - WTF!!
                Collection<BukGetCommand> cmds = cmd.getSubCommands();
                // -- Checkout this subs ...
                for (BukGetCommand subCmd : cmds) {
                    if (!cmdList.contains(subCmd)) {
                        // -- Wow, there is a new subcommand!! Now add this to the list ...
                        if (cmdList.add(subCmd)) {
                            added++;
                        }
                    }
                }
            }
        }
        
        return cmdList;
    }
}
