package extracells.tileentity;

import appeng.api.networking.IGrid;
import appeng.api.networking.IGridHost;
import appeng.api.networking.IGridNode;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;;


abstract class TileEntityBase extends TileEntity {

    IStorageGrid getStorageGrid(ForgeDirection side) {
        if (!(this instanceof IGridHost)) {
            return null;
        }
        IGridHost host = (IGridHost) this;
        IGridNode node = host.getGridNode(side);
        if (node == null) {
            return null;
        }
        IGrid grid = node.getGrid();
        if (grid == null) {
            return null;
        }
        return grid.getCache(IStorageGrid.class);
    }

    IMEMonitor<IAEFluidStack> getFluidInventory(ForgeDirection side) {
        IStorageGrid storageGrid = getStorageGrid(side);
        if (storageGrid == null) {
            return null;
        } else {
            return storageGrid.getFluidInventory();
        }
    }

    IMEMonitor<IAEItemStack> getItemInventory(ForgeDirection side) {
        IStorageGrid storageGrid = getStorageGrid(side);
        if (storageGrid == null) {
            return null;
        } else {
            return storageGrid.getItemInventory();
        }
    }
}
