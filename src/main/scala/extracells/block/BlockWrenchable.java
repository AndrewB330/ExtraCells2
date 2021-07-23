package extracells.block;

import appeng.api.implementations.items.IAEWrench;
import buildcraft.api.tools.IToolWrench;
import extracells.tileentity.TileEntityCertusTank;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public abstract class BlockWrenchable extends BlockBase {

    protected BlockWrenchable(Material material, float hardness, float resistance) {
        super(material);
        setHardness(hardness);
        setResistance(resistance);
    }

    protected BlockWrenchable(Material material) {
        super(material);
    }

    public abstract ItemStack getDropStack(World world, int x, int y, int z);

    private boolean wrenchBuildCraft(EntityPlayer player, ItemStack currentItem, int x, int y, int z) {
        try {
            if (currentItem == null ||
                    !(currentItem.getItem() instanceof IToolWrench) ||
                    !((IToolWrench) currentItem.getItem()).canWrench(player, x, y, z)) {
                return false;
            }
            ((IToolWrench) currentItem.getItem()).wrenchUsed(player, x, y, z);
            return true;
        } catch (Throwable ignore) {
            // TODO: No IToolWrench (??)
            return false;
        }
    }

    private boolean wrenchAE(EntityPlayer player, ItemStack currentItem, int x, int y, int z) {
        return currentItem != null &&
                currentItem.getItem() instanceof IAEWrench &&
                ((IAEWrench) currentItem.getItem()).canWrench(currentItem, player, x, y, z);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int blockID, float offsetX,
                                    float offsetY, float offsetZ) {
        if (super.onBlockActivated(world, x, y, z, player, blockID, offsetX, offsetY, offsetZ)) {
            return true;
        }

        ItemStack currentItem = player.inventory.getCurrentItem();

        if (player.isSneaking() && currentItem != null) {
            if (wrenchBuildCraft(player, currentItem, x, y, z) || wrenchAE(player, currentItem, x, y, z)) {
                dropBlockAsItem(world, x, y, z, getDropStack(world, x, y, z));
                world.setBlockToAir(x, y, z);
            }
        }

        return false;
    }
}
