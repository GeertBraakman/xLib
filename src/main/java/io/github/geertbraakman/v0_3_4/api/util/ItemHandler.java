package io.github.geertbraakman.api.util;

import io.github.geertbraakman.Handler;
import io.github.geertbraakman.api.APIPlugin;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class ItemHandler extends Handler {

    private Map<String, ItemStack> itemList;

    public ItemHandler(APIPlugin plugin) {
        super(plugin);
        itemList = new HashMap<>();
        loadValues();
    }

    public void loadValues() {
        ItemStack button = new ItemStack(Material.PISTON);
        ItemMeta buttonMeta = button.getItemMeta();
        buttonMeta.setDisplayName("Button");
        button.setItemMeta(buttonMeta);

        ItemStack decoration = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta decorationMeta = decoration.getItemMeta();
        decorationMeta.setDisplayName(" ");
        decoration.setItemMeta(decorationMeta);

        itemList.put("button", button);
        itemList.put("decoration", decoration);

    }

    public ItemStack getItem(String name) {
        return itemList.get(name);
    }

    @Override
    public boolean reload() {
        loadValues();
        return true;
    }
}
