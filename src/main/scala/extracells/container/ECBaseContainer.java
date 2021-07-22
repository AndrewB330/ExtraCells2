package extracells.container;

import extracells.util.ItemUtils;
import extracells.util.inventory.ECBaseInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class ECBaseContainer extends Container {

    private static final int SLOT_SIZE = 18;

    protected ECBaseInventory inventory;

    public ECBaseContainer(InventoryPlayer playerInventory, ECBaseInventory inventory, int xOffset, int yOffset) {
        this.inventory = inventory;
        bindPlayerInventory(playerInventory, xOffset, yOffset);
        addSlots(playerInventory);
        detectAndSendChanges();
    }

    protected abstract void addSlots(IInventory inventoryPlayer);

    protected void bindPlayerInventory(IInventory inventoryPlayer, int offsetX, int offsetY) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, offsetX + j * SLOT_SIZE, offsetY + i * SLOT_SIZE));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(inventoryPlayer, i, offsetX + i * SLOT_SIZE, offsetY + 58));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        ItemStack resultStack = null;
        Slot slot = (Slot) this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            resultStack = slotStack.copy();

            if (slotIndex < 36) {
                // Player -> Container
                if (!mergeItemStack(slotStack, 36, this.inventorySlots.size(), false)) {
                    return null;
                }
            } else if (!mergeItemStack(slotStack, 0, 36, true)) {
                // Container -> Player
                return null;
            }

            if (slotStack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
        }

        return resultStack;
    }

    @Override
    protected boolean mergeItemStack(ItemStack stack, int from, int to, boolean fromLast) {
        int slotIndex = (fromLast ? to - 1 : from);
        boolean changes = false;

        if (stack.isStackable()) {
            while (stack.stackSize > 0 && (fromLast && slotIndex >= from || !fromLast && slotIndex < to)) {
                Slot slot = (Slot) this.inventorySlots.get(slotIndex);
                ItemStack slotStack = slot.getStack();
                if (slotStack != null && ItemUtils.areItemEqualsIgnoreStackSize(slotStack, stack) && slot.isItemValid(stack)) {
                    int mergedSize = stack.stackSize + slotStack.stackSize;
                    int stackLimit = Math.min(slot.getSlotStackLimit(), stack.getMaxStackSize());
                    if (mergedSize <= stackLimit) {
                        stack.stackSize = 0;
                        slotStack.stackSize = mergedSize;
                        slot.onSlotChanged();
                        changes = true;
                    } else if (slotStack.stackSize < stackLimit) {
                        stack.stackSize -= stackLimit - slotStack.stackSize;
                        slotStack.stackSize = stackLimit;
                        slot.onSlotChanged();
                        changes = true;
                    }
                }

                slotIndex += (fromLast ? -1 : +1);
            }
        }

        slotIndex = (fromLast ? to - 1 : from);

        while (stack.stackSize > 0 && (fromLast && slotIndex >= from || !fromLast && slotIndex < to)) {
            Slot slot = (Slot) this.inventorySlots.get(slotIndex);
            int stackLimit = Math.min(slot.getSlotStackLimit(), stack.getMaxStackSize());
            if (slot.getStack() == null && slot.isItemValid(stack)) {
                if (stack.stackSize <= stackLimit) {
                    slot.putStack(stack.copy());
                    stack.stackSize = 0;
                } else {
                    slot.putStack(ItemUtils.copyAmount(stack, stackLimit));
                    stack.stackSize -= stackLimit;
                }
                slot.onSlotChanged();
                changes = true;
            }

            slotIndex += (fromLast ? -1 : +1);
        }

        return changes;
    }
}
