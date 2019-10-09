package io.github.geertbraakman.api.command;

import io.github.geertbraakman.api.APIPlugin;
import io.github.geertbraakman.api.messaging.MessageHandler;
import org.apache.commons.lang.Validate;
import org.bukkit.command.*;

import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public abstract class APICommand extends Command {

    private ArrayList<APICommand> subCommands;
    private APIPlugin plugin;
    private boolean subCommandCheck = true;
    private MessageHandler messageHandler;

    public APICommand(APIPlugin plugin, String command) {
        super(command);
        this.plugin = plugin;
        this.messageHandler = plugin.getMessageHandler();
        subCommands = new ArrayList<>();
    }

    /**
     * Add an alias to this command
     * @param alias the alias you want to add.
     */
    public void addAlias(String alias) {
        List<String> aliases = getAliases();
        aliases.add(alias.toLowerCase());
        super.setAliases(aliases);
    }

    /**
     * set all the aliases you want to use at ones.
     * @param aliases the aliases you want to use.
     * @return this instance, for chaining.
     */
    @Override
    public Command setAliases(List<String> aliases) {
        List<String> known = getAliases();
        for (String s : aliases) {
            known.add(s.toLowerCase());
        }
        super.setAliases(known);
        return this;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {

        if (!plugin.isEnabled()) {
            return false;
        }

        if (!testPermission(sender)) {
            return true;
        }

        if (isSubCommandCheckEnabled() && isSubCommand(args)) {
            return executeSubCommand(sender, commandLabel, args);
        }

        try {
            return onCommand(sender, this, commandLabel, args);
        } catch (Exception ex) {
            throw new CommandException("Unhandled exception executing command '" + commandLabel + "' in plugin " + plugin.getDescription().getFullName(), ex);
        }
    }


    /**
     * Get all the subcommans registert for this command.
     *
     * @return the subcommans.
     */
    public List<APICommand> getSubCommands() {
        return subCommands;
    }

    /**
     * The method that triggers the onTabComplete method. Will be called by the CommandMap
     *
     * @param sender the sender of the command
     * @param alias  the alias used for this command
     * @param args   the args for this command
     *
     * @return all the values to show on Tab complete
     *
     * @throws IllegalArgumentException if 1 of the parameters is null.
     */
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args){
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        return onTabComplete(sender, this, alias, args);
    }

    /**
     * The Method that get excecuted when the command is called by the sender. Has to be overwritten by the plugin that made this command.
     *
     * @param sender  the sender of the command
     * @param command the command that is excecuted, the first in the chain.
     * @param alias   the alias used by the sender
     * @param args    the arguments
     *
     * @return If the command failed or not
     */
    public abstract boolean onCommand(CommandSender sender, Command command, String alias, String[] args);

    private boolean isSubCommand(String[] args) {

        if (args.length < 1) {
            return false;
        }

        String command = args[0];

        for (APICommand subCommand: subCommands) {
            if (subCommand.containsLabel(command)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Will look at the first argument and check if it's a sub-command of the main command.
     *
     * @param sender the sender of the command
     * @param alias  the alias that is used in the command
     * @param args   the arguments given by the sender
     *
     * @return if an sub-command has been found and called.
     */
    public boolean executeSubCommand(CommandSender sender, String alias, String[] args) {
        if (args.length == 0) {
            return false;
        }

        String command = args[0];

        for (APICommand subCommand : subCommands) {
            if (subCommand.containsLabel(command)) {
                return subCommand.execute(sender, command, removeFirstArgument(args));
            }
        }
        return false;
    }

    /**
     * check if a label is an alias of this command.
     * @param label the label you want to check.
     * @return if it's an alias.
     */
    private boolean containsLabel(String label) {
        return (getName().equalsIgnoreCase(label) || getAliases().contains(label.toLowerCase()) || getLabel().equalsIgnoreCase(label));
    }

    /**
     * Will get executed when a player tab-completes on this command.
     * @param sender the sender of the request
     * @param command the command of this event.
     * @param alias the alias used by the sender.
     * @param args the arguments
     * @return all the options that the player can choose of.
     */
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
       return getTabCompleteOptions(sender, args);
    }

    /**\
     * Returns the tab-complete options, also calls onTabComplete if a sub-command is found.
     * @param sender the sender of the request.
     * @param args the arguments
     * @return all the options that the player can choose of.
     */
    public List<String> getTabCompleteOptions(CommandSender sender, String[] args) {
        List<String> options = new ArrayList<>();

        if(args.length == 1){
            for (APICommand sc : subCommands) {
                if (sc.getPermission() == null || sender.hasPermission(sc.getPermission())) {
                    options.add(sc.getName());
                }
            }
        } else {
            for (APICommand subCommand : subCommands) {
                if (subCommand.containsLabel(args[1]) && (subCommand.getPermission() == null || sender.hasPermission(subCommand.getPermission()))) {
                    options = subCommand.onTabComplete(sender, subCommand, args[1], removeFirstArgument(args));
                }
            }
        }
        return options;
    }

    /**
     * Remove the first argument of an array.
     * @param args the array you want to modify
     * @return the array without the first argument.
     */
    public String[] removeFirstArgument(String[] args) {
        String[] strings = new String[args.length - 1];
        System.arraycopy(args, 1, strings, 0, args.length - 1);
        return strings;
    }

    /**
     * @return if sub-command check is enabled.
     */
    public boolean isSubCommandCheckEnabled() {
        return subCommandCheck;
    }

    /**
     * enable or disable the sub-command check
     * @param subCommandCheck the value you want to set this to.
     */
    public void setSubCommandCheck(boolean subCommandCheck) {
        this.subCommandCheck = subCommandCheck;
    }

    public void addSubCommand(APICommand subCommand){
        if(!subCommands.contains(subCommand)) {
            subCommands.add(subCommand);
        }
    }

    void setSubCommandCheck(Boolean value){
        subCommandCheck = value;
    }

    public Plugin getPlugin(){
        return this.plugin;
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }
}
