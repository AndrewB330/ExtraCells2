package extracells.util.inventory;

import appeng.api.AEApi;
import appeng.api.config.Upgrades;
import appeng.api.definitions.IMaterials;
import appeng.api.implementations.items.IUpgradeModule;
import extracells.registries.UpgradesNumber;
import net.minecraft.item.ItemStack;

public class InventoryUpgrades extends InventoryBase {

    private final UpgradesNumber upgradesMaxLimit;
    private UpgradesNumber upgradesInstalled;

    public InventoryUpgrades(int _size, UpgradesNumber upgradesMaxLimit) {
        super("upgrades", _size, 1);
        this.upgradesMaxLimit = upgradesMaxLimit;
        this.upgradesInstalled = new UpgradesNumber();
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        if (itemStack == null)
            return false;
        IMaterials materials = AEApi.instance().definitions().materials();
        if (materials.cardSpeed().isSameAs(itemStack) &&
                (upgradesMaxLimit.speedUpgrades - upgradesInstalled.speedUpgrades) > 0)
            return true;
        if (materials.cardCapacity().isSameAs(itemStack) &&
                (upgradesMaxLimit.capacityUpgrades - upgradesInstalled.capacityUpgrades) > 0)
            return true;
        if (materials.cardRedstone().isSameAs(itemStack) &&
                (upgradesMaxLimit.redstoneUpgrades - upgradesInstalled.redstoneUpgrades) > 0)
            return true;
        if (materials.cardInverter().isSameAs(itemStack) &&
                (upgradesMaxLimit.invertedUpgrades - upgradesInstalled.invertedUpgrades) > 0)
            return true;
        if (materials.cardCrafting().isSameAs(itemStack) &&
                (upgradesMaxLimit.craftingUpgrades - upgradesInstalled.craftingUpgrades) > 0)
            return true;
        if (materials.cardFuzzy().isSameAs(itemStack) &&
                (upgradesMaxLimit.fuzzyUpgrades - upgradesInstalled.fuzzyUpgrades) > 0)
            return true;
        return false;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        updateUpgradesInstalled();
    }

    private void updateUpgradesInstalled() {
        upgradesInstalled = new UpgradesNumber();

        for (final ItemStack stack : getContent()) {
            if (stack == null || stack.getItem() == null || !(stack.getItem() instanceof IUpgradeModule)) {
                continue;
            }

            final Upgrades myUpgrade = ((IUpgradeModule) stack.getItem()).getType(stack);
            switch (myUpgrade) {
                case CAPACITY:
                    upgradesInstalled.capacityUpgrades++;
                    break;
                case FUZZY:
                    upgradesInstalled.fuzzyUpgrades++;
                    break;
                case REDSTONE:
                    upgradesInstalled.redstoneUpgrades++;
                    break;
                case SPEED:
                    upgradesInstalled.speedUpgrades++;
                    break;
                case INVERTER:
                    upgradesInstalled.invertedUpgrades++;
                    break;
                case CRAFTING:
                    upgradesInstalled.craftingUpgrades++;
                    break;
                default:
                    break;
            }
        }

        upgradesInstalled.speedUpgrades = Math.min(upgradesInstalled.speedUpgrades,
                upgradesMaxLimit.speedUpgrades);
        upgradesInstalled.capacityUpgrades = Math.min(upgradesInstalled.capacityUpgrades,
                upgradesMaxLimit.capacityUpgrades);
        upgradesInstalled.redstoneUpgrades = Math.min(upgradesInstalled.redstoneUpgrades,
                upgradesMaxLimit.redstoneUpgrades);
        upgradesInstalled.invertedUpgrades = Math.min(upgradesInstalled.invertedUpgrades,
                upgradesMaxLimit.invertedUpgrades);
        upgradesInstalled.craftingUpgrades = Math.min(upgradesInstalled.craftingUpgrades,
                upgradesMaxLimit.craftingUpgrades);
        upgradesInstalled.fuzzyUpgrades = Math.min(upgradesInstalled.fuzzyUpgrades,
                upgradesMaxLimit.fuzzyUpgrades);
    }

    public UpgradesNumber getUpgradesInstalled() {
        return upgradesInstalled;
    }
}
