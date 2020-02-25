package io.github.geertbraakman.v0_3_4.api.userinterface.prebuild.submitting;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemSubmitEvent{

    private Player player;
    private List<ItemStack> submittedItems;

    public ItemSubmitEvent(Player player, List<ItemStack> submittedItems) {
        this.player = player;
        this.submittedItems = submittedItems;
    }

    public Player getPlayer() {
        return player;
    }

    public List<ItemStack> getSubmittedItems() {
        return submittedItems;
    }

}
