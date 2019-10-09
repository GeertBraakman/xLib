package io.github.geertbraakman.api.command;

import io.github.geertbraakman.Handler;
import io.github.geertbraakman.PropertyHandler;
import io.github.geertbraakman.api.APIPlugin;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.util.logging.Level;

public class CommandHandler extends Handler {

    private SimpleCommandMap simpleCommandMap;
    private Boolean subCommandCheck;

    public CommandHandler(APIPlugin plugin) {
        super(plugin);
        simpleCommandMap = getSimpleCommandMap();
        subCommandCheck = PropertyHandler.getInstance(plugin).subCommandCheck();
    }

    private SimpleCommandMap getSimpleCommandMap() {
        SimplePluginManager spm = (SimplePluginManager) getAPIPlugin().getServer().getPluginManager();
        Field f;
        SimpleCommandMap localSimpleCommandMap = null;
        try {
            f = SimplePluginManager.class.getDeclaredField("commandMap");
            f.setAccessible(true);
            localSimpleCommandMap = (SimpleCommandMap) f.get(spm);
            f.setAccessible(false);
        } catch (Exception e) {
            getLogger().log(Level.WARNING, "There went something wrong with getting the CommandMap.");
            getLogger().log(Level.WARNING, "Message: " + e.getMessage());
        }
        return localSimpleCommandMap;
    }

    public boolean registerCommand(Plugin plugin, APICommand command) {
        if(simpleCommandMap == null) {
            getLogger().log(Level.WARNING, plugin.getName() + " tried to register the command '" + command.getName() + "' but there is something wrong with the CommandMap");
            return false;
        }
        command.setSubCommandCheck(subCommandCheck);
        return simpleCommandMap.register(plugin.getName(), command);
    }


}
