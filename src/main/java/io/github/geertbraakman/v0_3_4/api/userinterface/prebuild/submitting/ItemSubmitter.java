package io.github.geertbraakman.api.userinterface.prebuild.submitting;

import com.google.common.collect.Iterables;
import io.github.geertbraakman.api.APIPlugin;
import io.github.geertbraakman.api.messaging.DefaultMessage;
import io.github.geertbraakman.api.util.GUISize;
import io.github.geertbraakman.api.userinterface.UserInterface;
import io.github.geertbraakman.exceptions.InvalidSizeException;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ItemSubmitter extends UserInterface {

  private int buttonSlot = 31;
  private int decorationStarSlot = 27;
  private ItemSubmitEventListener itemSubmitEventListener;

  public ItemSubmitter(String name, APIPlugin plugin, ItemSubmitEventListener listener) {
    super(name, GUISize.FOUR_ROWS, plugin);
    this.itemSubmitEventListener = listener;
    initializeStaticItems();
  }

  public ItemSubmitter(
      String name, GUISize guiSize, APIPlugin plugin, ItemSubmitEventListener listener)
      throws InvalidSizeException {
    this(name, plugin, listener);
    if (guiSize.getSize() < GUISize.TWO_ROWS.getSize()) {
      throw new InvalidSizeException(guiSize.getSize(), GUISize.TWO_ROWS.getSize());
    }

    this.buttonSlot = guiSize.getSize() - 5;
    this.decorationStarSlot = guiSize.getSize() - 9;
  }

  private void initializeStaticItems() {

    ItemStack decoration = getItemHandler().getItem("decoration");

    for (int i = decorationStarSlot; i < getInventory().getSize(); i++) {
      if (i == buttonSlot) {
        addStaticItem(getItemHandler().getItem("button"), buttonSlot);
      } else {
        addStaticItem(decoration, i);
      }
    }
  }

  @Override
  public void onClick(InventoryClickEvent inventoryClickEvent) {
    if (getStaticItems().containsValue(inventoryClickEvent.getCurrentItem())) {
      inventoryClickEvent.setCancelled(true);
      CompletableFuture.runAsync(
          () -> {
            ItemStack clickedItem = inventoryClickEvent.getCurrentItem();
            if (clickedItem != null && clickedItem.equals(getStaticItems().get(buttonSlot))) {
              Inventory inventory = inventoryClickEvent.getClickedInventory();

              if (inventory == null) {
                inventoryClickEvent
                    .getWhoClicked()
                    .sendMessage(
                        getMessageHandler().getMessage(DefaultMessage.INTERNAL_ERROR, null));
                return;
              }
              HumanEntity humanEntity = inventoryClickEvent.getWhoClicked();

              if (!(humanEntity instanceof Player)) {
                inventoryClickEvent
                    .getWhoClicked()
                    .sendMessage(
                        getMessageHandler().getMessage(DefaultMessage.INTERNAL_ERROR, null));
                return;
              }
              Player player = (Player) humanEntity;

              List<ItemStack> itemsToSubmit = getItemsToSubmit(inventory);
              inventory.clear();
              addStaticItems();

              ItemSubmitEvent itemSubmitEvent = new ItemSubmitEvent(player, itemsToSubmit);
              List<ItemStack> itemsToReturn = itemSubmitEventListener.onSubmit(itemSubmitEvent);

              rewardItems(player, itemsToReturn);
            }
          });
    }
  }

  public List<ItemStack> getItemsToSubmit(Inventory inventory) {
    List<ItemStack> items = new ArrayList<>(Arrays.asList(inventory.getContents()));
    Iterables.removeIf(items, Objects::isNull);
    items.removeAll(getStaticItems().values());
    return items;
  }

  @Override
  public void onClose(InventoryCloseEvent inventoryCloseEvent) {
    List<ItemStack> itemStacks = getItemsToSubmit(inventoryCloseEvent.getInventory());
    for (ItemStack itemStack : itemStacks) {
      inventoryCloseEvent.getPlayer().getInventory().addItem(itemStack);
    }
  }

  @Override
  public boolean onOpening(Player player) {
    addStaticItems();
    return true;
  }

  public void rewardItems(Player player, List<ItemStack> items) {
    Inventory inventory = player.getInventory();
    System.out.println(items.size());
    HashMap<Integer, ItemStack> didNotFit = inventory.addItem(items.toArray(new ItemStack[0]));

    if (!didNotFit.isEmpty()) {
      Map<String, String> map = new HashMap<>();
      map.put("%amount%", String.valueOf(didNotFit.size()));

      player.sendMessage(getMessageHandler().getMessage(DefaultMessage.NOT_ENOUGH_SPACE, player, map));
      for (ItemStack item: didNotFit.values()) {
        player.getWorld().dropItem(player.getLocation(), item);
      }
    }
  }
}
