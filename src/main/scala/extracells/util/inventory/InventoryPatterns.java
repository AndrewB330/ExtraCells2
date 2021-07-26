package extracells.util.inventory;

import appeng.api.implementations.ICraftingPatternItem;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import extracells.util.FluidUtil;
import net.minecraft.item.ItemStack;

public class InventoryPatterns extends InventoryBase {

    public InventoryPatterns(String _customName, int _size) {
        super(_customName, _size, 1);
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack stack) {
        if (stack.getItem() instanceof ICraftingPatternItem) {
            // TODO: check this null passed instead of world, can crash something
            ICraftingPatternDetails details = ((ICraftingPatternItem) stack.getItem()).getPatternForItem(stack, null);
            return details != null;
        }
        return false;
    }
}
