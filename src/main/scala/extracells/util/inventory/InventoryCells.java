package extracells.util.inventory;

import appeng.api.AEApi;
import appeng.api.storage.ICellRegistry;
import net.minecraft.item.ItemStack;

public class InventoryCells extends InventoryBase {

    ICellRegistry cellRegistry = AEApi.instance().registries().cell();

    public InventoryCells(String _customName, int _size, int _stackLimit) {
        super(_customName, _size, _stackLimit);
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack stack) {
        return this.cellRegistry.isCellHandled(stack);
    }
}
