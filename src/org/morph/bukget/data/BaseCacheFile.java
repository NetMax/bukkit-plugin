package org.morph.bukget.data;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Morphesus
 */
public abstract class BaseCacheFile {
    public enum FileType {
        UNKNOWN_TYPE(0),
        PLUGIN_LIST_CACHE(1),
        PLUGIN_FULL_CACHE(2);
        
        private final int typenum;
        
        private FileType(int typenum) {
            this.typenum = typenum;
        }
        
        public int getTypeNumber() {
            return this.typenum;
        }
        
        public static FileType valueOf(int typenum) {
            for (FileType ft : values()) {
                if (ft.getTypeNumber() == typenum) {
                    return ft;
                }
            }
            
            return UNKNOWN_TYPE;
        }
    }
    
    protected long timestamp;
    protected FileType type;
    
    public long getTimestamp() {
        return this.timestamp;
    }
    
    public FileType getType() {
        return this.type;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public void setType(FileType type) {
        this.type = type;
    }
    
    public static BaseCacheFile readHeader(final File file) throws FileNotFoundException, IOException {
        BaseCacheFile header = new BaseCacheFile() {};
        DataInputStream     dis = new DataInputStream(new FileInputStream(file));
        
        // Read header
        header.timestamp = dis.readLong();
        header.type      = FileType.valueOf(dis.readByte());
        
        dis.close();
        
        return header;
    }
}
