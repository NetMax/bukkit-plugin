package org.morph.bukget.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Morphesus
 */
public class PluginData {
    private String status;
    private String slug;
    private String pluginName;
    private String server;
    private List<String> categories;
    private List<String> authors;
    private String logo;
    private String logoFull;
    private String webpage;
    private String dboPage;
    private String description;
    private List<PluginVersion> versions;
    private String name;

    public String getSlug() {
        return slug;
    }

    public String getPluginName() {
        return pluginName;
    }

    public String getServer() {
        return server;
    }

    public List<String> getCategories() {
        return categories;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getLogo() {
        return logo;
    }

    public String getLogoFull() {
        return logoFull;
    }

    public String getWebpage() {
        return webpage;
    }

    public String getDboPage() {
        return dboPage;
    }

    public String getDescription() {
        return description;
    }

    public List<PluginVersion> getVersions() {
        return versions;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setLogoFull(String logoFull) {
        this.logoFull = logoFull;
    }

    public void setWebpage(String webpage) {
        this.webpage = webpage;
    }

    public void setDboPage(String dboPage) {
        this.dboPage = dboPage;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVersions(List<PluginVersion> versions) {
        this.versions = versions;
    }
    
    public void addVersion(PluginVersion version) {
        if (this.versions == null) {
            this.versions = new ArrayList<PluginVersion>();
        }
        
        if (version != null) {
            this.versions.add(version);
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getStatus() {
        return this.status;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
}
