package io.github.geertbraakman.api.command.prebuilts;

import io.github.geertbraakman.api.command.APICommand;
import io.github.geertbraakman.api.reloading.Reloader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ReloadCommand extends APICommand {

    private Reloader reloader;

    public ReloadCommand(Plugin plugin) {
        super(plugin, "Reload");
        reloader = Reloader.getInstance(plugin);
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
            sender.sendMessage(getMessageHandler().getMessage("Reload-Success", player, "&aReloaded Successfully!"));
        } else {
            sender.sendMessage(getMessageHandler().getMessage("Reload-Failed", player, "&cReload failed!"));
        }

        return true;
    }
}
