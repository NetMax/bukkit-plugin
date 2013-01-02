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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.morph.bukget.data.BaseCacheFile.FileType;
import org.morph.bukget.data.PluginData;
import org.morph.bukget.data.PluginDataCacheFile;
import org.morph.bukget.data.PluginListCacheFile;
import org.morph.bukget.data.PluginVersion;

/**
 * 
 * @author Morphesus
 */
public class BukGetManager {
    public static final String URL_BUKGET_ROOT            = "http://bukget.org/";
    public static final String URL_BUKGET_API             = URL_BUKGET_ROOT + "/api";
    public static final String URL_BUKGET_API_PLUGINS     = URL_BUKGET_API + "/plugins";
    public static final String URL_BUKGET_API_PLUGIN_DATA = URL_BUKGET_API + "/plugin/";
    
    public PluginListCacheFile getCache() throws IOException {
        if (!existsCacheFile()) {
            updateLocalCache();
        }
        
        return new PluginListCacheFile(new File(BukGet.instance.getDataFolder(), BukGet.BUKGET_NAME_CACHE));
    }

    public void updateLocalDataCache() throws IOException, ParseException {
        // Debug Info
        BukGet.debug("Creating Full Cache File ...");
        
        PluginDataCacheFile cache = new PluginDataCacheFile(new File(BukGet.instance.getDataFolder(), BukGet.BUKGET_DATA_CACHE));
        
        PluginListCacheFile plugins;
        if (existsCacheFile()) {
            plugins = getCache();
        } else {
            updateLocalCache();
            plugins = getCache();
        }
        
        if (plugins != null) {
            for (String plugin : plugins.getPluginNames()) {
                PluginData pData = getPluginData(plugin);
                cache.addPluginData(pData);
                
                cache.save();
            }
        } else {
            BukGet.instance.getLogger().severe("Could not get Plugin Name Cache");
        }
    }
    
    public PluginData getPluginData(final String pluginName) throws IOException, ParseException {
        // Debug
        BukGet.debug("Getting Data for '" + pluginName + "'.");
        
        String     raw   = getData(URL_BUKGET_API_PLUGIN_DATA + pluginName.toLowerCase().trim());
        PluginData pData = new PluginData();
        
        // Parse Data
        JSONParser parser = new JSONParser();
        Object     json   = parser.parse(raw);
        JSONObject root   = (JSONObject) json;
        
        pData.setStatus(root.get("status").toString());
        pData.setName(root.get("name").toString());
        pData.setPluginName(root.get("plugin_name").toString());
        pData.setDboPage(root.get("bukkitdev_link").toString());
        pData.setDescription(root.get("desc").toString());
        
        JSONArray jsonCategories = (JSONArray) root.get("categories");
        pData.setCategories(jsonCategories);
        
        JSONArray jsonVersions = (JSONArray) root.get("versions");
        for (Object verObj : jsonVersions) {
            JSONObject jsonObjVersion = (JSONObject) verObj;
            PluginVersion version     = new PluginVersion();
            
            version.setHardDependencies((JSONArray) jsonObjVersion.get("hard_dependencies"));
            version.setSoftDependencies((JSONArray) jsonObjVersion.get("soft_dependencies"));
            version.setGameVersions((JSONArray) jsonObjVersion.get("game_builds"));
            version.setStatus(jsonObjVersion.get("status").toString());
            version.setVersion(jsonObjVersion.get("name").toString()); // Version == Name ??
            version.setDate(Long.parseLong(jsonObjVersion.get("date").toString()));
            version.setType(jsonObjVersion.get("type").toString());
            version.setDownload(jsonObjVersion.get("dl_link").toString());
            version.setFilename(jsonObjVersion.get("filename").toString());
            
            pData.addVersion(version);
        }
        
        return pData;
    }
    
    public void updateLocalCache() throws IOException {
        // Debug Info
        BukGet.debug("Creating Cache File ...");
        
        PluginListCacheFile cacheData = createCache();
        
        if (cacheData != null) {
            cacheData.saveAs(new File(BukGet.instance.getDataFolder(), BukGet.BUKGET_NAME_CACHE));
        } else {
            BukGet.instance.getLogger().severe("Error: CacheData is null! Could not save " + BukGet.BUKGET_NAME_CACHE);
        }
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
            cache.setType(FileType.PLUGIN_NAME_CACHE);
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
        con.setConnectTimeout(3000);
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
