package extracells.util.inventory;

import appeng.api.AEApi;
import appeng.api.storage.ICellRegistry;
import extracells.util.FluidUtil;
import net.minecraft.item.ItemStack;

public class ECCellInventory extends ECBaseInventory {

    ICellRegistry cellRegistry = AEApi.instance().registries().cell();

    public ECCellInventory(String _customName, int _size, int _stackLimit) {
        super(_customName, _size, _stackLimit);
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack stack) {
        return this.cellRegistry.isCellHandled(stack);
    }
}
