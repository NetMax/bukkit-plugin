package org.morph.bukget.data;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.morph.bukget.BukGet;

/**
 *
 * @author Morphesus
 */
public class PluginDataCacheFile extends BaseCacheFile {
    private List<PluginData> plugins;
    private File             file;
    
    public PluginDataCacheFile() { }
    public PluginDataCacheFile(File file) throws IOException {
        if (file != null) {
            this.file = file;
            this.open();
        }
    }
    
    public void addPluginData(final PluginData data) {
        if (data != null) {
            this.plugins.add(data);
        }
    }
    
    public final void save() throws FileNotFoundException, IOException {
        if (this.file != null) {
            saveAs(this.file);
        } else {
            BukGet.instance.getLogger().severe("Could not save PluginListCacheFile: save() couldn't find a default file");
        }
    }
    
    public void saveAs(final File file) throws IOException {
        if (!file.exists()) {
            // Debug info
            BukGet.debug("Try creating cache file on " + file.getAbsolutePath());
            
            file.createNewFile();
        }
        
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
        
        // Write header
        dos.writeLong(timestamp);
        dos.writeByte(type.getTypeNumber());
        
        /*** Write data -- enjoy ***/
        dos.writeShort(this.plugins.size());
        
        // Write out the plugins -- have fun ^^
        for (PluginData plugin : this.plugins) {
            // Slug
            DataOutputHelper.writeSmallString(dos, plugin.getSlug());
            
            // PluginName
            DataOutputHelper.writeSmallString(dos, plugin.getPluginName());
            
            // Server
            DataOutputHelper.writeSmallString(dos, plugin.getServer());
            
            // Categories
            dos.writeByte(plugin.getCategories().size());
            for (String category : plugin.getCategories()) {
                DataOutputHelper.writeSmallString(dos, category);
            }
            
            // Authors
            dos.writeByte(plugin.getAuthors().size());
            for (String author : plugin.getAuthors()) {
                DataOutputHelper.writeSmallString(dos, author);
            }
            
            // Logo
            DataOutputHelper.writeSmallString(dos, plugin.getLogo());
            
            // Logo Full
            DataOutputHelper.writeSmallString(dos, plugin.getLogoFull());
            
            // Webpage
            DataOutputHelper.writeSmallString(dos, plugin.getWebpage());
            
            // DBO Page
            DataOutputHelper.writeSmallString(dos, plugin.getDboPage());
            
            // Description
            DataOutputHelper.writeMediumString(dos, plugin.getDescription());
            
            // Versions
            dos.writeByte(plugin.getVersions().size());
            for (PluginVersion version : plugin.getVersions()) {
                DataOutputHelper.writeSmallString(dos, version.getVersion());
                DataOutputHelper.writeSmallString(dos, version.getMd5());
                DataOutputHelper.writeSmallString(dos, version.getFilename());
                DataOutputHelper.writeSmallString(dos, version.getLink());
                DataOutputHelper.writeSmallString(dos, version.getDownload());
                DataOutputHelper.writeSmallString(dos, version.getType());
                DataOutputHelper.writeSmallString(dos, version.getStatus());
                DataOutputHelper.writeMediumString(dos, version.getChangelog());
                
                // Version -- GameVersions
                dos.writeByte(version.getGameVersions().size());
                for (String gameVersion : version.getGameVersions()) {
                    DataOutputHelper.writeSmallString(dos, gameVersion);
                }
                
                dos.writeLong(version.getDate());
                
                DataOutputHelper.writeSmallString(dos, version.getSlug());
                
                // Version -- Hard Dependencies
                dos.writeByte(version.getHardDependencies().size());
                for (String hd : version.getHardDependencies()) {
                    DataOutputHelper.writeSmallString(dos, hd);
                }
                
                // Version -- Soft Dependencies
                dos.writeByte(version.getSoftDependencies().size());
                for (String hd : version.getSoftDependencies()) {
                    DataOutputHelper.writeSmallString(dos, hd);
                }
                
                // Version -- Commands
                dos.writeShort(version.getCommands().size());
                for (PluginCommand cmd : version.getCommands()) {
                    DataOutputHelper.writeSmallString(dos, cmd.getUsage());
                    
                    // Version -- Commands -- Aliases
                    dos.writeByte(cmd.getAliases().size());
                    for (String alias : cmd.getAliases()) {
                        DataOutputHelper.writeSmallString(dos, alias);
                    }
                    
                    DataOutputHelper.writeSmallString(dos, cmd.getCommand());
                    DataOutputHelper.writeSmallString(dos, cmd.getPermission());
                    DataOutputHelper.writeSmallString(dos, cmd.getPermissionMessage());
                }
                
                // Version -- Permissions
                dos.writeShort(version.getPermissions().size());
                for (PluginPermission perm : version.getPermissions()) {
                    DataOutputHelper.writeSmallString(dos, perm.getRole());
                    DataOutputHelper.writeSmallString(dos, perm.getDefaultPermission());
                }
            }
        }
        
        dos.close();
    }
    
    public final boolean open() throws IOException {
        if (this.file != null) {
            openFile(this.file);
            return true;
        } else {
            return false;
        }
    }
    
    public void openFile(final File file) throws IOException {
        // Read header
        BaseCacheFile header = BaseCacheFile.readHeader(file);
        this.timestamp       = header.getTimestamp();
        this.type            = header.getType();
        
        // TODO :: Read data
    }
}
