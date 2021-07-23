package extracells.util.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryBase implements IInventory {

    public final ItemStack[] slots; // todo: rename to stacks

    private final int stackLimit;
    private final String customName;

    private IInventoryUpdateReceiver receiver;

    public InventoryBase(String _customName, int _size, int _stackLimit) {
        this.slots = new ItemStack[_size];
        this.customName = _customName;
        this.stackLimit = _stackLimit;
    }

    public void setReceiver(IInventoryUpdateReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    /**
     * @return lit of all non-null and non-empty stacks in inventory
     */
    public List<ItemStack> getContent() {
        return Arrays
                .stream(slots)
                .filter((ItemStack stack) -> stack != null && stack.stackSize > 0)
                .collect(Collectors.toList());
    }

    @Override
    public ItemStack decrStackSize(int slotId, int amount) {
        if (this.slots[slotId] == null)
            return null;

        ItemStack itemstack;

        if (this.slots[slotId].stackSize <= amount) {
            itemstack = this.slots[slotId];
            this.slots[slotId] = null;
        } else {
            ItemStack temp = this.slots[slotId];
            itemstack = temp.splitStack(amount);
            this.slots[slotId] = temp;
            if (temp.stackSize == 0) {
                this.slots[slotId] = null;
            } else {
                this.slots[slotId] = temp;
            }
        }

        markDirty();

        return itemstack.stackSize == 0 ? null : itemstack;
    }

    @Override
    public String getInventoryName() {
        return this.customName;
    }

    @Override
    public int getInventoryStackLimit() {
        return this.stackLimit;
    }

    @Override
    public int getSizeInventory() {
        return this.slots.length;
    }

    @Override
    public ItemStack getStackInSlot(int slotId) {
        return this.slots[slotId];
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slotId) {
        return this.slots[slotId];
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    public void incrStackSize(int slotId, int amount) {
        ItemStack stack = this.slots[slotId];
        if (stack == null)
            return;
        int stackLimit = Math.min(getInventoryStackLimit(), stack.getMaxStackSize());
        stack.stackSize = Math.min(stackLimit, stack.stackSize + amount);
        markDirty();
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public void markDirty() {
        if (this.receiver != null)
            this.receiver.onInventoryChanged();
    }

    @Override
    public void setInventorySlotContents(int slotId, ItemStack itemstack) {
        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
            itemstack.stackSize = getInventoryStackLimit();
        }
        this.slots[slotId] = itemstack;

        markDirty();
    }

    public void readFromNBT(NBTTagList target) {
        if (target == null) {
            Arrays.fill(slots, null);
            return;
        }
        for (int i = 0; i < target.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = target.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot") & 255;

            if (j < this.slots.length) {
                this.slots[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
            }
        }
    }

    public void readFromNBTAs(final NBTTagCompound data, final String name) {
        // TODO: check why 10(??)
        final NBTTagList c = data.getTagList(name, 10);
        if (c != null) {
            this.readFromNBT(c);
        }
    }

    public void writeToNBT(NBTTagList target) {
        for (int i = 0; i < this.slots.length; ++i) {
            if (this.slots[i] != null) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte) i);
                this.slots[i].writeToNBT(nbttagcompound);
                target.appendTag(nbttagcompound);
            }
        }
    }

    public void writeToNBTAs(final NBTTagCompound data, final String name) {
        final NBTTagList c = new NBTTagList();
        this.writeToNBT(c);
        data.setTag(name, c);
    }
}
