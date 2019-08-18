package io.github.geertbraakman.api.storage;

import io.github.geertbraakman.Handler;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class StorageHandler extends Handler {

    private static List<StorageHandler> instances;

    public static StorageHandler getInstance(Plugin plugin)
    {
        if (instances == null) {
            instances = new ArrayList<>();
        }

        for (StorageHandler instance: instances){
            if(instance.getPlugin().equals(plugin)){
                return  instance;
            }
        }

         StorageHandler instance = new StorageHandler(plugin);
        instances.add(instance);
        return instance;
    }

    private List<Source> sources;

    private StorageHandler(Plugin plugin) {
        super(plugin);
        sources = new ArrayList<>();
    }

    @Override
    public boolean reload(){
        for(Source source: sources){
            if(!source.reload()){
               return false;
            }
        }
        return true;
    }

    @Override
    public void onDisable(){
        for(Source source: sources){
            source.save();
        }
    }

    public void registerSource(Source source) {
        if (!sources.contains(source)) {
            sources.add(source);
        }
    }

    public void unRegister(Source source){
        sources.remove(source);
    }
}
