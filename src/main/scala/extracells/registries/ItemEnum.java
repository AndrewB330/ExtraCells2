package extracells.registries;

import extracells.Extracells;
import extracells.integration.Integration;
import extracells.item.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public enum ItemEnum {
    PARTITEM("part.base", new ItemPartECBase()),
    FLUIDSTORAGE("storage.fluid", new ItemStorageFluid()),
    PHYSICALSTORAGE("storage.physical", new ItemStoragePhysical()),
    FLUIDPATTERN("pattern.fluid", new ItemFluidPattern()),
    FLUIDWIRELESSTERMINAL("terminal.fluid.wireless", ItemWirelessTerminalFluid.THIS()),
    STORAGECOMPONET("storage.component", new ItemStorageComponent()),
    STORAGECASING("storage.casing", new ItemStorageCasing()),
    FLUIDITEM("fluid.item", new ItemFluid(), null, null),
    FLUIDSTORAGEPORTABLE("storage.fluid.portable", ItemStoragePortableFluidCell.THIS()),
    CRAFTINGPATTERN("pattern.crafting", new ItemInternalCraftingPattern(), null, null);

    private final String internalName;
    private Item item;
    private Integration.Mods mod;

    ItemEnum(String _internalName, Item _item) {
        this(_internalName, _item, null);
    }

    ItemEnum(String _internalName, Item _item, Integration.Mods _mod) {
        this(_internalName, _item, _mod, Extracells.ModTab());
    }

    ItemEnum(String _internalName, Item _item, Integration.Mods _mod, CreativeTabs creativeTab) {
        this.internalName = _internalName;
        this.item = _item;
        this.item.setUnlocalizedName("extracells." + this.internalName);
        this.mod = _mod;
        if ((creativeTab != null) && (_mod == null || _mod.isEnabled()))
            this.item.setCreativeTab(Extracells.ModTab());
    }

    public ItemStack getDamagedStack(int damage) {
        return new ItemStack(this.item, 1, damage);
    }

    public String getInternalName() {
        return this.internalName;
    }

    public Item getItem() {
        return this.item;
    }

    public ItemStack getSizedStack(int size) {
        return new ItemStack(this.item, size);
    }

    public String getStatName() {
        return StatCollector.translateToLocal(this.item.getUnlocalizedName());
    }

    public Integration.Mods getMod() {
        return mod;
    }
}
