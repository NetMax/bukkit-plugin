package org.morph.bukget.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.morph.bukget.BukGet;

/**
 * Manages the huuuuge of BukGet commands
 * @author Morphesus
 */
public class BukGetCommandManager {
    private Map<String, Class<?>> commands = new HashMap<String, Class<?>>();
    
    public void registerCommand(String cmd, Class<?> command) {
        if (command != null && Arrays.asList(command.getInterfaces()).contains(BukGetCommand.class)) {
            this.commands.put(cmd, command);
            BukGet.instance.getLogger().log(Level.INFO, "Registered command: {0}", cmd);
        } else {
            throw new IllegalArgumentException("Every command needs to be assignable from BukGetCommand class");
        }
    }
    
    public void unregisterCommand(String label) {
        this.commands.remove(label);
    }
    
    public boolean isRegistered(String label) {
        return this.commands.containsKey(label);
    }
    
    public Class<?> getCommand(String label) {
        return this.commands.get(label);
    }
}
