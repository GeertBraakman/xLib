package io.github.geertbraakman.api.userinterface;

import io.github.geertbraakman.api.APIPlugin;
import io.github.geertbraakman.api.messaging.MessageHandler;
import io.github.geertbraakman.api.util.GUISize;
import io.github.geertbraakman.api.util.ItemHandler;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class UserInterface
{
  private ItemHandler itemHandler;
  private Inventory inventory;
  private String name;
  private HashMap<Integer, ItemStack> staticItems;
  private List<Player> playerList;
  private MessageHandler messageHandler;
  
  public UserInterface(String name, GUISize GUISize, APIPlugin plugin) {
    Validate.notNull(name, "Name cannot be null");
    Validate.notNull(GUISize, "size cannot be null");
    Validate.notNull(plugin, "Plugin cannot be null");

    this.itemHandler = plugin.getItemHandler();
    this.messageHandler = plugin.getMessageHandler();
    this.name = ChatColor.stripColor(name);
    inventory = Bukkit.createInventory(null, GUISize.getSize(), ChatColor.translateAlternateColorCodes('&', name));
    staticItems = new HashMap<>();
    playerList = new ArrayList<>();
  }
  
  protected void addStaticItem(ItemStack itemStack, int slot) {
    staticItems.put(slot, itemStack);
  }
  
  protected void addStaticItems() {
    for (Map.Entry<Integer,ItemStack> entry : staticItems.entrySet()) {
      int key = entry.getKey();
      ItemStack value = entry.getValue();
      inventory.setItem(key, value);
    }
  }
  
  public Inventory getInventory() {
    return inventory;
  }

  void click(InventoryClickEvent event) {
    onClick(event);
  }

  public abstract void onClick(InventoryClickEvent inventoryClickEvent);

  void close(InventoryCloseEvent inventoryCloseEvent){
    HumanEntity entity = inventoryCloseEvent.getPlayer();
    if(entity instanceof Player){
      Player player = (Player) entity;
      playerList.remove(player);
    }
    onClose(inventoryCloseEvent);
  }

  public abstract void onClose(InventoryCloseEvent inventoryCloseEvent);
  
  public void openFor(Player player) {
    if(onOpening(player)){
      playerList.add(player);
      player.openInventory(inventory);
    }
  }

  public abstract boolean onOpening(Player player);

  public String getName() {
    return name;
  }

  public List<Player> getPlayerList(){
    return playerList;
  }

  public Map<Integer, ItemStack> getStaticItems(){
    return staticItems;
  }

  public ItemHandler getItemHandler() {
    return itemHandler;
  }

  protected MessageHandler getMessageHandler() {
    return messageHandler;
  }
}
