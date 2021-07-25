package extracells.crafting;

import java.util.HashMap;
import java.util.Map;
import appeng.api.AEApi;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IAEStack;
import extracells.api.crafting.IFluidCraftingPatternDetails;
import extracells.registries.ItemEnum;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

public class CraftingPattern implements IFluidCraftingPatternDetails,
		Comparable<CraftingPattern> {

	protected final ICraftingPatternDetails pattern;

	private IAEFluidStack[] fluidsCondensed = null;
	private IAEFluidStack[] fluids = null;

	public CraftingPattern(ICraftingPatternDetails _pattern) {
		this.pattern = _pattern;
	}

	@Override
	public boolean canSubstitute() {
		return this.pattern.canSubstitute();
	}

	public int compareInt(int int1, int int2) {
		if (int1 == int2)
			return 0;
		if (int1 < int2)
			return -1;
		return 1;
	}

	@Override
	public int compareTo(CraftingPattern o) {
		return compareInt(o.getPriority(), this.getPriority());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		CraftingPattern other = (CraftingPattern) obj;
		if (this.pattern != null && other.pattern != null)
			return this.pattern.equals(other.pattern);
		return false;
	}

	@Override
	public IAEFluidStack[] getCondensedFluidInputs() {
		if (this.fluidsCondensed == null) {
			getCondensedInputs();
		}
		return this.fluidsCondensed;
	}

	@Override
	public IAEItemStack[] getCondensedInputs() {
		return removeFluidContainers(this.pattern.getCondensedInputs(), true);
	}

	@Override
	public IAEItemStack[] getCondensedOutputs() {
		return this.pattern.getCondensedOutputs();
	}

	@Override
	public IAEFluidStack[] getFluidInputs() {
		if (this.fluids == null) {
			getInputs();
		}
		return this.fluids;
	}

	@Override
	public IAEItemStack[] getInputs() {
		return removeFluidContainers(this.pattern.getInputs(), false);
	}

	@Override
	public ItemStack getOutput(InventoryCrafting craftingInv, World world) {
		IAEItemStack[] input = this.pattern.getInputs();
		for (int i = 0; i < input.length; i++) {
			IAEItemStack stack = input[i];
			if (stack != null
					&& FluidContainerRegistry.isFilledContainer(stack
							.getItemStack())) {
				try {
					craftingInv.setInventorySlotContents(i,
							input[i].getItemStack());
				} catch (Throwable e) {}
			} else if (stack != null
					&& stack.getItem() instanceof IFluidContainerItem) {
				try {
					craftingInv.setInventorySlotContents(i,
							input[i].getItemStack());
				} catch (Throwable e) {}
			}
		}
		ItemStack returnStack = this.pattern.getOutput(craftingInv, world);
		for (int i = 0; i < input.length; i++) {
			IAEItemStack stack = input[i];
			if (stack != null
					&& FluidContainerRegistry.isFilledContainer(stack
							.getItemStack())) {
				craftingInv.setInventorySlotContents(i, null);
			} else if (stack != null
					&& stack.getItem() instanceof IFluidContainerItem) {
				craftingInv.setInventorySlotContents(i, null);
			}
		}
		return returnStack;
	}

	@Override
	public IAEItemStack[] getOutputs() {
		return this.pattern.getOutputs();
	}

	@Override
	public ItemStack getPattern() {
		ItemStack p = this.pattern.getPattern();
		if (p == null)
			return null;
		ItemStack s = new ItemStack(ItemEnum.CRAFTINGPATTERN.getItem());
		NBTTagCompound tag = new NBTTagCompound();
		tag.setTag("item", p.writeToNBT(new NBTTagCompound()));
		s.setTagCompound(tag);
		return s;
	}

	@Override
	public int getPriority() {
		return this.pattern.getPriority();
	}

	@Override
	public boolean isCraftable() {
		return this.pattern.isCraftable();
	}

	@Override
	public boolean isValidItemForSlot(int slotIndex, ItemStack itemStack,
			World world) {
		return this.pattern.isValidItemForSlot(slotIndex, itemStack, world);
	}

	public IAEItemStack[] removeFluidContainers(IAEItemStack[] requirements,
			boolean isCondenced) {

		IAEItemStack[] returnStack = new IAEItemStack[requirements.length];
		IAEFluidStack[] fluidStacks = new IAEFluidStack[requirements.length];

		for(int i = 0; i < requirements.length; i++) {
			IAEItemStack currentRequirement = requirements[i];
			if(currentRequirement != null) {
				ItemStack current = currentRequirement.getItemStack();
				current.stackSize = 1;
				FluidStack fluid = null;
				if (FluidContainerRegistry.isFilledContainer(current)) {
					fluid = FluidContainerRegistry.getFluidForFilledItem(current);
				} else if (currentRequirement.getItem() instanceof IFluidContainerItem) {
					fluid = ((IFluidContainerItem) currentRequirement.getItem()).getFluid(current);
				}

				if (fluid == null) {
					returnStack[i] = currentRequirement;
				} else {
					fluid.amount *= currentRequirement.getStackSize();
					fluidStacks[i] = AEApi.instance().storage().createFluidStack(fluid);
				}
			}
		}

		if(isCondenced) {
			returnStack = condenceStacks(returnStack, new IAEItemStack[0]);
			this.fluidsCondensed = condenceStacks(fluidStacks, new IAEFluidStack[0]);
		} else {
			this.fluids = fluidStacks;
		}
		return returnStack;
	}

	@Override
	public void setPriority(int priority) {
		this.pattern.setPriority(priority);
	}

	@Override
	public int hashCode(){
		return this.pattern.hashCode();
	}

	// gamerforEA start: Added methods
	private static <T extends IAEStack<T>> T[] condenceStacks(T[] stacks, T[] arrayToReturn) {
		Map<T, T> tmpMap = new HashMap<>();

		for(T stack : stacks) {
			if(stack != null) {
				T tmpStack = tmpMap.get(stack);
				if(tmpStack == null) {
					tmpMap.put(stack, stack.copy());
				} else {
					tmpStack.add(stack);
				}
			}
		}

		return tmpMap.values().toArray(arrayToReturn);
	}
	// gamerforEA end
}
