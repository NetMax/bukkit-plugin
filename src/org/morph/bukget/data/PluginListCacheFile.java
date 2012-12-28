package org.morph.bukget.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.morph.bukget.BukGet;

/**
 *
 * @author Morphesus
 */
public class PluginListCacheFile extends BaseCacheFile {
    private List<String> plugins;
    private File         file;

    public PluginListCacheFile(final File file) throws IOException {
        if (file != null && file.exists()){
            this.file = file;
            open(file);
        }
    }

    public PluginListCacheFile() {
        this.file = null;
    }
    
    public List<String> getPluginNames() {
        return this.plugins;
    }
    
    public File getFile() {
        return this.file;
    }
    
    public void setFile(File file) {
        this.file = file;
    }
    
    public void addPluginName(final String plugin) {
        if (this.plugins == null && plugin != null) {
            this.plugins = new ArrayList<String>();
        }
        
        if (!hasPluginName(plugin)) {
            this.plugins.add(plugin);
        }
    }
    
    public boolean hasPluginName(final String plugin) {
        return plugin != null && this.plugins != null && this.plugins.contains(plugin);
    }
    
    public void removePluginName(final String plugin) {
        if (this.plugins != null && plugin != null) {
            this.plugins.remove(plugin);
        }
    }

    public void setPluginNames(List<String> plugins) {
        this.plugins = plugins;
    }
    
    public final void save() throws FileNotFoundException, IOException {
        if (this.file != null) {
            saveAs(this.file);
        } else {
            BukGet.instance.getLogger().severe("Could not save PluginListCacheFile: save() couldn't find a default file");
        }
    }

    public final void saveAs(final File file) throws FileNotFoundException, IOException {
        if (!file.exists()) {
            // Debug info
            BukGet.debug("Try creating cache file on " + file.getAbsolutePath());
            
            file.createNewFile();
        }
        
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
        
        // Write header
        dos.writeLong(timestamp);
        dos.writeByte(type.getTypeNumber());
        
        // Write data
        dos.writeShort(plugins.size());
        for (String name : plugins) {
            dos.writeByte(name.length());
            dos.writeChars(name);
        }
        
        // Close stream
        dos.close();
    }

    public final void open(final File file) throws IOException {
        DataInputStream     dis = new DataInputStream(new FileInputStream(file));
        
        // Read header
        timestamp = dis.readLong();
        type      = FileType.valueOf(dis.readByte());
        plugins   = new ArrayList<String>();
        
        // Read data
        short count = dis.readShort();
        for (short s = 0; s < count; s++) {
            byte len = dis.readByte();
            String data = "";
            for (byte b = 0; b < len; b++) {
                data += dis.readChar();
            }
            
            plugins.add(data);
        }
        
        dis.close();
    }
}
