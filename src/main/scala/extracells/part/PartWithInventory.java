package extracells.part;

import extracells.util.inventory.InventoryBase;
import extracells.util.inventory.IInventoryUpdateReceiver;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public abstract class PartWithInventory extends PartECBase implements IInventoryUpdateReceiver {
    // We need this for back-compatibility, so that inventories that has
    // different names than default will not disappear.

    private final String inventoryName;
    private final InventoryBase inventory;

    public PartWithInventory(InventoryBase inventory, String inventoryName) {
        this.inventory = inventory;
        this.inventoryName = inventoryName;
        inventory.setReceiver(this);
    }

    public PartWithInventory(InventoryBase inventory) {
        this(inventory, "inventory");
    }

    public InventoryBase getInventory() {
        return inventory;
    }

    @Override
    public void getDrops(List<ItemStack> drops, boolean wrenched) {
        super.getDrops(drops, wrenched);
        drops.addAll(inventory.getContent());
    }

    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        this.inventory.readFromNBTAs(data, this.inventoryName);
        onInventoryChanged();
    }

    public void writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        this.inventory.writeToNBTAs(data, this.inventoryName);
    }
}

