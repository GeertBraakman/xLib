package io.github.geertbraakman.api.config.util;

import io.github.geertbraakman.api.command.APICommand;
import io.github.geertbraakman.api.util.Util;
import io.github.geertbraakman.exceptions.ItemLoadException;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Loader {

    public static APICommand loadCommand(APICommand command, ConfigurationSection section) {


        return command;
    }


    public static ItemStack loadItemStack(ConfigurationSection section, ItemStack defaultItemStack) {

        // Check if there is a default ItemStack to return. If not creating on.
        if (defaultItemStack == null) {
            defaultItemStack = new ItemStack(Material.STONE);
        }

        // Check if there is a section to load from. If not returning the default ItemStack
        if (section == null) {
            return defaultItemStack;
        }

        // Get the default ItemMeta
        ItemMeta defaultItemMeta = defaultItemStack.getItemMeta();

        if (defaultItemMeta == null) {
            return null;
        }

        // Get the material String and parse it to a material. If it is not a valid material or the material is null then the default material will be used.
        String materialString = section.getString("material");

        if (materialString == null) {
            materialString = defaultItemStack.getType().name();
        }

        Material material = Material.getMaterial(materialString.toUpperCase());

        if (material == null) {
            material = defaultItemStack.getType();
        }

        // Create the itemStack
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta == null) {
            return null;
        }


        // Get and set the displayName
        String displayName = section.getString("display-name");

        if (displayName == null) {
            displayName = defaultItemMeta.getDisplayName();
        }

        if ("none".equals(displayName)) {
            displayName = "";
        }

        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));

        // Get and set the amount
        String amountString = section.getString("amount");
        if (amountString == null) {
            amountString = "1";
        }

        int amount = 1;

        if (Util.isInteger(amountString)) {
            amount = Integer.parseInt(amountString);
        }

        if (amount < 1) {
            amount = 1;
        }

        itemStack.setAmount(amount);

        // Get and set the lore
        List<String> lore = section.getStringList("lore");

        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));
        }

        itemMeta.setLore(lore);

        // Get and set the enchantments
        ConfigurationSection enchantmentSection = section.getConfigurationSection("enchantments");
        if (enchantmentSection != null) {
            for (String key : enchantmentSection.getKeys(false)) {
                Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(key.toLowerCase()));
                System.out.println("Key: " + key);
                if (enchantment != null) {
                    System.out.println("Enchantment: " + enchantment.toString());
                    String levelString = enchantmentSection.getString(key);
                    System.out.println("levelString: " + levelString);
                    if (levelString != null && Util.isInteger(levelString)) {
                        int level = Integer.parseInt(levelString);
                        System.out.println("Level: " + level);
                        itemMeta.addEnchant(enchantment, level, true);
                    }
                }
            }
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
