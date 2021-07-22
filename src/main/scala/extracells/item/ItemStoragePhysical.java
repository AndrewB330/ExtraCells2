package extracells.item;

import appeng.api.AEApi;
import appeng.api.config.FuzzyMode;
import appeng.api.implementations.items.IStorageCell;
import appeng.api.storage.*;
import appeng.api.storage.data.IAEItemStack;
import extracells.Extracells;
import extracells.registries.ItemEnum;
import extracells.util.inventory.InventoryConfigItems;
import extracells.util.inventory.InventoryUpgrades;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemStoragePhysical extends ItemECBase implements IStorageCell {

    public static final String[] suffixes = {"256k", "1024k", "4096k", "16384k"};

    public static final int[] bytes_cell = {262144, 1048576, 4194304, 16777216};
    public static final int[] types_cell = {63, 63, 63, 63};
    private IIcon[] icons;
    private final int MAX_POWER = 32000;

    public ItemStoragePhysical() {
        setMaxStackSize(1);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player,
                               List list, boolean par4) {
        ICellRegistry cellRegistry = AEApi.instance().registries().cell();
        IMEInventoryHandler<IAEItemStack> invHandler = cellRegistry
                .getCellInventory(itemStack, null, StorageChannel.ITEMS);
        ICellInventoryHandler inventoryHandler = (ICellInventoryHandler) invHandler;
        ICellInventory cellInv = inventoryHandler.getCellInv();
        long usedBytes = cellInv.getUsedBytes();

        list.add(String.format(StatCollector
                        .translateToLocal("extracells.tooltip.storage.physical.bytes"),
                usedBytes, cellInv.getTotalBytes()));
        list.add(String.format(StatCollector
                        .translateToLocal("extracells.tooltip.storage.physical.types"),
                cellInv.getStoredItemTypes(), cellInv.getTotalItemTypes()));
        if (usedBytes > 0)
            list.add(String.format(
                    StatCollector
                            .translateToLocal("extracells.tooltip.storage.physical.content"),
                    cellInv.getStoredItemCount()));
    }

    @Override
    public int getBytesPerType(ItemStack cellItem) {
        return Extracells.dynamicTypes() ?
                bytes_cell[MathHelper.clamp_int(cellItem.getItemDamage(), 0, suffixes.length - 1)] / 128 : 8;
    }

    @Override
    @Deprecated
    public int BytePerType(ItemStack cellItem) {
        return getBytesPerType(cellItem);
    }

    @Override
    public int getBytes(ItemStack cellItem) {
        return bytes_cell[MathHelper.clamp_int(cellItem.getItemDamage(), 0, suffixes.length - 1)];
    }

    @Override
    public IInventory getConfigInventory(ItemStack is) {
        return new InventoryConfigItems(63, is);
    }

    @Override
    public FuzzyMode getFuzzyMode(ItemStack is) {
        if (!is.hasTagCompound())
            is.setTagCompound(new NBTTagCompound());
        return FuzzyMode.values()[is.getTagCompound().getInteger("fuzzyMode")];
    }

    @Override
    public IIcon getIconFromDamage(int dmg) {
        return this.icons[MathHelper.clamp_int(dmg, 0, suffixes.length - 1)];
    }

    @Override
    public double getIdleDrain() {
        return 0;
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        return EnumRarity.epic;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void getSubItems(Item item, CreativeTabs creativeTab, List itemList) {
        for (int i = 0; i < suffixes.length; i++) {
            itemList.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public int getTotalTypes(ItemStack stack) {
        return types_cell[MathHelper.clamp_int(stack.getItemDamage(), 0, suffixes.length - 1)];
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "extracells.item.storage.physical." + suffixes[stack.getItemDamage()];
    }

    @Override
    public IInventory getUpgradesInventory(ItemStack stack) {
        // TODO: check which upgrades can be places
        return new InventoryUpgrades(2, InventoryUpgrades.UPGRADES_NONE);
    }

    @Override
    public boolean isBlackListed(ItemStack cellItem, IAEItemStack requestedAddition) {
        return false;
    }

    @Override
    public boolean isEditable(ItemStack is) {
        return true;
    }

    @Override
    public boolean isStorageCell(ItemStack i) {
        return true;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        if (itemStack == null)
            return null;
        if (!entityPlayer.isSneaking())
            return itemStack;
        IMEInventoryHandler<IAEItemStack> invHandler = AEApi.instance().registries().cell().getCellInventory(itemStack, null, StorageChannel.ITEMS);
        ICellInventoryHandler inventoryHandler = (ICellInventoryHandler) invHandler;
        ICellInventory cellInv = inventoryHandler.getCellInv();
        if (cellInv.getUsedBytes() == 0 && entityPlayer.inventory.addItemStackToInventory(ItemEnum.STORAGECASING.getDamagedStack(0)))
            return ItemEnum.STORAGECOMPONET.getDamagedStack(itemStack.getItemDamage());
        return itemStack;
    }

    @Override
    public void registerIcons(IIconRegister iconRegister) {
        this.icons = new IIcon[suffixes.length];

        for (int i = 0; i < suffixes.length; ++i) {
            this.icons[i] = iconRegister.registerIcon("extracells:storage.physical." + suffixes[i]);
        }
    }

    @Override
    public void setFuzzyMode(ItemStack stack, FuzzyMode mode) {
        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setInteger("fuzzyMode", mode.ordinal());
    }

    @Override
    public boolean storableInStorageCell() {
        return false;
    }
}
