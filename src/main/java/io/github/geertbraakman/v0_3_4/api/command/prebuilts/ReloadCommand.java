package io.github.geertbraakman.v0_3_4.api.command.prebuilts;


import io.github.geertbraakman.v0_3_4.api.APIPlugin;
import io.github.geertbraakman.v0_3_4.api.command.APICommand;
import io.github.geertbraakman.v0_3_4.api.messaging.DefaultMessage;
import io.github.geertbraakman.v0_3_4.api.reloading.Reloader;
import io.github.geertbraakman.v0_3_4.Handler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This class is a prebuild for a ReloadCommand. When added as SubCommand to your plugin it will add the functionality to reload all classes that implements {@link Handler}.
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

        sender.sendMessage(getPlugin().getMessageHandler().getMessage(message, player));

        return true;
    }
}
