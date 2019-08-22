package io.github.geertbraakman.api.util;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

public class Util {

    public static boolean isInteger(String string){
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static ItemStack updatePlaceHolders(ItemStack itemStack, Player player) {
        return updatePlaceholders(itemStack, player, null);
    }

    public static ItemStack updatePlaceholders(ItemStack itemStack, Player player, HashMap<String, String> placeholders) {
        boolean usePlaceholdersAPI = (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null);

        if(!itemStack.hasItemMeta()) {
            return itemStack;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta.hasDisplayName()){
            itemMeta.setDisplayName(updatePlaceholders(itemMeta.getDisplayName(), player, placeholders));
        }

        if (itemMeta.hasLore()){
            List<String> lore = itemMeta.getLore();
            for (int i = 0; i < lore.size(); i++) {
                lore.set(i, updatePlaceholders(lore.get(i), player, placeholders));
            }
            itemMeta.setLore(lore);
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }


    public static String updatePlaceholders(String string, Player player, HashMap<String, String> placeholders) {
        boolean usePlaceholdersAPI = (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null);

        if(placeholders != null) {
            for (String placeholder : placeholders.keySet()) {
                string = string.replace(placeholder, placeholders.get(placeholder));
            }
        }

        if(usePlaceholdersAPI) {
            string = PlaceholderAPI.setPlaceholders(player, string);
        }

        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
