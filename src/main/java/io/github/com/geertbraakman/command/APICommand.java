package io.github.com.geertbraakman.command;

import org.apache.commons.lang.Validate;
import org.bukkit.command.*;

import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public abstract class APICommand extends Command {

    private ArrayList<APICommand> subCommands;
    private Plugin plugin;


    public APICommand(Plugin plugin, String command){
        super(command);
        this.plugin = plugin;

    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        boolean success = false;

        if (!plugin.isEnabled()) {
            return false;
        }

        if (!testPermission(sender)) {
            return true;
        }

        try {
            success = this.onCommand(sender, this, commandLabel, args);
        } catch (Throwable ex) {
            throw new CommandException("Unhandled exception executing command '" + commandLabel + "' in plugin " + plugin.getDescription().getFullName(), ex);
        }

        if (!success && usageMessage.length() > 0) {
            for (String line : usageMessage.replace("<command>", commandLabel).split("\n")) {
                sender.sendMessage(line);
            }
        }

        return success;
    }

    public ArrayList<APICommand> getSubCommands() {
        return subCommands;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

       return onTabComplete(sender, this, alias, args);
    }

    public abstract boolean onCommand(CommandSender sender, Command command, String alias, String[] args);

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
        List<String> subValues = new ArrayList<>();
        if (args.length == 1) {
            for(APICommand sc: subCommands){
                if(sc.getPermission() == null || sender.hasPermission(sc.getPermission())) {
                    subValues.add(sc.getName());
                }
            }
            return subValues;
        }

        for(APICommand subCommand: subCommands){
            if(subCommand.getName().equalsIgnoreCase(args[1]) && (subCommand.getPermission() == null || sender.hasPermission(subCommand.getPermission()))){
                return subCommand.onTabComplete(sender, subCommand, args[1], removeFirstArgument(args));
            }
        }

        return subValues;
    }

   private String[] removeFirstArgument(String[] args) {
        String[] strings = new String[args.length -1];
       if (args.length - 1 >= 0) System.arraycopy(args, 1, strings, 0, args.length - 1);
        return strings;
    }
}
