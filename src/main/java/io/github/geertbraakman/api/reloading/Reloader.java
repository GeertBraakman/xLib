package io.github.geertbraakman.api.reloading;

import io.github.com.geertbraakman.Handler;
import io.github.com.geertbraakman.api.config.ConfigHandler;
import io.github.com.geertbraakman.api.storage.StorageHandler;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Reloader extends Handler {

    private static List<Reloader> instances;

    public static Reloader getInstance(Plugin plugin) {
        if(instances == null){
            instances = new ArrayList<>();
        }

        for(Reloader reloader: instances){
            if(reloader.getPlugin().equals(plugin)){
                return reloader;
            }
        }

        Reloader instance = new Reloader(plugin);
        instances.add(instance);
        return instance;
    }

    private List<IReloadable> reloadableList;

    private Reloader(Plugin plugin){
        super(plugin);
        reloadableList = new ArrayList<>();
    }

    public void registerReloadable(IReloadable reloadable){
        if(!reloadableList.contains(reloadable)){
            reloadableList.add(reloadable);
        }
    }

    public boolean reloadPlugin() {
        boolean success = true;

        success = ConfigHandler.getInstance(getPlugin()).reload() && StorageHandler.getInstance(getPlugin()).reload();

        for(IReloadable reloadable: reloadableList){
            if(!reloadable.reload()){
                success = false;
                getLogger().log(Level.WARNING, "Error while reloading '" + reloadable.getClass().getName() + "'. Check above for more info!");
            }
        }
        return success;
    }

}
