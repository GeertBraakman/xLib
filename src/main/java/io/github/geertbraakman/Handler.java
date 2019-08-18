package io.github.geertbraakman;

import io.github.geertbraakman.api.reloading.IReloadable;
import io.github.geertbraakman.api.reloading.Reloader;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

/**
 * A generalization class for all the handlers in this plugin.
 */
public abstract class Handler implements Listener, IReloadable {

    private Plugin plugin;
    private Logger logger;

    /**
     * The constructor is made protected because you should not be able to create an empty handler.
     */
    protected Handler(Plugin plugin){
        this.plugin = plugin;
        logger = plugin.getLogger();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        if (!(this instanceof Reloader)) {
            Reloader.getInstance(plugin).registerReloadable(this);
        }
    }

    /**
     * @return The plugin that is using this handler
     */
    protected Plugin getPlugin(){
        return plugin;
    }

    /**
     *
     * @return the logger of this plugin.
     */
    protected Logger getLogger(){
        return logger;
    }

    /**
     * This method will be executed when the plugin reloads.
     * @return if the reload passed
     */
    public boolean reload() {
        return true;
    }

    /**
     * This method will be executed when the plugin disables
     */
    public void onDisable(){}
}
