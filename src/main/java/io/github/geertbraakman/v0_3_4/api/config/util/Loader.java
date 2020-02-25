package io.github.geertbraakman.v0_3_4.api.config.util;

import io.github.geertbraakman.v0_3_4.api.command.APICommand;
import io.github.geertbraakman.v0_3_4.api.messaging.DefaultMessage;
import io.github.geertbraakman.v0_3_4.api.util.Util;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.UUID;

public class Loader {

    private Loader() {}

    public static APICommand loadCommand(APICommand command, ConfigurationSection section) {
        if (command == null) {
            return null;
        }
        if (section == null) {
            return command;
        }

        String name = section.getString("name");
        if (name != null) {
            command.setName(name);
        }

        String label = section.getString("label");
        if (label  != null) {
            command.setLabel(label);
        }

        List<String> aliases = section.getStringList("aliases");
        if(!aliases.isEmpty()) {
            command.setAliases(aliases);
        }

        String description = section.getString("description");
        if (description != null) {
            command.setDescription(description);
        }

        String usageMessage = section.getString("usage-message");
        if (usageMessage != null) {
           command.setUsage(usageMessage);
        }

        String permission = section.getString("permission");
        if (permission != null) {
            command.setPermission(permission);
        }

        List<String> permissions = section.getStringList("permissions");
        if (!permissions.isEmpty()) {
           StringBuilder builder = new StringBuilder();
           for (String perm: permissions) {
               builder.append(perm).append(';');
           }
           command.setPermission(builder.toString());
        }

        return command;
    }

    public static ItemStack loadItemStack(ConfigurationSection section) {
        return loadItemStack(section, null);
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
        Material material = loadMaterial(section);

        if (material == null) {
            material = defaultItemStack.getType();
        }

        // Create the itemStack
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta == null) {
            return itemStack;
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
        int amount = Util.parsePositiveInt(section.getString("amount"), defaultItemStack.getAmount());

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
                if (enchantment != null) {
                    String levelString = enchantmentSection.getString(key);
                    if (levelString != null && Util.isInteger(levelString)) {
                        int level = Integer.parseInt(levelString);
                        itemMeta.addEnchant(enchantment, level, true);
                    }
                }
            }
        }

        // Get and set unbreakable
        boolean unbreakable = section.getBoolean("unbreakable", false);
        itemMeta.setUnbreakable(unbreakable);

        // Get and set durability
        if (itemMeta instanceof Damageable) {
            int durability = Util.parsePositiveInt(section.getString("damage"), ((Damageable) itemMeta).getDamage());
            ((Damageable) itemMeta).setDamage(durability);
        }

        // Skull creator
        if (itemMeta instanceof SkullMeta) {
            OfflinePlayer offlinePlayer = section.getOfflinePlayer("skull-owner");
            if (offlinePlayer == null) {
                String value = section.getString("skull-owner");
                if (value != null) {
                    offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(value));
                }
            }
            ((SkullMeta) itemMeta).setOwningPlayer(offlinePlayer);
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static Material loadMaterial(ConfigurationSection configurationSection) {
        if (configurationSection == null) {
            return null;
        }

        String materialString = configurationSection.getString("material");

        if (materialString == null) {
            return null;
        }

        return Material.getMaterial(materialString.toUpperCase());
    }

}
