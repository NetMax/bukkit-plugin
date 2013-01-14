package org.morph.bukget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
    public static final String URL_BUKGET_ROOT            = "http://bukget.org";
    public static final String URL_BUKGET_API             = URL_BUKGET_ROOT + "/api";
    public static final String URL_BUKGET_API_PLUGINS     = URL_BUKGET_API + "/plugins";
    public static final String URL_BUKGET_API_PLUGIN_DATA = URL_BUKGET_API + "/plugin/";
    
    public static final int MIN_BUFFER_SIZE = 256;
    
    /**
     * Gets the local name cache file, if existing.
     * If not, the cache file will be generated.
     * @return The local plugin name cache
     * @throws IOException 
     */
    public PluginListCacheFile getLocalNameCache() throws IOException {
        if (!existsNameCacheFile()) {
            updateLocalNameCache();
        }
        
        return new PluginListCacheFile(new File(BukGet.instance.getDataFolder(), BukGet.BUKGET_NAME_CACHE));
    }
    
    /**
     * Deletes the local data cache file
     */
    public boolean deleteLocalDataCache() {
        final File cacheFile = new File(BukGet.instance.getDataFolder(), BukGet.BUKGET_DATA_CACHE);
        
        if (cacheFile.exists()) {
            return cacheFile.delete();
        } else {
            return false;
        }
    }

    /**
     * Creates a cache containing all plugins details.
     * @throws IOException
     * @throws ParseException 
     */
    public void updateLocalDataCache() throws IOException, ParseException {
        // Debug Info
        BukGet.debug("Creating Full Cache File ...");
        
        PluginDataCacheFile cache = new PluginDataCacheFile(new File(BukGet.instance.getDataFolder(), BukGet.BUKGET_DATA_CACHE));
        
        PluginListCacheFile plugins;
        if (existsNameCacheFile()) {
            plugins = getLocalNameCache();
        } else {
            updateLocalNameCache();
            plugins = getLocalNameCache();
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
    
    /**
     * Returns detailed informations for a single plugin
     * @param pluginName The plugin to get informations for
     * @return Plugin informations
     * @throws IOException
     * @throws ParseException 
     */
    public PluginData getPluginData(final String pluginName) throws IOException, ParseException {
        // Debug
        BukGet.debug("Getting Data for '" + pluginName + "'.");
        
        String     raw   = getData(URL_BUKGET_API_PLUGIN_DATA + pluginName.toLowerCase().trim());
        PluginData pData = new PluginData();
        
        // Parse Data
        JSONParser parser = new JSONParser();
        Object     json   = parser.parse(raw);
        JSONObject root   = (JSONObject) json;
        
        // -- Check plugin existence
        if (root.containsKey("error")) {
            return null;
        }
        
        // -- Read plugin details
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
    
    /**
     * Deletes the local name list cache file
     */
    public boolean deleteLocalNameCache() {
        final File cacheFile = new File(BukGet.instance.getDataFolder(), BukGet.BUKGET_NAME_CACHE);
        
        if (cacheFile.exists()) {
            return cacheFile.delete();
        } else {
            return false;
        }
    }
    
    /**
     * Updates the local plugin name cache
     * @throws IOException 
     */
    public void updateLocalNameCache() throws IOException {
        // Debug Info
        BukGet.debug("Creating Cache File ...");
        
        PluginListCacheFile cacheData = createNameCache();
        
        if (cacheData != null) {
            cacheData.saveAs(new File(BukGet.instance.getDataFolder(), BukGet.BUKGET_NAME_CACHE));
        } else {
            BukGet.instance.getLogger().severe("Error: CacheData is null! Could not save " + BukGet.BUKGET_NAME_CACHE);
        }
    }
    
    /**
     * Checks, if the local name cache file is existing
     * @return 
     */
    public boolean existsNameCacheFile() {
        return new File(BukGet.instance.getDataFolder(), BukGet.BUKGET_NAME_CACHE).exists();
    }
    
    /**
     * Creates a name cache, ready to save
     * @return The name cache object
     * @throws IOException 
     */
    public PluginListCacheFile createNameCache() throws IOException {
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
    
    /**
     * Returns a list of all existing plugins
     * @return The plugin name list
     * @throws IOException 
     */
    public List<String> getPluginList() throws IOException {
        try {
            String raw = this.getData(URL_BUKGET_API_PLUGINS);
            
            JSONParser parser = new JSONParser();
            Object     json   = parser.parse(raw);
            JSONArray  jsona  = (JSONArray) json;
            
            // Debug Info
            BukGet.debug("Parsing Plugin data ...");
            
            return (List<String>) jsona;
        } catch (ParseException ex) {
            BukGet.instance.getLogger().log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /**
     * Receive textual data from internet using 4KB Buffer
     * @param urlString The url to get data from
     * @return The received data as String
     * @throws IOException 
     */
    private String getData(String urlString) throws IOException {
        return this.getData(urlString, 4096); // 4KB Buffer
    }
    
    /**
     * Receive textual data from internet using a custom Buffer
     * @param urlString The url to get data from
     * @param bufferSize The buffer size you want to use
     * @return The received data as String
     * @throws IOException 
     */
    private String getData(String urlString, int bufferSize) throws IOException {
        if (bufferSize <= MIN_BUFFER_SIZE) {
            throw new IllegalArgumentException("The bufferSize is too small :(");
        }
        
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        
        con.setAllowUserInteraction(false);
        con.setConnectTimeout(5000);
        con.setDoInput(true);
        con.setDoOutput(false);
        con.setInstanceFollowRedirects(true);
        con.setRequestMethod("GET");
        con.setUseCaches(false);
        
        InputStream is = con.getInputStream();
        StringBuilder data = new StringBuilder();
        
        // Debug Info
        BukGet.debug(String.format("Downloading Data from '%s'", urlString));
        
        byte[] buffer = new byte[bufferSize];
        int    read;
        while ((read = is.read(buffer)) > -1) {
            for (int i = 0; i < read; i++) {
                data.append((char) buffer[i]);
            }
        }
        
        is.close();
        return data.toString();
    }
    
    /**
     * Downloads a file
     * @param urlString URL of the desired file
     * @param destination Where you want to save the file?
     * @param override If the file is already existing, you want to override it?
     * @param bufferSize The bufferSize you want to use
     * @return True on success, otherwise false will be returned
     */
    private boolean downloadFile(String urlString, File destination, boolean override, int bufferSize) throws MalformedURLException, IOException {
        if (destination != null & (!destination.exists() || override)) {
            if (bufferSize <= MIN_BUFFER_SIZE) {
                throw new IllegalArgumentException("The bufferSize is too small :(");
            }
            
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            
            con.setAllowUserInteraction(false);
            con.setConnectTimeout(2500);
            con.setDoInput(true);
            con.setDoOutput(false);
            con.setInstanceFollowRedirects(true);
            con.setRequestMethod("GET");
            con.setUseCaches(false);
            
            InputStream      is  = con.getInputStream();
            FileOutputStream fos = new FileOutputStream(destination);
            
            // Debug
            BukGet.debug(String.format("Downloading file from '%s'", urlString));
            
            // Delete old file, if exists
            if (destination.exists()) {
                if (!destination.delete()) {
                    BukGet.debug(String.format("Could not delete old file '%s'", destination.toString()));
                    return false;
                }
            }
            
            // Download new file
            byte[] buffer = new byte[bufferSize];
            int    read;
            while ((read = is.read(buffer)) > -1) {
                fos.write(buffer, 0, read);
            }
            
            fos.close();
            is.close();
            
            return true;
        } else {
            return false;
        }
    }
}
