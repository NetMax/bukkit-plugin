package org.morph.bukget.data;

import java.util.List;

/**
 *
 * @author Morphesus
 */
class PluginVersion {
    private String version;
    private String md5;
    private String filename;
    private String link;
    private String download;
    private String type;
    private String status;
    private String changelog;
    private List<String> gameVersions;
    private long date;
    private String slug;
    private List<String> hardDependencies;
    private List<String> softDependencies;
    private List<PluginCommand> commands;
    private List<PluginPermission> permissions;

    public String getVersion() {
        return version;
    }

    public String getMd5() {
        return md5;
    }

    public String getFilename() {
        return filename;
    }

    public String getLink() {
        return link;
    }

    public String getDownload() {
        return download;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getChangelog() {
        return changelog;
    }

    public List<String> getGameVersions() {
        return gameVersions;
    }

    public long getDate() {
        return date;
    }

    public String getSlug() {
        return slug;
    }

    public List<String> getHardDependencies() {
        return hardDependencies;
    }

    public List<String> getSoftDependencies() {
        return softDependencies;
    }

    public List<PluginCommand> getCommands() {
        return commands;
    }

    public List<PluginPermission> getPermissions() {
        return permissions;
    }
}
