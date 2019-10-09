package io.github.geertbraakman.api.storage;

import io.github.geertbraakman.Handler;
import io.github.geertbraakman.api.APIPlugin;

import java.util.ArrayList;
import java.util.List;

public class StorageHandler extends Handler {

    private List<Source> sources;

    public StorageHandler(APIPlugin plugin) {
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
