package extracells.tileentity;

import extracells.util.inventory.InventoryBase;
import extracells.util.inventory.IInventoryUpdateReceiver;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileEntityWithInventory extends TileEntityBase implements IInventoryUpdateReceiver {
    // We need this for back-compatibility, so that inventories that has
    // different names than default will not disappear.

    private final String inventoryName;
    private final InventoryBase inventory;

    public TileEntityWithInventory(InventoryBase inventory, String inventoryName) {
        this.inventory = inventory;
        this.inventoryName = inventoryName;
        inventory.setReceiver(this);
    }

    public TileEntityWithInventory(InventoryBase inventory) {
        this(inventory, "inventory");
    }

    public InventoryBase getInventory() {
        return inventory;
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
