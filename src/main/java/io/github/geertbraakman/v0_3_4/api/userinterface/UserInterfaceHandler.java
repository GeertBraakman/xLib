package io.github.geertbraakman.api.userinterface;

import io.github.geertbraakman.Handler;
import io.github.geertbraakman.api.APIPlugin;
import io.github.geertbraakman.api.util.ItemHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class UserInterfaceHandler extends Handler implements Listener
{

  private List<UserInterface> userInterfaces;
  private ItemHandler itemHandler;

  public UserInterfaceHandler(APIPlugin plugin)
  {
    super(plugin);
    userInterfaces = new ArrayList<>();
    this.itemHandler = plugin.getItemHandler();
  }
  
  public void registerUI(UserInterface userInterface) {
    if (!userInterfaces.contains(userInterface)) {
      userInterfaces.add(userInterface);
    }
  }
  
  @EventHandler
  public void onClick(InventoryClickEvent event) {
    Inventory inventory = event.getClickedInventory();
    UserInterface userInterface = getUI(inventory);
    if (userInterface != null) {
      userInterface.click(event);
    }
  }
  
  @EventHandler
  public void onClose(InventoryCloseEvent event)
  {
    Inventory inventory = event.getInventory();
    UserInterface userInterface = getUI(inventory);
    if (userInterface != null) {
      userInterface.close(event);
    }
  }
  
  private UserInterface getUI(Inventory inventory)
  {
    if (inventory != null) {
      for (UserInterface userInterface : userInterfaces) {
        if (userInterface.getInventory().equals(inventory)) {
          return userInterface;
        }
      }
    }
    return null;
  }
  
  public void unRegister(UserInterface userInterface)
  {
    userInterfaces.remove(userInterface);
  }

  @Override
  public boolean reload(){
    closeAllInventories();
    return true;
  }

  public void closeAllInventories(){
    for(UserInterface userInterface : userInterfaces){
      for(Player player: userInterface.getPlayerList()){
        player.closeInventory();
      }
    }
  }

  public ItemHandler getItemHandler() {
    return itemHandler;
  }
}
