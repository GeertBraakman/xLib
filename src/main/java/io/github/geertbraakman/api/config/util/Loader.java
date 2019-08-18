package io.github.geertbraakman.api.config.util;

import io.github.com.geertbraakman.api.command.APICommand;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Loader {

    public static APICommand loadCommand(APICommand command, ConfigurationSection section){




        return command;
    }


    public static ItemStack loadItemStack(ConfigurationSection section, ItemStack defaultItemStack) {
        if(section == null){
            return  null;
        }

        if (defaultItemStack == null){
           defaultItemStack = new ItemStack(Material.STONE);
        }
        ItemMeta itemMeta = defaultItemStack.getItemMeta();

        String materialString = section.getString("material");
        Material material = defaultItemStack.getType();
        if (materialString != null && Material.getMaterial(materialString.toUpperCase()) != null){
            material = Material.getMaterial(materialString.toUpperCase());
        }

        String displayName = section.getString("display-name");
        if (displayName == null && itemMeta.hasDisplayName()) {

        }

        int durability = section.getInt("durability");
        int amount = section.getInt("amount");

        List<String> lore = section.getStringList("lore");




        ConfigurationSection enchantmentSection = section.getConfigurationSection("enchantments");

        List<String> enchantments = new ArrayList<>();



        return new ItemStack(Material.STICK);
    }


}
