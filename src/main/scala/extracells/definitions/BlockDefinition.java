package extracells.definitions;

import appeng.api.definitions.ITileDefinition;
import extracells.api.definitions.IBlockDefinition;
import extracells.registries.BlockEnum;
import extracells.tileentity.*;

public class BlockDefinition implements IBlockDefinition {

    public static final BlockDefinition instance = new BlockDefinition();

    @Override
    public ITileDefinition blockInterface() {
        return new BlockItemDefinitions(BlockEnum.ECBASEBLOCK.getBlock(),
                TileEntityFluidInterface.class);
    }

    @Override
    public ITileDefinition certusTank() {
        return new BlockItemDefinitions(BlockEnum.CERTUSTANK.getBlock(),
                TileEntityCertusTank.class);
    }

	/* TODO: implement!
	@Override
	public ITileDefinition fluidFiller() {
		return new BlockItemDefinitions(BlockEnum.FLUIDCRAFTER.getBlock(), 1,
				TileEntityFluidFiller.class);
	}*/

    @Override
    public ITileDefinition walrus() {
        return new BlockItemDefinitions(BlockEnum.WALRUS.getBlock(),
                TileEntityWalrus.class);
    }

}
