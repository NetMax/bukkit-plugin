/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.morph.bukget.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Morphesus
 */
public class CommandPlugin implements BukGetCommand {

    @Override
    public BukGetCommandResult exec(JavaPlugin plugin, CommandSender sender, String[] args) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getName() {
        return "plugin";
    }

    @Override
    public String getPermission() {
        return "bukget.plugin";
    }

    @Override
    public String[] getUsage() {
        return new String[] {
            "/bukget plugin <info|enable|disable|install> [params]",
            "|",
            "+-> /bukget plugin info <plugin_name>",
            "+-> /bukget plugin enable <plugin_name>",
            "+-> /bukget plugin disable <plugin_name>",
            "+-> /bukget plugin install <plugin_name>"
        };
    }
    
}
