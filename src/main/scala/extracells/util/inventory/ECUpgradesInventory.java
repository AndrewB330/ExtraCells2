package extracells.util.inventory;

import appeng.api.AEApi;
import net.minecraft.item.ItemStack;

public class ECUpgradesInventory extends ECBaseInventory {

    public ECUpgradesInventory( int _size, IInventoryUpdateReceiver _receiver) {
        super("upgrades", _size, 1, _receiver);
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        if (itemStack == null)
            return false;
        if (AEApi.instance().definitions().materials().cardCapacity().isSameAs(itemStack))
            return true;
        else if (AEApi.instance().definitions().materials().cardSpeed().isSameAs(itemStack))
            return true;
        else if (AEApi.instance().definitions().materials().cardRedstone().isSameAs(itemStack))
            return true;
        return false;
    }
}
