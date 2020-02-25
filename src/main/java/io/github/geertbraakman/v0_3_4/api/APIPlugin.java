package io.github.geertbraakman.v0_3_4.api;

import io.github.geertbraakman.v0_3_4.api.command.CommandHandler;
import io.github.geertbraakman.v0_3_4.api.config.ConfigHandler;
import io.github.geertbraakman.v0_3_4.api.messaging.MessageHandler;
import io.github.geertbraakman.v0_3_4.api.reloading.Reloader;
import io.github.geertbraakman.v0_3_4.api.userinterface.UserInterfaceHandler;
import io.github.geertbraakman.v0_3_4.api.util.ItemHandler;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class APIPlugin extends JavaPlugin {

    private MessageHandler messageHandler;
    private CommandHandler commandHandler;
    private ConfigHandler configHandler;
    private Reloader reloader;
    private UserInterfaceHandler userInterfaceHandler;
    private ItemHandler itemHandler;

    public MessageHandler getMessageHandler() {
        if(messageHandler == null) {
            messageHandler = new MessageHandler(this);
        }

        return messageHandler;
    }

    public ItemHandler getItemHandler() {
        if(itemHandler == null) {
            itemHandler = new ItemHandler(this);
        }

        return itemHandler;
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
