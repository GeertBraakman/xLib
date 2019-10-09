package io.github.geertbraakman.api.command.prebuilts;

import io.github.geertbraakman.api.APIPlugin;
import io.github.geertbraakman.api.command.APICommand;
import io.github.geertbraakman.api.reloading.Reloader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand extends APICommand {

    private Reloader reloader;

    public ReloadCommand(APIPlugin plugin) {
        super(plugin, "Reload");
        reloader = plugin.getReloader();
        initialize();
    }

    private void initialize() {
        setPermission("xLib.reload");
        setDescription("This command will reload the plugin.");
        getMessageHandler().setDefaultMessage("reload-success", "%prefix%&aThe reload was successfully!");
        getMessageHandler().setDefaultMessage("reload-failed", "%prefix%&cThe reload failed!");
}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        Player player = null;

        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (reloader.reloadPlugin()){
            sender.sendMessage(getMessageHandler().getMessage("Reload-Success", player));
        } else {
            sender.sendMessage(getMessageHandler().getMessage("Reload-Failed", player));
        }

        return true;
    }
}
