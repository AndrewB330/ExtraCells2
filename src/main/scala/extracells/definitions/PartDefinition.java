package extracells.definitions;

import appeng.api.definitions.IItemDefinition;
import extracells.api.definitions.IPartDefinition;
import extracells.registries.ItemEnum;
import extracells.registries.PartEnum;

public class PartDefinition implements IPartDefinition {

    public static final PartDefinition instance = new PartDefinition();

    @Override
    public IItemDefinition partConversionMonitor() {
        return new ItemItemDefinitions(ItemEnum.PARTITEM.getItem(),
                PartEnum.FLUID_CONVERSION_MONITOR.ordinal());
    }

    @Override
    public IItemDefinition partFluidAnnihilationPlane() {
        return new ItemItemDefinitions(ItemEnum.PARTITEM.getItem(),
                PartEnum.FLUID_PANE_ANNIHILATION.ordinal());
    }

    @Override
    public IItemDefinition partFluidExportBus() {
        return new ItemItemDefinitions(ItemEnum.PARTITEM.getItem(),
                PartEnum.FLUID_IMPORT.ordinal());
    }

    @Override
    public IItemDefinition partFluidFormationPlane() {
        return new ItemItemDefinitions(ItemEnum.PARTITEM.getItem(),
                PartEnum.FLUID_PANE_FORMATION.ordinal());
    }

    @Override
    public IItemDefinition partFluidImportBus() {
        return new ItemItemDefinitions(ItemEnum.PARTITEM.getItem(),
                PartEnum.FLUID_EXPORT.ordinal());
    }

    @Override
    public IItemDefinition partFluidLevelEmitter() {
        return new ItemItemDefinitions(ItemEnum.PARTITEM.getItem(),
                PartEnum.FLUID_LEVEL_EMITTER.ordinal());
    }

    @Override
    public IItemDefinition partFluidStorageBus() {
        return new ItemItemDefinitions(ItemEnum.PARTITEM.getItem(),
                PartEnum.FLUID_STORAGE.ordinal());
    }

    @Override
    public IItemDefinition partFluidTerminal() {
        return new ItemItemDefinitions(ItemEnum.PARTITEM.getItem(),
                PartEnum.FLUID_TERMINAL.ordinal());
    }

    @Override
    public IItemDefinition partInterface() {
        return new ItemItemDefinitions(ItemEnum.PARTITEM.getItem(),
                PartEnum.INTERFACE.ordinal());
    }

    @Override
    public IItemDefinition partOreDictExportBus() {
        return new ItemItemDefinitions(ItemEnum.PARTITEM.getItem(),
                PartEnum.ORE_DICT_EXPORT_BUS.ordinal());
    }

    @Override
    public IItemDefinition partStorageMonitor() {
        return new ItemItemDefinitions(ItemEnum.PARTITEM.getItem(),
                PartEnum.FLUID_MONITOR.ordinal());
    }

}
