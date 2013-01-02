package org.morph.bukget.data;

import java.util.List;

/**
 *
 * @author Morphesus
 */
public class PluginVersion {
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

    public void setVersion(String version) {
        this.version = version;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    public void setGameVersions(List<String> gameVersions) {
        this.gameVersions = gameVersions;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setHardDependencies(List<String> hardDependencies) {
        this.hardDependencies = hardDependencies;
    }

    public void setSoftDependencies(List<String> softDependencies) {
        this.softDependencies = softDependencies;
    }

    public void setCommands(List<PluginCommand> commands) {
        this.commands = commands;
    }

    public void setPermissions(List<PluginPermission> permissions) {
        this.permissions = permissions;
    }
}
