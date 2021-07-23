package extracells.block;

import appeng.api.implementations.items.IAEWrench;
import buildcraft.api.tools.IToolWrench;
import extracells.network.ChannelHandler;
import extracells.registries.BlockEnum;
import extracells.render.RenderHandler;
import extracells.tileentity.TileEntityCertusTank;
import extracells.util.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class BlockCertusTank extends BlockWrenchable {

    IIcon breakIcon;
    IIcon topIcon;
    IIcon bottomIcon;
    IIcon sideIcon;
    IIcon sideMiddleIcon;
    IIcon sideTopIcon;
    IIcon sideBottomIcon;

    public final static float SIZE = 0.875f; // x and z size of the block

    public BlockCertusTank() {
        super(Material.glass, 2.0F, 10.0F);
        setBlockBounds(
                0.5f - SIZE / 2.0f, 0.0F, 0.5f - SIZE / 2.0f,
                0.5f + SIZE / 2.0f, 1.0F, 0.5f + SIZE / 2.0f
        );
    }

    @Override
    public boolean canRenderInPass(int pass) {
        RenderHandler.renderPass = pass;
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityCertusTank();
    }

    @Override
    public ItemStack getDropStack(World world, int x, int y, int z) {
        TileEntity worldTE = world.getTileEntity(x, y, z);
        if (worldTE instanceof TileEntityCertusTank) {
            ItemStack dropStack = new ItemStack(BlockEnum.CERTUSTANK.getBlock(), 1);

            NBTTagCompound tileEntity = new NBTTagCompound();
            ((TileEntityCertusTank) worldTE).getTank().writeToNBT(tileEntity);

            if (!tileEntity.hasKey("Empty")) {
                dropStack.setTagCompound(new NBTTagCompound());
                dropStack.stackTagCompound.setTag("tileEntity", tileEntity);
            }

            return dropStack;

        }
        return null;
    }

    @Override
    public IIcon getIcon(int side, int b) {
        switch (b) {
            case 1:
                return this.sideTopIcon;
            case 2:
                return this.sideBottomIcon;
            case 3:
                return this.sideMiddleIcon;
            default:
                return side == 0 ? this.bottomIcon : side == 1 ? this.topIcon
                        : this.sideIcon;
        }
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal(getUnlocalizedName() + ".name");
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return getDropStack(world, x, y, z);
    }

    @Override
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public int getRenderType() {
        return RenderHandler.getId();
    }

    @Override
    public String getUnlocalizedName() {
        return super.getUnlocalizedName().replace("tile.", "");
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int blockID, float offsetX,
                                    float offsetY, float offsetZ) {
        if (super.onBlockActivated(world, x, y, z, player, blockID, offsetX, offsetY, offsetZ)) {
            return true;
        }

        ItemStack currentItem = player.inventory.getCurrentItem();

        if (currentItem == null) {
            return false;
        }

        FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(currentItem);

        TileEntityCertusTank tank = (TileEntityCertusTank) world.getTileEntity(x, y, z);

        if (fluid != null) {
            // Fill tank with fluid
            int amountInitial = fluid.amount;
            int amountFilled = tank.fill(ForgeDirection.UNKNOWN, fluid, true);

            if (amountFilled != 0 && !player.capabilities.isCreativeMode) {
                ItemStack remaining = currentItem.getItem().getContainerItem(currentItem);

                if (amountFilled < amountInitial) {
                    FluidStack fluidRemaining = new FluidStack(fluid.getFluid(), amountInitial - amountFilled);
                    remaining = FluidContainerRegistry.fillFluidContainer(fluidRemaining, remaining);
                }

                if (currentItem.stackSize > 1) {
                    player.inventory.decrStackSize(player.inventory.currentItem, 1);
                    ItemUtils.addItemToPlayerInventory(player, remaining);
                } else {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, remaining);
                }
            }
        } else {
            // Drain fluid from tank
            FluidStack available = tank.getTankInfo(ForgeDirection.UNKNOWN)[0].fluid;
            if (available == null) {
                return false;
            }

            ItemStack filled = FluidContainerRegistry.fillFluidContainer(available, currentItem);

            fluid = FluidContainerRegistry.getFluidForFilledItem(filled);

            if (fluid == null) {
                return false;
            }

            if (!player.capabilities.isCreativeMode) {
                if (currentItem.stackSize > 1) {
                    player.inventory.decrStackSize(player.inventory.currentItem, 1);
                    ItemUtils.addItemToPlayerInventory(player, filled);
                } else {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, filled);
                }
            }
            tank.drain(ForgeDirection.UNKNOWN, fluid.amount, true);
        }

        return true;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z,
                                      Block neighborBlock) {
        if (!world.isRemote) {
            ChannelHandler.sendPacketToAllPlayers(world.getTileEntity(x, y, z).getDescriptionPacket(), world);
        }
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.breakIcon = iconRegister.registerIcon("extracells:certustank");
        this.topIcon = iconRegister.registerIcon("extracells:CTankTop");
        this.bottomIcon = iconRegister.registerIcon("extracells:CTankBottom");
        this.sideIcon = iconRegister.registerIcon("extracells:CTankSide");
        this.sideMiddleIcon = iconRegister.registerIcon("extracells:CTankSideMiddle");
        this.sideTopIcon = iconRegister.registerIcon("extracells:CTankSideTop");
        this.sideBottomIcon = iconRegister.registerIcon("extracells:CTankSideBottom");
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
}
