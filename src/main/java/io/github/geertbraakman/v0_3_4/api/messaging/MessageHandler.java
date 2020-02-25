package io.github.geertbraakman.v0_3_4.api.messaging;

import io.github.geertbraakman.v0_3_4.Handler;
import io.github.geertbraakman.v0_3_4.api.APIPlugin;
import io.github.geertbraakman.v0_3_4.api.config.APIConfig;
import io.github.geertbraakman.v0_3_4.api.util.Util;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

public class MessageHandler extends Handler {

  private APIConfig messageFile;
  private Map<String, String> messages;

  /**
   * The constructor of this class, it will load the message file and call the reload method. It
   * will also set a default message.
   *
   * @param plugin The plugin that is using this MessageHandler.
   */
  public MessageHandler(APIPlugin plugin) {
    super(plugin);

    messageFile = new APIConfig(plugin, "messages");
    getAPIPlugin().getConfigHandler().registerConfig(messageFile);

    reload();
  }

  /**
   * This methode will reload the message Handler, it will load all the messages from messages.yml.
   *
   * @return if the reload was a success.
   */
  @Override
  public boolean reload() {
    messages = new HashMap<>();

    for (DefaultMessage defaultMessage : DefaultMessage.values()) {
      if (!messages.containsKey(defaultMessage.key)) {
        messages.put(defaultMessage.key, defaultMessage.message);
      }
    }

    if (messageFile.getFileConfiguration() == null) {
      getLogger()
          .log(Level.WARNING, "Could not find fileConfiguration while loading the messages!");
      return false;
    }

    ConfigurationSection section =
        messageFile.getFileConfiguration().getConfigurationSection("messages");

    if (section == null) {
      getLogger().log(Level.WARNING, "Could not find message Section while loading the messages!");
      return false;
    }

    for (String key : section.getKeys(false)) {
      messages.put(key, section.getString(key));
    }

    getLogger().log(Level.INFO,  () -> "Loaded " + messages.size() + " messages.");
    return true;
  }

  /**
   * Get a message and set the placeholders from placeholderAPI.
   *
   * @param key The message you want to get.
   * @param player The player that this message is send to.
   * @return The message.
   */
  public String getMessage(String key, Player player) {
    return getMessage(key, player, null);
  }

  public String getMessage(DefaultMessage defaultMessage, Player player) {
      return getMessage(defaultMessage.key, player);
  }

  public String getMessage(DefaultMessage defaultMessage, Player player, Map<String, String> map) {
      return getMessage(defaultMessage.key, player, map);
  }

  /**
   * Get a message and set the placeholders from the placeholderAPI and the provided placeholders.
   *
   * @param key The message you want to get.
   * @param player The player that this message is send to.
   * @param map A map with extra placeholders that are not provided by the placeholderAPI.
   * @return The message.
   */
  public String getMessage(String key, Player player, Map<String, String> map) {
    String message = DefaultMessage.DEFAULT.key;

    if (key != null && messages.containsKey(key)) {
      message = messages.get(key);
    }

    DefaultMessage defaultMessage = DefaultMessage.fromKey(message);

    if (defaultMessage != null) {
      message = defaultMessage.message;
    }

    if (map == null) {
      map = new HashMap<>();
    }

    map.put("%prefix%", messages.get(DefaultMessage.PREFIX.key));
    map.put("%key%", key);

    return Util.setPlaceholders(message, player, map);
  }

  /**
   * Set a default message for a specific key.
   *
   * @param key The key from the message you want to set.
   * @param message The message you want to set.
   */
  public void setDefaultMessage(String key, String message) {
    if (!messages.containsKey(key)
        || messages.get(key).equals(DefaultMessage.DEFAULT.key)
        || (DefaultMessage.fromKey(key) != null
            && messages
                .get(key)
                .equals(Objects.requireNonNull(DefaultMessage.fromKey(key)).message))) {
      messages.put(key, message);
    }
  }
}
