package io.github.geertbraakman.api.messaging;

import io.github.geertbraakman.Handler;
import io.github.geertbraakman.api.config.APIConfig;
import io.github.geertbraakman.api.config.ConfigHandler;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class MessageHandler extends Handler {

    private static List<MessageHandler> instances;

    public static MessageHandler getInstance(Plugin plugin) {
        if(instances == null) {
            instances = new ArrayList<>();
        }

       for(MessageHandler handler: instances) {
           if(handler.getPlugin().equals(plugin)){
               return handler;
           }
       }
       MessageHandler instance = new MessageHandler(plugin);

       instances.add(instance);
       return instance;
    }

    private APIConfig messageFile;
    private Map<String, String> messages;
    private boolean usePlaceholders;

    private MessageHandler(Plugin plugin) {
        super(plugin);
        messageFile = new APIConfig(plugin, "messages");
        ConfigHandler.getInstance(plugin).registerConfig(messageFile);
        usePlaceholders = plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
        if(usePlaceholders){
            getLogger().log(Level.INFO, "Hooked into PlaceholderAPI");
        }
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

        getLogger().log(Level.INFO, "Loaded " + messages.size() + " messages.");
        return true;
    }

    public String getMessage(String key, Player player){
        return getMessage(key, player, "&cThe key &7" + key + "&c can't be found, contact an administrator!");
    }

    public String getMessage(String key, Player player, String defaultMessage, boolean addPrefix){
        String message = "default";

        if(key != null){
            if(messages.containsKey(key)){
                message = "" + messages.get(key);
            }
        }


        switch (message){
            case "none":
                message = "";
            case "default":
                message = defaultMessage;
        }

        return formatMessage(message, player, addPrefix);
    }

    public String getMessage(String key, Player player, String defaultMessage){
        return getMessage(key, player, defaultMessage, true);
    }

    public String formatMessage(String message, Player player, boolean addPrefix){
        String prefix = messages.get("prefix") + " ";
        if(prefix.equals(" ") || !addPrefix) {
            prefix = "";
        }

        message = prefix + message;

        if(usePlaceholders) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
