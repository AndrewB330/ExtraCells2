package extracells.container.slot;

import appeng.api.implementations.items.IUpgradeModule;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotUpgrades extends Slot {
    IInventory inventory;

    public SlotUpgrades(IInventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.inventory = inventory;
    }

    @Override
    public boolean isItemValid(ItemStack itemstack) {
        return itemstack.getItem() instanceof IUpgradeModule &&
                ((IUpgradeModule) itemstack.getItem()).getType(itemstack) != null &&
                this.inventory.isItemValidForSlot(this.slotNumber, itemstack);
    }
}
