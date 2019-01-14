package io.gtihub.GeertBraakman.command.premade;

import io.gtihub.GeertBraakman.command.APICommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class HelpSubCommand extends APICommand {

    private APICommand toHelpCommand;

    public HelpSubCommand(Plugin plugin, APICommand command) {
        super(plugin, "help");
        this.toHelpCommand = command;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        sender.sendMessage(ChatColor.DARK_GRAY + "----------------" + ChatColor.GOLD + " PlayerWarps "
                + ChatColor.DARK_GRAY + "----------------");

        StringBuilder aliases = new StringBuilder(toHelpCommand.getName());
        for (String al : toHelpCommand.getAliases()) {
            aliases.append(", ").append(al);
        }
        sender.sendMessage(ChatColor.DARK_GREEN + "aliases: " + ChatColor.DARK_GRAY + aliases);
        sender.sendMessage("");
        sender.sendMessage(ChatColor.GRAY + "Sub-Commands: ");


        for (APICommand sc : toHelpCommand.getSubCommands()) {
            if (sc.getPermission() == null || sender.hasPermission(sc.getPermission())) {
                TextComponent starter = new TextComponent("> ");
                starter.setColor(ChatColor.DARK_GRAY);

                String commandString = "/" + toHelpCommand.getName() + " " + sc.getName() + " " + createSubCommandString(sc.getSubCommands());
                TextComponent commandHelp = new TextComponent(commandString);
                commandHelp.setColor(ChatColor.DARK_GREEN);
                commandHelp.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, commandString));

                ComponentBuilder componentBuilder = new ComponentBuilder(ChatColor.GOLD + "Command: " + ChatColor.WHITE + sc.getName());
                componentBuilder.append("\n" + ChatColor.GOLD + "Description: " + ChatColor.WHITE + sc.getDescription());
                componentBuilder.append("\n" + ChatColor.GOLD + "Usage: " + ChatColor.WHITE + commandString);

                if (sc.getPermission() != null) {
                    componentBuilder.append("\n" + ChatColor.GOLD + "Permission: " + ChatColor.WHITE + sc.getPermission());
                }

                componentBuilder.append("\n");
                componentBuilder.append("\n" + ChatColor.GRAY + "Click to auto-complete.");

                commandHelp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, componentBuilder.create()));
                starter.addExtra(commandHelp);
                sender.spigot().sendMessage(starter);

            }
        }
        sender.sendMessage(ChatColor.DARK_GRAY + "--------------------------------------------");
        return true;
    }

    private String createSubCommandString(ArrayList<APICommand> subCommands) {
        if(subCommands.size() == 0){
            return "";
        }


        StringBuilder string = new StringBuilder("[");

        for(int i = 0; i < subCommands.size(); i++) {
            string.append(subCommands.get(i).getName());

            if(i < subCommands.size() -1){
                string.append(" | ");
            }
        }
        return string.toString();
    }
}
