package extracells.util.inventory;

import appeng.api.AEApi;
import appeng.api.definitions.IMaterials;
import net.minecraft.item.ItemStack;

public class InventoryUpgrades extends InventoryBase {

    public final static int UPGRADE_SPEED =    0b00000001;
    public final static int UPGRADE_CAPACITY = 0b00000010;
    public final static int UPGRADE_REDSTONE = 0b00000100;
    public final static int UPGRADE_INVERTED = 0b00001000;
    public final static int UPGRADE_CRAFTING = 0b00010000;

    public final static int UPGRADES_NONE = 0;

    public final static int UPGRADES_IO = UPGRADE_SPEED | UPGRADE_CAPACITY | UPGRADE_REDSTONE;

    private final int allowedUpgrades;

    public InventoryUpgrades(int _size, int allowedUpgrades) {
        super("upgrades", _size, 1);
        this.allowedUpgrades = allowedUpgrades;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        if (itemStack == null)
            return false;
        IMaterials materials = AEApi.instance().definitions().materials();
        if (materials.cardSpeed().isSameAs(itemStack) && (allowedUpgrades & UPGRADE_SPEED) > 0)
            return true;
        if (materials.cardCapacity().isSameAs(itemStack) && (allowedUpgrades & UPGRADE_CAPACITY) > 0)
            return true;
        if (materials.cardRedstone().isSameAs(itemStack) && (allowedUpgrades & UPGRADE_REDSTONE) > 0)
            return true;
        if (materials.cardInverter().isSameAs(itemStack) && (allowedUpgrades & UPGRADE_INVERTED) > 0)
            return true;
        if (materials.cardCrafting().isSameAs(itemStack) && (allowedUpgrades & UPGRADE_CRAFTING) > 0)
            return true;
        return false;
    }
}
