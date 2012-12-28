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
    public PluginDataCacheFile(File file) {
        if (file != null && file.exists()) {
            this.file = file;
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
        
        // Write data
        
    }
}
