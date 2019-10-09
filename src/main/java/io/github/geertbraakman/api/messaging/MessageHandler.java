package io.github.geertbraakman.api.messaging;

import io.github.geertbraakman.Handler;
import io.github.geertbraakman.api.APIPlugin;
import io.github.geertbraakman.api.config.APIConfig;
import io.github.geertbraakman.api.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class MessageHandler extends Handler {

    private APIConfig messageFile;
    private Map<String, String> messages;
    public static boolean usePlaceholders = Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
    private boolean listContainsPrefix;
    private String defaultMessage;

    public MessageHandler(APIPlugin plugin) {
        super(plugin);

        messageFile = new APIConfig(plugin, "messages");
        getAPIPlugin().getConfigHandler().registerConfig(messageFile);

        if(usePlaceholders){
            getLogger().log(Level.INFO, "Hooked into PlaceholderAPI");
        }

        defaultMessage = "%prefix%&cThere is no default message available for &7%key%&c!";

        reload();
    }

    public boolean reload(){
        messages = new HashMap<>();

        if(messageFile.getFileConfiguration() == null){
            getLogger().log(Level.WARNING, "Could not find fileConfiguration while loading the messages!");
            return false;
        }

        ConfigurationSection section = messageFile.getFileConfiguration().getConfigurationSection("messages");

        if(section == null){
            getLogger().log(Level.WARNING, "Could not find message Section while loading the messages!");
            return false;
        }

        for(String key: section.getKeys(false)){
            messages.put(key, section.getString(key));
        }

        if(!messages.containsKey("prefix")){
            messages.put("prefix", "");
        }


        getLogger().log(Level.INFO, "Loaded " + messages.size() + " messages.");
        return true;
    }

    public String getMessage(String key, Player player) {
        return getMessage(key, player, null);
    }

    public String getMessage(String key, Player player, Map<String, String> map) {
        String message = "default";

        if(key != null){
            if(messages.containsKey(key)) {
                message = messages.get(key);
            }
        }

        switch (message.toLowerCase()){
            case "none":
                message = "";
            case "default":
                message = defaultMessage;
        }

        if(map == null) {
            map = new HashMap<>();
        }


        map.put("%prefix%", messages.get("prefix"));
        map.put("%key%", key);


        return Util.updatePlaceholders(message, player, map);
    }
    public void setDefaultMessage (String key, String message) {
        if (!messages.containsKey(key)){
            messages.put(key, message);
        } else if (messages.get(key).equals("default")) {
            messages.put(key, message);
        }
    }

}
