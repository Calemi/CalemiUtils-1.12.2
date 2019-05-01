package calemiutils.block;

import calemiutils.CalemiUtils;
import calemiutils.block.base.BlockInventoryContainerBase;
import calemiutils.config.CUConfig;
import calemiutils.tileentity.TileEntityMiningUnit;
import calemiutils.util.HardnessConstants;
import calemiutils.util.MaterialSound;
import calemiutils.util.helper.LoreHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;

import java.util.List;

public class BlockMiningUnit extends BlockInventoryContainerBase {

    public BlockMiningUnit() {

        super("mining_unit", MaterialSound.WOOD, HardnessConstants.UNIT);
        setCreativeTab(CalemiUtils.TAB);
        if (CUConfig.blockUtils.miningUnit && CUConfig.itemUtils.economy) addBlock();
    }

    @Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {

        LoreHelper.addInformationLore(tooltip, "Collects ores from the world within a radius.");
        LoreHelper.addControlsLore(tooltip, "Open Inventory", LoreHelper.Type.USE, true);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {

        return new TileEntityMiningUnit();
    }

    public EnumBlockRenderType getRenderType(IBlockState state) {

        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {

        return false;
    }
}