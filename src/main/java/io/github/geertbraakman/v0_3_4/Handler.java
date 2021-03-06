package io.github.geertbraakman.v0_3_4;

import io.github.geertbraakman.v0_3_4.api.APIPlugin;
import io.github.geertbraakman.v0_3_4.api.reloading.IReloadable;
import io.github.geertbraakman.v0_3_4.api.reloading.Reloader;
import org.apache.commons.lang.Validate;
import org.bukkit.event.Listener;

import java.util.logging.Logger;

/**
 * A generalization class for all the handlers in this plugin.
 */
public abstract class Handler implements Listener, IReloadable {

    private APIPlugin plugin;
    private Logger logger;

    /**
     * The constructor is made protected because you should not be able to create an empty handler.
     */
    protected Handler(APIPlugin plugin){
        Validate.notNull(plugin, "Plugin cannot be null");
        this.plugin = plugin;
        logger = plugin.getLogger();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        if (!(this instanceof Reloader)) {
            plugin.getReloader().registerReloadable(this);
        }
    }

    /**
     * @return The plugin that is using this handler
     */
    protected APIPlugin getAPIPlugin(){
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
