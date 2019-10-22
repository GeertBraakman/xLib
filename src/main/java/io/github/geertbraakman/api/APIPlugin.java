package io.github.geertbraakman.api;

import io.github.geertbraakman.api.command.CommandHandler;
import io.github.geertbraakman.api.config.ConfigHandler;
import io.github.geertbraakman.api.messaging.MessageHandler;
import io.github.geertbraakman.api.reloading.Reloader;
import io.github.geertbraakman.api.userinterface.UserInterfaceHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class APIPlugin extends JavaPlugin {

    private MessageHandler messageHandler;
    private CommandHandler commandHandler;
    private ConfigHandler configHandler;
    private Reloader reloader;
    private UserInterfaceHandler userInterfaceHandler;

    public MessageHandler getMessageHandler() {
        if(messageHandler == null) {
            messageHandler = new MessageHandler(this);
        }

        return messageHandler;
    }

    public CommandHandler getCommandHandler() {
        if(commandHandler == null) {
            commandHandler = new CommandHandler(this);
        }
        return commandHandler;
    }

    public ConfigHandler getConfigHandler() {
        if (configHandler == null) {
            configHandler = new ConfigHandler(this);
        }

        return configHandler;
    }

    public Reloader getReloader() {
        if (reloader == null) {
            reloader = new Reloader(this);
        }

        return reloader;
    }

    public UserInterfaceHandler getUserInterfaceHandler() {
        if (userInterfaceHandler == null) {
            userInterfaceHandler = new UserInterfaceHandler(this);
        }
        return userInterfaceHandler;
    }

}
