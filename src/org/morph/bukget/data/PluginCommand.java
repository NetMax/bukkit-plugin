package org.morph.bukget.data;

import java.util.List;

/**
 *
 * @author Morphesus
 */
public class PluginCommand {
    private String usage;
    private List<String> alias;
    private String command;
    private String permission;
    private String permissionMessage;

    public String getUsage() {
        return usage;
    }

    public List<String> getAliases() {
        return alias;
    }

    public String getCommand() {
        return command;
    }

    public String getPermission() {
        return permission;
    }

    public String getPermissionMessage() {
        return permissionMessage;
    }
}
