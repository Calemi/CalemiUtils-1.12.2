package calemiutils.block;

import calemiutils.CalemiUtils;
import calemiutils.block.base.BlockContainerBase;
import calemiutils.tileentity.TileEntityNetworkCable;
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

public class BlockNetworkCableOpaque extends BlockContainerBase {

    BlockNetworkCableOpaque(String name, boolean hasTab) {

        super(name, MaterialSound.IRON, HardnessConstants.SECURED);
        if (hasTab) setCreativeTab(CalemiUtils.TAB);
    }

    public BlockNetworkCableOpaque() {

        this("network_cable_opaque", true);
    }

    @Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {

        LoreHelper.addInformationLore(tooltip, "Used to connect Trading Posts to Banks within a single network.");
    }

    @Override
    public int getMetaFromState(IBlockState state) {

        return 0;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {

        return new TileEntityNetworkCable();
    }

    public EnumBlockRenderType getRenderType(IBlockState state) {

        return EnumBlockRenderType.MODEL;
    }
}
