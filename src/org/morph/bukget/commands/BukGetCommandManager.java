/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.morph.bukget.commands;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages the huuuuge of BukGet commands
 * @author Morphesus
 */
public class BukGetCommandManager {
    private Map<String, BukGetCommand> commands = new HashMap<String, BukGetCommand>();
    
    public void registerCommand(BukGetCommand command) {
        if (command != null) {
            this.commands.put(command.getName(), command);
        } else {
            throw new NullPointerException("Could not register a 'null' command");
        }
    }
    
    public BukGetCommand unregisterCommand(String label) {
        return this.commands.remove(label);
    }
    
    public boolean isRegistered(String label) {
        return this.commands.containsKey(label);
    }
    
    public BukGetCommand getCommand(String label) {
        return (BukGetCommand) this.commands.get(label);
    }
}
