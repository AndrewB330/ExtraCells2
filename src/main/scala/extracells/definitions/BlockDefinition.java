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

}
