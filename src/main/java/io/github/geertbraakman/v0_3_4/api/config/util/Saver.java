package io.github.geertbraakman.v0_3_4.api.config.util;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Saver {

    public static Map<String, Object> saveItemStack(ItemStack itemStack) {
        Map<String, Object> map = new HashMap<>();


        map.put("material", itemStack.getType().name());
        map.put("amount", itemStack.getAmount());

        if (!itemStack.getEnchantments().isEmpty()) {
            Map<String, Object> enchantments = new HashMap<>();

            itemStack.getEnchantments().forEach((key, value) -> enchantments.put(key.getKey().getNamespace(), value));
            map.put("enchantments", enchantments);
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {

            if (itemMeta.hasDisplayName()) {
                map.put("display-name", itemMeta.getDisplayName());
            } else {
                map.put("display-name", "none");
            }

            if (itemMeta.hasLore()) {
                List<String> lore = new LinkedList<>();
                for (String line : itemMeta.getLore()) {
                    lore.add(line.replace("§", "&"));
                }

                map.put("lore", lore);
            }

            if (itemMeta.isUnbreakable()) {
                map.put("unbreakable", true);
            }

            if (itemMeta instanceof Damageable) {
                map.put("damage", ((Damageable) itemMeta).getDamage());
            }

            if (itemMeta instanceof SkullMeta) {
                map.put("skull-owner", ((SkullMeta) itemMeta).getOwningPlayer().getUniqueId().toString());
            }
        }

        return map;
    }

}
