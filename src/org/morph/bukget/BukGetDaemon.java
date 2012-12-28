package org.morph.bukget;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.morph.bukget.data.PluginListCacheFile;

/**
 *
 * @author Morphesus
 */
public class BukGetDaemon extends Thread {

    public static volatile boolean running = true;

    @Override
    public void run() {
        final File cache_file = new File(BukGet.instance.getDataFolder(), BukGet.BUKGET_NAME_CACHE);
        
        // Debug Info
        BukGet.debug("Daemon started");

        while (running) {
            // Check cache
            if (cache_file.exists()) {
                try {
                    PluginListCacheFile cache = new PluginListCacheFile(cache_file);

                    // We need a update
                    if (System.currentTimeMillis() > (cache.getTimestamp() + BukGet.instance.getConfig().getLong("daemon.cache.update_interval", 1800000))) {
                        BukGet.manager.updateLocalCache();
                    }

                } catch (IOException ex) {
                    BukGet.instance.getLogger().log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    BukGet.manager.updateLocalCache();
                } catch (IOException ex) {
                    BukGet.instance.getLogger().log(Level.SEVERE, null, ex);
                }
            }
            
            try {
                Thread.sleep(BukGet.instance.getConfig().getLong("daemon.thread_interval", 5000));
            } catch (InterruptedException ex) {
                BukGet.instance.getLogger().log(Level.SEVERE, null, ex);
            }
        }
    }
}
