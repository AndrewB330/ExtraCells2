package extracells.registries;

import appeng.api.config.Upgrades;
import extracells.integration.Integration;
import extracells.part.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

public enum PartEnum {
    FLUID_EXPORT("fluid.export", PartFluidExport.class, "fluid.IO",
            new UpgradesNumber(2, 2, 1, 0, 0, 0)),
    FLUID_IMPORT("fluid.import", PartFluidImport.class, "fluid.IO",
            new UpgradesNumber(2, 2, 1, 0, 0, 0)),
    FLUID_STORAGE("fluid.storage", PartFluidStorage.class, null,
            new UpgradesNumber(0, 0, 0, 1, 0, 0)),
    FLUID_TERMINAL("fluid.terminal", PartFluidTerminal.class),
    FLUID_LEVEL_EMITTER("fluid.levelemitter", PartFluidLevelEmitter.class),
    FLUID_PANE_ANNIHILATION("fluid.plane.annihilation", PartFluidPlaneAnnihilation.class, "fluid.plane"),
    FLUID_PANE_FORMATION("fluid.plane.formation", PartFluidPlaneFormation.class, "fluid.plane"),
    INTERFACE("interface", PartFluidInterface.class),
    FLUID_MONITOR("fluid.monitor", PartFluidStorageMonitor.class),
    FLUID_CONVERSION_MONITOR("fluid.conversion.monitor", PartFluidConversionMonitor.class),
    ORE_DICT_EXPORT_BUS("oredict.export", PartOreDictExporter.class);

    private final Integration.Mods mod;

    private static Pair<Upgrades, Integer> generatePair(Upgrades _upgrade, int integer) {
        return new MutablePair<Upgrades, Integer>(_upgrade, integer);
    }

    public static int getPartID(Class<? extends PartBase> partClass) {
        for (int i = 0; i < values().length; i++) {
            if (values()[i].getPartClass() == partClass)
                return i;
        }
        return -1;
    }

    public static int getPartID(PartBase partECBase) {
        return getPartID(partECBase.getClass());
    }

    private final String unlocalizedName;

    private final Class<? extends PartBase> partClass;

    private final String groupName;

    private UpgradesNumber upgradesMaxLimit = new UpgradesNumber();

    PartEnum(String _unlocalizedName, Class<? extends PartBase> _partClass) {
        this(_unlocalizedName, _partClass, null, (Integration.Mods) null);
    }

    PartEnum(String _unlocalizedName, Class<? extends PartBase> _partClass, String _groupName) {
        this(_unlocalizedName, _partClass, _groupName, (Integration.Mods) null);
    }

    PartEnum(String _unlocalizedName, Class<? extends PartBase> _partClass, String _groupName, Integration.Mods _mod) {
        this.unlocalizedName = "extracells.part." + _unlocalizedName;
        this.partClass = _partClass;
        this.groupName = _groupName == null || _groupName.isEmpty() ? null : "extracells." + _groupName;
        this.mod = _mod;
    }

    PartEnum(String _unlocalizedName, Class<? extends PartBase> _partClass, String _groupName, UpgradesNumber upgradesMaxLimit) {
        this(_unlocalizedName, _partClass, _groupName, (Integration.Mods) null);
        this.upgradesMaxLimit = upgradesMaxLimit;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public Class<? extends PartBase> getPartClass() {
        return this.partClass;
    }

    public String getStatName() {
        return StatCollector.translateToLocal(this.unlocalizedName + ".name");
    }

    public String getUnlocalizedName() {
        return this.unlocalizedName;
    }

    public UpgradesNumber getUpgradesMaxLimit() {
        return this.upgradesMaxLimit;
    }

    public PartBase newInstance(ItemStack partStack)
            throws IllegalAccessException, InstantiationException {
        PartBase partECBase = this.partClass.newInstance();
        partECBase.initializePart(partStack);
        return partECBase;
    }

    public Integration.Mods getMod() {
        return mod;
    }
}
