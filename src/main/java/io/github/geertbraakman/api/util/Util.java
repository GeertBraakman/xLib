package io.github.geertbraakman.api.util;

import io.github.geertbraakman.api.messaging.MessageHandler;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import java.util.List;
import java.util.Map;

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

    public static ItemStack updatePlaceholders(ItemStack itemStack, Player player, Map<String, String> placeholders) {

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


    public static String updatePlaceholders(String string, Player player, Map<String, String> placeholders) {

        if(placeholders != null) {
            for (String placeholder : placeholders.keySet()) {
                string = string.replace(placeholder, placeholders.get(placeholder));
            }
        }

        if(MessageHandler.usePlaceholders) {
            string = PlaceholderAPI.setPlaceholders(player, string);
        }

        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
