package extracells.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;


public abstract class BlockBase extends BlockContainer {

    protected BlockBase(Material material, float hardness, float resistance) {
        super(material);
        setHardness(hardness);
        setResistance(resistance);
    }

    protected BlockBase(Material material) {
        super(material);
    }

}
