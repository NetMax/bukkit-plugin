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
public class CommandCache extends BukGetCommand {
    @Override
    public String getName() {
        return "cache";
    }

    @Override
    public String getPermission() {
        return "bukget.cache";
    }

    @Override
    public boolean exec(JavaPlugin plugin, CommandSender sender, String[] args) {
        return false;
    }
}
