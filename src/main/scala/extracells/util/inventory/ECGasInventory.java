package extracells.util.inventory;

import extracells.util.GasUtil;
import net.minecraft.item.ItemStack;

public class ECGasInventory extends ECPrivateInventory {

    public ECGasInventory(String _customName, int _size, int _stackLimit, IInventoryUpdateReceiver _receiver) {
        super(_customName, _size, _stackLimit, _receiver);
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return GasUtil.isGasContainer(itemstack);
    }
}
