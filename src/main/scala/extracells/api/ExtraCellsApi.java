package extracells.api;

import extracells.api.definitions.IBlockDefinition;
import extracells.api.definitions.IItemDefinition;
import extracells.api.definitions.IPartDefinition;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public interface ExtraCellsApi {

	public void addFluidToShowBlacklist(Class<? extends Fluid> clazz);

	public void addFluidToShowBlacklist(Fluid fluid);

	public void addFluidToStorageBlacklist(Class<? extends Fluid> clazz);

	public void addFluidToStorageBlacklist(Fluid fluid);

	public IBlockDefinition blocks();

	public boolean canFluidSeeInTerminal(Fluid fluid);

	public boolean canStoreFluid(Fluid fluid);

	/**
	 * @deprecated incorrect spelling
	 */
	@Deprecated
	public String getVerion();

	public String getVersion();

	@Deprecated
	public IWirelessFluidTermHandler getWirelessFluidTermHandler(ItemStack is);

	public IWirelessOldFluidTermHandler getWirelessTermHandler(ItemStack is);

	public boolean isWirelessFluidTerminal(ItemStack is);

	public IItemDefinition items();

	@Deprecated
	public ItemStack openPortableCellGui(EntityPlayer player, ItemStack stack, World world);

	public ItemStack openPortableFluidCellGui(EntityPlayer player, ItemStack stack, World world);

	@Deprecated
	public ItemStack openWirelessTerminal(EntityPlayer player, ItemStack stack, World world);

	public ItemStack openWirelessFluidTerminal(EntityPlayer player, ItemStack stack, World world);

	@Deprecated
	public ItemStack openWirelessTerminal(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, Long key);

	public IPartDefinition parts();

	public void registerWirelessTermHandler(IWirelessOldFluidTermHandler handler);

	@Deprecated
	public void registerWirelessFluidTermHandler(IWirelessFluidTermHandler handler);

	/**
	 * @deprecated incorrect spelling
	 */
	@Deprecated
	public void registryWirelessFluidTermHandler(IWirelessFluidTermHandler handler);
}
