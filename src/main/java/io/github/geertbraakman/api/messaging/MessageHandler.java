package io.github.geertbraakman.api.messaging;

import io.github.geertbraakman.Handler;
import io.github.geertbraakman.api.config.APIConfig;
import io.github.geertbraakman.api.config.ConfigHandler;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class MessageHandler extends Handler {
    
    private APIConfig messageFile;
    private Map<String, String> messages;
    private boolean usePlaceholders;

    public MessageHandler(Plugin plugin) {
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

    public String getMessage(String key, Player player, String defaultMessage){
        String message = defaultMessage;

        if(key != null){
            message = messages.get(key);
            if(messages.containsKey(key)){
                message = messages.get(key);
            }
        }
        return formatMessage(message, player);
    }

    public String formatMessage(String message, Player player){
        String prefix = messages.get("prefix");
        if(prefix == null) {
            prefix = "";
        }

        message = prefix + message;

        if(usePlaceholders) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
