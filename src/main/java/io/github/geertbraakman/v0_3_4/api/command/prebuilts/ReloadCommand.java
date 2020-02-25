package io.github.geertbraakman.api.command.prebuilts;


import io.github.geertbraakman.api.APIPlugin;
import io.github.geertbraakman.api.command.APICommand;
import io.github.geertbraakman.api.messaging.DefaultMessage;
import io.github.geertbraakman.api.reloading.Reloader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This class is a prebuild for a ReloadCommand. When added as SubCommand to your plugin it will add the functionality to reload all classes that implements {@link io.github.geertbraakman.Handler}.
 *
 * Default permission: '[plugin.getName()].reload'
 * Default description: 'This command will reload the plugin.'
 */
public class ReloadCommand extends APICommand {

    private Reloader reloader;

    /**
     * Create a new instance of the ReloadCommand.
     * @param plugin The plugin that this command will reload.
     */
    public ReloadCommand(APIPlugin plugin) {
        super(plugin, "reload");
        reloader = plugin.getReloader();
        initialize();
    }

    /**
     * Initialize the command, This will set the permission and description.
     */
    private void initialize() {
        setPermission(getPlugin().getName() + ".reload");
        setDescription("This command will reload the plugin.");
}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        Player player = null;

        if (sender instanceof Player) {
            player = (Player) sender;
        }

        DefaultMessage message;

        if (reloader.reloadPlugin()){
            message = DefaultMessage.RELOAD_SUCCESS;
        } else {
            message = DefaultMessage.RELOAD_FAILED;
        }

        sender.sendMessage(getMessageHandler().getMessage(message, player));

        return true;
    }
}
