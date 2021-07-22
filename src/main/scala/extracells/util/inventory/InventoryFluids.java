package extracells.util.inventory;

import extracells.util.FluidUtil;
import net.minecraft.item.ItemStack;

public class InventoryFluids extends InventoryBase {

    public InventoryFluids(String _customName, int _size, int _stackLimit) {
        super(_customName, _size, _stackLimit);
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return FluidUtil.isFluidContainer(itemstack);
    }
}
