package io.github.geertbraakman.api.messaging;

import io.github.geertbraakman.Handler;
import io.github.geertbraakman.api.APIPlugin;
import io.github.geertbraakman.api.config.APIConfig;
import io.github.geertbraakman.api.util.Util;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class MessageHandler extends Handler {

    private static final String PREFIX_KEY = "prefix";
    private static final String DEFAULT_KEY = "default";
    private static final String NONE_KEY = "none";

    private APIConfig messageFile;
    private Map<String, String> messages;
    private String defaultMessage;

    /**
     * The constructor of this class, it will load the message file and call the reload method. It will also set a default message.
     * @param plugin The plugin that is using this MessageHandler.
     */
    public MessageHandler(APIPlugin plugin) {
        super(plugin);

        messageFile = new APIConfig(plugin, "messages");
        getAPIPlugin().getConfigHandler().registerConfig(messageFile);
        defaultMessage = "%prefix%&cThere is no default message available for &7%key%&c!";

        reload();
    }

    /**
     * This methode will reload the message Handler, it will load all the messages from messages.yml.
     * @return if the reload was a success.
     */
    @Override
    public boolean reload() {
        messages = new HashMap<>();

        if (messageFile.getFileConfiguration() == null) {
            getLogger().log(Level.WARNING, "Could not find fileConfiguration while loading the messages!");
            return false;
        }

        ConfigurationSection section = messageFile.getFileConfiguration().getConfigurationSection("messages");

        if (section == null) {
            getLogger().log(Level.WARNING, "Could not find message Section while loading the messages!");
            return false;
        }

        for (String key : section.getKeys(false)) {
            messages.put(key, section.getString(key));
        }

        if (!messages.containsKey(PREFIX_KEY)) {
            messages.put(PREFIX_KEY, "");
        }

        getLogger().log(Level.INFO, "Loaded {} messages.", messages.size());
        return true;
    }

    /**
     * Get a message and set the placeholders from placeholderAPI.
     * @param key The message you want to get.
     * @param player The player that this message is send to.
     * @return The message.
     */
    public String getMessage(String key, Player player) {
        return getMessage(key, player, null);
    }

    /**
     * Get a message and set the placeholders from the placeholderAPI and the provided placeholders.
     * @param key The message you want to get.
     * @param player The player that this message is send to.
     * @param map A map with extra placeholders that are not provided by the placeholderAPI.
     * @return The message.
     */
    public String getMessage(String key, Player player, Map<String, String> map) {
        String message = DEFAULT_KEY;

        if (key != null && messages.containsKey(key)) {
            message = messages.get(key);
        }

        switch (message.toLowerCase()) {
            case NONE_KEY:
                message = "";
                break;
            case DEFAULT_KEY:
                message = defaultMessage;
                break;
            default:
                break;
        }

        if (map == null) {
            map = new HashMap<>();
        }

        map.put("%prefix%", messages.get(PREFIX_KEY));
        map.put("%key%", key);

        return Util.setPlaceholders(message, player, map);
    }

    /**
     * Set a default message for a specific key.
     * @param key The key from the message you want to set.
     * @param message The message you want to set.
     */
    public void setDefaultMessage(String key, String message) {
        if (!messages.containsKey(key) || messages.get(key).equals(DEFAULT_KEY)) {
            messages.put(key, message);
        }
    }

}
