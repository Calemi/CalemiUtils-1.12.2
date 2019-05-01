package calemiutils.block;

import calemiutils.CalemiUtils;
import calemiutils.block.base.BlockInventoryContainerBase;
import calemiutils.config.CUConfig;
import calemiutils.gui.GuiBuildingUnit;
import calemiutils.tileentity.TileEntityBuildingUnit;
import calemiutils.util.MaterialSound;
import calemiutils.util.helper.LoreHelper;
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
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockBuildingUnit extends BlockInventoryContainerBase {

    public BlockBuildingUnit() {

        super("building_unit", MaterialSound.WOOD, 0, 0, 5);
        setCreativeTab(CalemiUtils.TAB);
        if (CUConfig.blockUtils.buildingUnit && CUConfig.blockUtils.blueprint) addBlock();
    }

    @Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {

        LoreHelper.addInformationLore(tooltip, "Builds custom structures out of Blueprint.");
        LoreHelper.addControlsLore(tooltip, "Open Gui", LoreHelper.Type.USE, true);
        LoreHelper.addControlsLore(tooltip, "Open Inventory", LoreHelper.Type.SNEAK_USE);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        TileEntity te = worldIn.getTileEntity(pos);

        if (playerIn.isSneaking()) {

            if (!worldIn.isRemote) {
                FMLNetworkHandler.openGui(playerIn, CalemiUtils.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
            }
        }

        else {

            if (te instanceof TileEntityBuildingUnit) {

                TileEntityBuildingUnit teBuildingUnit = (TileEntityBuildingUnit) te;

                if (worldIn.isRemote) openGui(playerIn, teBuildingUnit);
                return true;
            }
        }

        return true;
    }

    @SideOnly(Side.CLIENT)
    private void openGui(EntityPlayer playerIn, TileEntityBuildingUnit te) {

        FMLClientHandler.instance().displayGuiScreen(playerIn, new GuiBuildingUnit(playerIn, te));
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {

    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {

        return new TileEntityBuildingUnit();
    }

    public EnumBlockRenderType getRenderType(IBlockState state) {

        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {

        return false;
    }
}