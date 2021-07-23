package extracells.part;

import extracells.registries.UpgradesNumber;
import extracells.util.inventory.IInventoryUpdateReceiver;
import extracells.util.inventory.InventoryUpgrades;

abstract public class PartWithUpgradesInventory extends PartWithInventory {
    public PartWithUpgradesInventory(int slots, UpgradesNumber upgradesMaxLimit) {
        super(new InventoryUpgrades(slots, upgradesMaxLimit), "upgradeInventory");
    }

    UpgradesNumber getUpgradesInstalled() {
        if (getInventory() instanceof InventoryUpgrades)
            return ((InventoryUpgrades) getInventory()).getUpgradesInstalled();
        return new UpgradesNumber();
    }

}
