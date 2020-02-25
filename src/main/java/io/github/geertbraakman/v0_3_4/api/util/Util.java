package io.github.geertbraakman.v0_3_4.api.util;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Util {

    public static final boolean USE_PAPI = getPlaceholderAPI();

    /**
     * The constructor is private and empty so this class can only be used as a static class.
     */
    private Util() {
    }

    /**
     * Get if the placeholderAPI is installed, if so, send a message that the plugin hooked into placeholderAPI
     * @return if placeholderAPI is installed
     */
    private static boolean getPlaceholderAPI() {
        boolean temp = Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;

        if (temp) {
            Logger.getLogger("xLib").log(Level.INFO, "Hooked into PlaceholderAPI!");
        }

        return temp;
    }

    /**
     * Check if a string is an integer.
     *
     * @param string The string to be checked.
     * @return If the string is an integer.
     */
    public static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Set the Placeholders from the placeholder API. It will update them in the lore and the display-name.
     *
     * @param itemStack The ItemStack you want to update the placeholders in.
     * @param player    The player that wile be used to parse the placeholders.
     * @return The ItemStack with the placeholders updated.
     */
    public static ItemStack updatePlaceHolders(ItemStack itemStack, Player player) {
        return setPlaceholders(itemStack, player, null);
    }

    /**
     * Set placeholders in an ItemStack. This will include placeholders from the placeholder API. It will set
     * placeholders in the lore and the display-name.
     *
     * @param itemStack    The ItemStack you want to update the placeholders in.
     * @param player       The player that should be used to parse the placeholders.
     * @param placeholders A map with extra placeholders that are not in the placeholderAPI.
     * @return The ItemStack with the placeholders updated.
     */
    public static ItemStack setPlaceholders(ItemStack itemStack, Player player, Map<String, String> placeholders) {

        if (!itemStack.hasItemMeta()) {
            return itemStack;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta.hasDisplayName()) {
            itemMeta.setDisplayName(setPlaceholders(itemMeta.getDisplayName(), player, placeholders));
        }

        if (itemMeta.hasLore()) {
            List<String> lore = itemMeta.getLore();
            for (int i = 0; i < lore.size(); i++) {
                lore.set(i, setPlaceholders(lore.get(i), player, placeholders));
            }
            itemMeta.setLore(lore);
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /**
     * Set placeholders in a String. This will include placeholders from the placeholder API
     *
     * @param string       The string you want to set the placeholders in.
     * @param player       The player that should be used to parse the placeholders.
     * @param placeholders A map with extra placeholders that are not in the placeholderAPI.
     * @return The string with the placeholders set.
     */
    public static String setPlaceholders(String string, Player player, Map<String, String> placeholders) {
        if (string == null) {
            string = "";
        }

        if (placeholders != null) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                string = string.replace(entry.getKey(), entry.getValue());
            }
        }

        if (USE_PAPI) {
            string = PlaceholderAPI.setPlaceholders(player, string);
        }

        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static int parsePositiveInt(String input, int defaultValue) {
        int i = parseInt(input, defaultValue);

        if (i < 0) {
            i = 0;
        }
        return  i;
    }

    public static int parseInt(String input, int defaultValue) {
        if (!isInteger(input)) {
            return defaultValue;
        }

        return Integer.parseInt(input);
    }
}
