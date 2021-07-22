package extracells.container;

import appeng.api.AEApi;
import appeng.api.implementations.guiobjects.IGuiItem;
import appeng.api.implementations.guiobjects.INetworkTool;
import appeng.api.util.DimensionalCoord;
import extracells.container.slot.SlotNetworkTool;
import extracells.container.slot.SlotUpgrades;
import extracells.part.PartFluidIO;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.function.Consumer;

public class ContainerBusFluidIO extends ECBaseContainer {
	private final PartFluidIO part;
	private Consumer<ItemStack> onTransferItemInSlot = null;

	public ContainerBusFluidIO(PartFluidIO part, EntityPlayer player) {
		super(player.inventory, part.getUpgradeInventory(), 8, 102);
		this.part = part;

		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (stack != null && AEApi.instance().definitions().items().networkTool().isSameAs(stack)) {
				DimensionalCoord coord = part.getHost().getLocation();
				IGuiItem guiItem = (IGuiItem) stack.getItem();
				INetworkTool networkTool = (INetworkTool) guiItem.getGuiObject(stack, coord.getWorld(), coord.x, coord.y, coord.z);
				for (int j = 0; j < 3; j++) {
					for (int k = 0; k < 3; k++) {
						addSlotToContainer(new SlotNetworkTool(networkTool, j + k * 3, 187 + k * 18, j * 18 + 102));
					}
				}
				return;
			}
		}
	}

	public void setTransferItemInSlotCallback(Consumer<ItemStack> callback) {
		onTransferItemInSlot = callback;
	}

	@Override
	protected void addSlots(IInventory inventoryPlayer) {
		for (int i = 0; i < 4; i++) {
			addSlotToContainer(new SlotUpgrades(inventory, i, 187, i * 18 + 8));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return part.isValid();
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		Slot slot = (Slot) this.inventorySlots.get(slotIndex);
		ItemStack slotStack = slot.getStack();
		if (onTransferItemInSlot != null) {
			onTransferItemInSlot.accept(slotStack);
		}
		return super.transferStackInSlot(player, slotIndex);
	}
}
