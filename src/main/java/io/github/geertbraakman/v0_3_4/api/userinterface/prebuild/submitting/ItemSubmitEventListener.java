package io.github.geertbraakman.v0_3_4.api.userinterface.prebuild.submitting;

import org.bukkit.inventory.ItemStack;
import java.util.List;

public interface ItemSubmitEventListener {

    List<ItemStack> onSubmit(ItemSubmitEvent event);
}
