package extracells.util.inventory;

import extracells.util.FluidUtil;
import net.minecraft.item.ItemStack;

public class ECFluidInventory extends ECBaseInventory {

    public ECFluidInventory(String _customName, int _size, int _stackLimit, IInventoryUpdateReceiver _receiver) {
        super(_customName, _size, _stackLimit, _receiver);
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return FluidUtil.isFluidContainer(itemstack);
    }
}
