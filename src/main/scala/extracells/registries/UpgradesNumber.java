package extracells.registries;

import appeng.api.config.Upgrades;

import java.util.HashMap;
import java.util.Map;

public class UpgradesNumber {
    public int speedUpgrades = 0;
    public int capacityUpgrades = 0;
    public int redstoneUpgrades = 0;
    public int invertedUpgrades = 0;
    public int craftingUpgrades = 0;
    public int fuzzyUpgrades = 0;

    public UpgradesNumber() {
    }

    public UpgradesNumber(int speed, int capacity, int redstone, int inverted, int crafting, int fuzzy) {
        speedUpgrades = speed;
        capacityUpgrades = capacity;
        redstoneUpgrades = redstone;
        invertedUpgrades = inverted;
        craftingUpgrades = crafting;
        fuzzyUpgrades = fuzzy;
    }

    public Map<Upgrades, Integer> ToAeMap() {
        Map<Upgrades, Integer> map = new HashMap<>();

        if (speedUpgrades > 0) {
            map.put(Upgrades.SPEED, speedUpgrades);
        }
        if (capacityUpgrades > 0) {
            map.put(Upgrades.CAPACITY, capacityUpgrades);
        }
        if (redstoneUpgrades > 0) {
            map.put(Upgrades.REDSTONE, redstoneUpgrades);
        }
        if (invertedUpgrades > 0) {
            map.put(Upgrades.INVERTER, invertedUpgrades);
        }
        if (craftingUpgrades > 0) {
            map.put(Upgrades.CRAFTING, craftingUpgrades);
        }
        if (fuzzyUpgrades > 0) {
            map.put(Upgrades.FUZZY, fuzzyUpgrades);
        }

        return map;
    }
}
