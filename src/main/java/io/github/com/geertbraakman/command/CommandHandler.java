package io.github.com.geertbraakman.command;

import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandHandler {

    private Plugin plugin;
    private Logger logger;
    private SimpleCommandMap simpleCommandMap;

    private CommandHandler(Plugin plugin) {
        this.plugin = plugin;
        logger = plugin.getLogger();
       setSimpleCommandMap();
    }

    private void setSimpleCommandMap() {
        SimplePluginManager spm = (SimplePluginManager) plugin.getServer().getPluginManager();
        Field f = null;

        try {
            f = SimplePluginManager.class.getDeclaredField("commandMap");
            f.setAccessible(true);
            simpleCommandMap = (SimpleCommandMap) f.get(spm);
            f.setAccessible(false);
        } catch (Exception e) {
            logger.log(Level.WARNING, "There went something wrong with getting the CommandMap.");
            logger.log(Level.WARNING, "Message: " + e.getMessage());
        }
    }

    public boolean registerCommand(Plugin plugin, APICommand command) {
        if(simpleCommandMap == null) {
            logger.log(Level.WARNING, plugin.getName() + " tried to register the command " + command.getName() + " but there is something wrong with the CommandMap");
            return false;
        }

        simpleCommandMap.register(plugin.getName(), command);
        return true;
    }

}
