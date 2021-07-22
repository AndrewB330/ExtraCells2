package extracells.util.inventory;

import extracells.util.FluidUtil;
import net.minecraft.item.ItemStack;

public class ECFluidInventory extends ECPrivateInventory {

    public ECFluidInventory(String _customName, int _size, int _stackLimit, IInventoryUpdateReceiver _receiver) {
        super(_customName, _size, _stackLimit, _receiver);
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return FluidUtil.isFluidContainer(itemstack);
    }
}
