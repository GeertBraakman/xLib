package io.github.geertbraakman.exceptions;

import org.bukkit.inventory.ItemStack;

public class ItemLoadException extends Exception {

    private final transient ItemStack loadedItem;

    public ItemLoadException (String message, ItemStack loadedItem) {
        super(message);
        this.loadedItem = loadedItem;
    }

    public ItemStack getLoadedItem() {
        return loadedItem;
    }
}
