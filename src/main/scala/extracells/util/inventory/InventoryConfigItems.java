package extracells.util.inventory;

import appeng.util.Platform;
import net.minecraft.item.ItemStack;

public class InventoryConfigItems extends InventoryBase {

    private final ItemStack cellItem;

    public InventoryConfigItems(int _size, ItemStack cellItem) {
        super("config", _size, 1);
        this.cellItem = cellItem;
        this.readFromNBTAs(Platform.openNbtData(this.cellItem), "list");
    }

    @Override
    public void markDirty() {
        super.markDirty();
        this.writeToNBTAs(Platform.openNbtData(this.cellItem), "list");
    }
}
