package io.github.geertbraakman.v0_3_4.api.reloading;

import io.github.geertbraakman.v0_3_4.Handler;
import io.github.geertbraakman.v0_3_4.api.APIPlugin;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

public class Reloader extends Handler {

    private List<IReloadable> reloadableList;

    public Reloader(APIPlugin plugin){
        super(plugin);
        reloadableList = new LinkedList<>();
    }

    public void registerReloadable(IReloadable reloadable){
        if(!reloadableList.contains(reloadable)){
            reloadableList.add(reloadable);
        }
    }

    public boolean reloadPlugin() {
        boolean success;

        success = getAPIPlugin().getConfigHandler().reload();

        for(IReloadable reloadable: reloadableList){
            if (!reloadable.reload()){
                success = false;
                getLogger().log(Level.WARNING, "Error while reloading '" + reloadable.getClass().getName() + "'. Check above for more info!");
            }
        }

        if (!success) {
            getLogger().log(Level.WARNING, "Error while reloading!");
        }

        return success;
    }

}
