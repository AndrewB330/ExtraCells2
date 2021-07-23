package extracells.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TileEntityCertusTank extends TileEntity implements IFluidHandler {

    public static final int CAPACITY = 32000;

    private final FluidTank tank = new FluidTank(CAPACITY);

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return drain(from, new FluidStack(fluid, 1), false) != null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return fill(from, new FluidStack(fluid, 1), false) == 1;
    }

	/*public void compareAndUpdate() {
		if (!this.worldObj.isRemote) {
			FluidStack current = this.tank.getFluid();

			if (current != null) {
				if (this.lastBeforeUpdate != null) {
					if (Math.abs(current.amount - this.lastBeforeUpdate.amount) >= 500) {
						ChannelHandler.sendPacketToAllPlayers(
								getDescriptionPacket(), this.worldObj);
						this.lastBeforeUpdate = current.copy();
					} else if (this.lastBeforeUpdate.amount < this.tank
							.getCapacity()
							&& current.amount == this.tank.getCapacity()
							|| this.lastBeforeUpdate.amount == this.tank
									.getCapacity()
							&& current.amount < this.tank.getCapacity()) {
						ChannelHandler.sendPacketToAllPlayers(
								getDescriptionPacket(), this.worldObj);
						this.lastBeforeUpdate = current.copy();
					}
				} else {
					ChannelHandler.sendPacketToAllPlayers(
							getDescriptionPacket(), this.worldObj);
					this.lastBeforeUpdate = current.copy();
				}
			} else if (this.lastBeforeUpdate != null) {
				ChannelHandler.sendPacketToAllPlayers(getDescriptionPacket(),
						this.worldObj);
				this.lastBeforeUpdate = null;
			}
		}
	}*/

    //TODO implement multiblock logic
	/*public FluidStack drain(FluidStack fluid, boolean doDrain,
			boolean findMainTank) {
		if (findMainTank) {
			int yOff = 0;
			TileEntity offTE = this.worldObj.getTileEntity(this.xCoord,
					this.yCoord + yOff, this.zCoord);
			TileEntityCertusTank mainTank = this;
			while (true) {
				if (offTE != null && offTE instanceof TileEntityCertusTank) {
					Fluid offFluid = ((TileEntityCertusTank) offTE).getFluid();
					if (offFluid != null && offFluid == fluid.getFluid()) {
						mainTank = (TileEntityCertusTank) this.worldObj
								.getTileEntity(this.xCoord, this.yCoord + yOff,
										this.zCoord);
						yOff++;
						offTE = this.worldObj.getTileEntity(this.xCoord,
								this.yCoord + yOff, this.zCoord);
						continue;
					}
				}
				break;
			}

			return mainTank != null ? mainTank.drain(fluid, doDrain, false)
					: null;
		}

		FluidStack drained = this.tank.drain(fluid.amount, doDrain);
		compareAndUpdate();

		if (drained == null || drained.amount < fluid.amount) {
			TileEntity offTE = this.worldObj.getTileEntity(this.xCoord,
					this.yCoord - 1, this.zCoord);
			if (offTE instanceof TileEntityCertusTank) {
				TileEntityCertusTank tank = (TileEntityCertusTank) offTE;
				FluidStack externallyDrained = tank.drain(new FluidStack(
						fluid.getFluid(), fluid.amount
								- (drained != null ? drained.amount : 0)),
						doDrain, false);

				if (externallyDrained != null)
					return new FluidStack(fluid.getFluid(),
							(drained != null ? drained.amount : 0)
									+ externallyDrained.amount);
				else
					return drained;
			}
		}

		return drained;
	}*/

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (resource == null || tank.getFluid() == null || tank.getFluid().getFluid() != resource.getFluid())
            return null;

        return drain(from, resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        if (this.tank.getFluid() == null)
            return null;

        return tank.drain(maxDrain, doDrain);
    }

    //TODO implement multiblock logic
	/*public int fill(FluidStack fluid, boolean doFill, boolean findMainTank) {
		if (findMainTank) {
			int yOff = 0;
			TileEntity offTE = this.worldObj.getTileEntity(this.xCoord,
					this.yCoord - yOff, this.zCoord);
			TileEntityCertusTank mainTank = this;
			while (true) {
				if (offTE != null && offTE instanceof TileEntityCertusTank) {
					Fluid offFluid = ((TileEntityCertusTank) offTE).getFluid();
					if (offFluid == null || offFluid == fluid.getFluid()) {
						mainTank = (TileEntityCertusTank) this.worldObj
								.getTileEntity(this.xCoord, this.yCoord - yOff,
										this.zCoord);
						yOff++;
						offTE = this.worldObj.getTileEntity(this.xCoord,
								this.yCoord - yOff, this.zCoord);
						continue;
					}
				}
				break;
			}

			return mainTank != null ? mainTank.fill(fluid, doFill, false) : 0;
		}

		int filled = this.tank.fill(fluid, doFill);
		compareAndUpdate();

		if (filled < fluid.amount) {
			TileEntity offTE = this.worldObj.getTileEntity(this.xCoord,
					this.yCoord + 1, this.zCoord);
			if (offTE instanceof TileEntityCertusTank) {
				TileEntityCertusTank tank = (TileEntityCertusTank) offTE;
				return filled
						+ tank.fill(new FluidStack(fluid.getFluid(), fluid.amount
								- filled), doFill, false);
			}
		}

		return filled;
	}*/

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (resource == null || this.tank.getFluid() != null && resource.getFluid() != this.tank.getFluid().getFluid())
            return 0;

        return tank.fill(resource, doFill);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        writeToNBT(nbtTag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
    }

    public Fluid getFluid() {
        FluidStack tankFluid = this.tank.getFluid();
        return tankFluid != null && tankFluid.amount > 0 ? tankFluid.getFluid() : null;
    }

    public float getFilledFraction() {
        return (float) this.tank.getFluidAmount() / this.tank.getCapacity();
    }

    //TODO implement multiblock logic
	/*
	public FluidTankInfo[] getTankInfo(boolean goToMainTank) {
		if (!goToMainTank)
			return new FluidTankInfo[] { this.tank.getInfo() };

		int amount = 0, capacity = 0;
		Fluid fluid = null;

		int yOff = 0;
		TileEntity offTE = this.worldObj.getTileEntity(this.xCoord, this.yCoord
				- yOff, this.zCoord);
		TileEntityCertusTank mainTank = this;
		while (true) {
			if (offTE != null && offTE instanceof TileEntityCertusTank) {
				if (((TileEntityCertusTank) offTE).getFluid() == null
						|| ((TileEntityCertusTank) offTE).getFluid() == getFluid()) {
					mainTank = (TileEntityCertusTank) this.worldObj
							.getTileEntity(this.xCoord, this.yCoord - yOff,
									this.zCoord);
					yOff++;
					offTE = this.worldObj.getTileEntity(this.xCoord,
							this.yCoord - yOff, this.zCoord);
					continue;
				}
			}
			break;
		}

		yOff = 0;
		offTE = this.worldObj.getTileEntity(this.xCoord, this.yCoord + yOff,
				this.zCoord);
		while (true) {
			if (offTE != null && offTE instanceof TileEntityCertusTank) {
				mainTank = (TileEntityCertusTank) offTE;
				if (mainTank.getFluid() == null
						|| mainTank.getFluid() == getFluid()) {
					FluidTankInfo info = mainTank.getTankInfo(false)[0];
					if (info != null) {
						capacity += info.capacity;
						if (info.fluid != null) {
							amount += info.fluid.amount;
							if (info.fluid.getFluid() != null)
								fluid = info.fluid.getFluid();
						}
					}
					yOff++;
					offTE = this.worldObj.getTileEntity(this.xCoord,
							this.yCoord + yOff, this.zCoord);
					continue;
				}
			}
			break;
		}

		return new FluidTankInfo[] { new FluidTankInfo(
				fluid != null ? new FluidStack(fluid, amount) : null, capacity) };
	}*/

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[]{this.tank.getInfo()};
    }

    public FluidTank getTank() {
        return this.tank;
    }

    @Override
    public void onDataPacket(NetworkManager net,
                             S35PacketUpdateTileEntity packet) {
        this.worldObj.markBlockRangeForRenderUpdate(this.xCoord, this.yCoord,
                this.zCoord, this.xCoord, this.yCoord, this.zCoord);
        readFromNBT(packet.func_148857_g());
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.tank.readFromNBT(tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        this.tank.writeToNBT(tag);
    }
}