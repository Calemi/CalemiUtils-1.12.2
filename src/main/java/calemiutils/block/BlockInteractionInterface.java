package calemiutils.block;

import calemiutils.CalemiUtils;
import calemiutils.block.base.BlockInventoryContainerBase;
import calemiutils.tileentity.TileEntityInteractionInterface;
import calemiutils.util.HardnessConstants;
import calemiutils.util.Location;
import calemiutils.util.MaterialSound;
import calemiutils.util.helper.LoreHelper;
import calemiutils.util.helper.SecurityHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class BlockInteractionInterface extends BlockInventoryContainerBase {

    public BlockInteractionInterface() {

        super("interaction_interface", MaterialSound.IRON, HardnessConstants.SECURED);
        setCreativeTab(CalemiUtils.TAB);
    }

    @Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {

        LoreHelper.addInformationLore(tooltip, "Place a block on top of it and connect it to an Interaction Terminal to function.");
        LoreHelper.addControlsLore(tooltip, "Open Inventory", LoreHelper.Type.USE, true);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        Location location = new Location(worldIn, pos);

        if (SecurityHelper.openSecuredBlock(location, playerIn, true)) {

            return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
        }

        else return true;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {

        return new TileEntityInteractionInterface();
    }

    public EnumBlockRenderType getRenderType(IBlockState state) {

        return EnumBlockRenderType.MODEL;
    }
}
