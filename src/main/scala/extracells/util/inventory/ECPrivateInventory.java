package extracells.util.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ECPrivateInventory implements IInventory {

    public final ItemStack[] slots; // todo: rename to stacks
    private final int stackLimit;

    private final String customName;
    private final IInventoryUpdateReceiver receiver;

    public ECPrivateInventory(String _customName, int _size, int _stackLimit) {
        this(_customName, _size, _stackLimit, null);
    }

    public ECPrivateInventory(String _customName, int _size, int _stackLimit, IInventoryUpdateReceiver _receiver) {
        this.slots = new ItemStack[_size];
        this.customName = _customName;
        this.stackLimit = _stackLimit;
        this.receiver = _receiver;
    }

    @Override
    public void closeInventory() {
    }

    public List<ItemStack> getContent() {
        return Arrays.stream(slots).filter(Objects::nonNull).collect(Collectors.toList());
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
    public void openInventory() {
		// NOBODY needs this!
    }

    public void readFromNBT(NBTTagList nbtList) {
        if (nbtList == null) {
            for (int i = 0; i < slots.length; i++) {
                slots[i] = null;
            }
            return;
        }
        for (int i = 0; i < nbtList.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbtList.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot") & 255;

            if (j >= 0 && j < this.slots.length) {
                this.slots[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
            }
        }
    }

    @Override
    public void setInventorySlotContents(int slotId, ItemStack itemstack) {
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
        this.slots[slotId] = itemstack;

        markDirty();
    }

    public NBTTagList writeToNBT() {
        NBTTagList nbtList = new NBTTagList();

        for (int i = 0; i < this.slots.length; ++i) {
            if (this.slots[i] != null) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte) i);
                this.slots[i].writeToNBT(nbttagcompound);
                nbtList.appendTag(nbttagcompound);
            }
        }
        return nbtList;
    }
}
