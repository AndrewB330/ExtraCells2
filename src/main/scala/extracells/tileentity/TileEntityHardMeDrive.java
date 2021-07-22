package extracells.tileentity;


import appeng.api.AEApi;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.events.MENetworkCellArrayUpdate;
import appeng.api.networking.security.IActionHost;
import appeng.api.storage.*;
import appeng.api.util.AECableType;
import appeng.api.util.DimensionalCoord;
import extracells.api.IECTileEntity;
import extracells.gridblock.ECGridBlockHardMEDrive;
import extracells.util.inventory.ECBaseInventory;
import extracells.util.inventory.ECCellInventory;
import extracells.util.inventory.IInventoryUpdateReceiver;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class TileEntityHardMeDrive extends TileWithInventory implements IActionHost, IECTileEntity, ICellContainer {

    private  int priority = 0;
    boolean isFirstGridNode = true;
    byte[] cellStatuses = new byte[3];
    List<IMEInventoryHandler> fluidHandlers = new ArrayList<IMEInventoryHandler>();
    List<IMEInventoryHandler> itemHandlers = new ArrayList<IMEInventoryHandler>();
    private final ECGridBlockHardMEDrive gridBlock = new ECGridBlockHardMEDrive(this);

    IGridNode node = null;

    public TileEntityHardMeDrive() {
        super(new ECCellInventory("extracells.part.drive", 3, 1));
    }


    @Override
    public void blinkCell(int i) {

    }

    @Override
    public IGridNode getActionableNode() {
        return getGridNode(ForgeDirection.UNKNOWN);
    }

    @Override
    public List<IMEInventoryHandler> getCellArray(StorageChannel channel) {
        if (!isActive())
            return new ArrayList<IMEInventoryHandler>();
        return channel == StorageChannel.ITEMS ? this.itemHandlers
                : this.fluidHandlers;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public DimensionalCoord getLocation() {
        return new DimensionalCoord(this);
    }

    @Override
    public double getPowerUsage() {
        return 0;
    }

    @Override
    public IGridNode getGridNode(ForgeDirection forgeDirection) {
        if (isFirstGridNode && hasWorldObj() && !getWorldObj().isRemote){
            isFirstGridNode = false;
            try{
                node = AEApi.instance().createGridNode(gridBlock);
                node.updateState();
            }catch (Exception e){
                isFirstGridNode = true;
            }
        }

        return node;
    }

    @Override
    public AECableType getCableConnectionType(ForgeDirection forgeDirection) {
        return AECableType.SMART;
    }

    @Override
    public void securityBreak() {

    }

    @Override
    public void saveChanges(IMEInventory imeInventory) {

    }

    //TODO
    boolean isActive(){
        return  true;
    }

    public int getColorByStatus(int status) {
        switch (status) {
            case 1:
                return 0x00FF00;
            case 2:
                return 0xFFFF00;
            case 3:
                return 0xFF0000;
            default:
                return 0x000000;
        }
    }

    @Override
    public void onInventoryChanged() {
        this.itemHandlers = updateHandlers(StorageChannel.ITEMS);
        this.fluidHandlers = updateHandlers(StorageChannel.FLUIDS);
        for (int i = 0; i < this.cellStatuses.length; i++) {
            ItemStack stackInSlot = this.getInventory().getStackInSlot(i);
            IMEInventoryHandler inventoryHandler = AEApi.instance()
                    .registries().cell()
                    .getCellInventory(stackInSlot, null, StorageChannel.ITEMS);
            if (inventoryHandler == null)
                inventoryHandler = AEApi
                        .instance()
                        .registries()
                        .cell()
                        .getCellInventory(stackInSlot, null,
                                StorageChannel.FLUIDS);

            ICellHandler cellHandler = AEApi.instance().registries().cell()
                    .getHandler(stackInSlot);
            if (cellHandler == null || inventoryHandler == null) {
                this.cellStatuses[i] = 0;
            } else {
                this.cellStatuses[i] = (byte) cellHandler.getStatusForCell(
                        stackInSlot, inventoryHandler);
            }
        }
        IGridNode node = getGridNode(ForgeDirection.UNKNOWN);
        if (node != null) {
            IGrid grid = node.getGrid();
            if (grid != null) {
                grid.postEvent(new MENetworkCellArrayUpdate());
            }
            getWorldObj().markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    private List<IMEInventoryHandler> updateHandlers(StorageChannel channel) {
        ICellRegistry cellRegistry = AEApi.instance().registries().cell();
        List<IMEInventoryHandler> handlers = new ArrayList<IMEInventoryHandler>();
        for (int i = 0; i < this.getInventory().getSizeInventory(); i++) {
            ItemStack cell = this.getInventory().getStackInSlot(i);
            if (cellRegistry.isCellHandled(cell)) {
                IMEInventoryHandler cellInventory = cellRegistry
                        .getCellInventory(cell, null, channel);
                if (cellInventory != null)
                    handlers.add(cellInventory);
            }
        }
        return handlers;
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        writeToNBT(nbtTag);
        int i = 0;
        for (byte aCellStati : this.cellStatuses) {
            nbtTag.setByte("status#" + i, aCellStati);
            i++;
        }
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
    }
}
