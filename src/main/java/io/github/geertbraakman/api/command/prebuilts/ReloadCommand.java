package io.github.geertbraakman.api.command.prebuilts;

import io.github.geertbraakman.api.APIPlugin;
import io.github.geertbraakman.api.command.APICommand;
import io.github.geertbraakman.api.messaging.DefaultMessage;
import io.github.geertbraakman.api.reloading.Reloader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand extends APICommand {

    private Reloader reloader;

    public ReloadCommand(APIPlugin plugin) {
        super(plugin, "reload");
        reloader = plugin.getReloader();
        initialize();
    }

    private void initialize() {
        setPermission("xLib.reload");
        setDescription("This command will reload the plugin.");
}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        Player player = null;

        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (reloader.reloadPlugin()){
            sender.sendMessage(getMessageHandler().getMessage(DefaultMessage.RELOAD_SUCCESS, player));
        } else {
            sender.sendMessage(getMessageHandler().getMessage(DefaultMessage.RELOAD_FAILED, player));
        }

        return true;
    }
}
