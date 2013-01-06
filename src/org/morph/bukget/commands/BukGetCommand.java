package org.morph.bukget.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Base command class
 * @author Morphesus
 */
public interface BukGetCommand {
    public BukGetCommandResult exec(JavaPlugin plugin, CommandSender sender, String[] args);
    public String getName();
    public String getPermission();
    public String[] getUsage();
}
