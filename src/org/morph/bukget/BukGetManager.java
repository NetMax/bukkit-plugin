package org.morph.bukget;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.morph.bukget.data.BaseCacheFile.FileType;
import org.morph.bukget.data.PluginListCacheFile;

/**
 * 
 * @author Morphesus
 */
public class BukGetManager {
    public static final String URL_BUKGET_ROOT        = "http://bukget.org/";
    public static final String URL_BUKGET_API         = URL_BUKGET_ROOT + "/api";
    public static final String URL_BUKGET_API_PLUGINS = URL_BUKGET_API + "/plugins";
    
    public PluginListCacheFile getCache() throws IOException {
        if (existsCacheFile()) {
            updateLocalCache();
        }
        
        return new PluginListCacheFile(new File(BukGet.instance.getDataFolder(), BukGet.BUKGET_NAME_CACHE));
    }

    public void updateLocalDataCache() {
        BukGet.debug("Creating Full Cache File ...");
    }
    
    public void updateLocalCache() throws IOException {
        // Debug Info
        BukGet.debug("Creating Cache File ...");
        
        PluginListCacheFile cacheData = createCache();
        cacheData.saveAs(new File(BukGet.instance.getDataFolder(), BukGet.BUKGET_NAME_CACHE));
    }
    
    public boolean existsCacheFile() {
        return new File(BukGet.instance.getDataFolder(), BukGet.BUKGET_NAME_CACHE).exists();
    }
    
    public PluginListCacheFile createCache() throws IOException {
        List<String> plugins = getPluginList();
        PluginListCacheFile cache = new PluginListCacheFile();
        
        // Debug Info
        BukGet.debug("Generating Plugin Cache ...");
        
        if (plugins != null) {
            cache.setTimestamp(System.currentTimeMillis());
            cache.setType(FileType.PLUGIN_LIST_CACHE);
            cache.setPluginNames(plugins);
            
            return cache;
        } else {
            return null;
        }
    }
    
    public List<String> getPluginList() throws IOException {
        try {
            List<String> plugins = new ArrayList<String>();
            String raw = this.getData(URL_BUKGET_API_PLUGINS);
            
            JSONParser parser = new JSONParser();
            Object     json   = parser.parse(raw);
            JSONArray  jsona  = (JSONArray) json;
            
            // Debug Info
            BukGet.debug("Parsing Plugin data ...");
            
            plugins = jsona;
            return jsona;
        } catch (ParseException ex) {
            BukGet.instance.getLogger().log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    private String getData(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        
        con.setAllowUserInteraction(false);
        con.setConnectTimeout(1000);
        con.setDoInput(true);
        con.setDoOutput(false);
        con.setInstanceFollowRedirects(true);
        con.setRequestMethod("GET");
        con.setUseCaches(false);
        
        InputStream is = con.getInputStream();
        StringBuilder data = new StringBuilder();
        
        // Debug Info
        BukGet.debug("Downloading Data ...");
        
        byte[] buffer = new byte[10485760]; // Buffer for 10MB
        int    read;
        while ((read = is.read(buffer)) > -1) {
            for (int i = 0; i < read; i++) {
                data.append((char) buffer[i]);
            }
        }
        
        is.close();
        return data.toString();
    }
}