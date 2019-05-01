package calemiutils.block.base;

import calemiutils.CalemiUtils;
import calemiutils.init.InitBlocks;
import calemiutils.init.InitItems;
import calemiutils.registry.IHasModel;
import calemiutils.util.HardnessConstants;
import calemiutils.util.MaterialSound;
import net.minecraft.block.BlockContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public abstract class BlockContainerBase extends BlockContainer implements IHasModel {

    BlockContainerBase(String name, MaterialSound matSound, float hardness, int harvestLevel, float resistance) {

        super(matSound.mat);
        setUnlocalizedName(name);
        setRegistryName(name);

        setSoundType(matSound.sound);
        setHardness(hardness);
        setHarvestLevel("pickaxe", harvestLevel);
        setResistance(resistance);
    }

    public BlockContainerBase addBlock() {

        if (getRegistryName() != null) {
            InitBlocks.BLOCKS.add(this);
            InitItems.ITEMS.add(new ItemBlock(this).setRegistryName(getRegistryName()));
        }

        return this;
    }

    protected BlockContainerBase(String name, MaterialSound matSound, HardnessConstants constant) {

        this(name, matSound, constant.hardness, constant.harvestLevel, constant.resistance);

        if (!constant.breakable) {
            setBlockUnbreakable();
        }
    }
    @Override
    public void registerModels() {

        CalemiUtils.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}
