package calemiutils.block;

import calemiutils.CalemiUtils;
import calemiutils.block.base.BlockContainerBase;
import calemiutils.gui.GuiInteractionTerminal;
import calemiutils.tileentity.TileEntityInteractionTerminal;
import calemiutils.util.HardnessConstants;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockInteractionTerminal extends BlockContainerBase {

    public BlockInteractionTerminal() {

        super("interaction_terminal", MaterialSound.IRON, HardnessConstants.SECURED);
        setCreativeTab(CalemiUtils.TAB);
    }

    @Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {

        LoreHelper.addInformationLore(tooltip, "Used to activate all connected Interaction Interfaces in one gui.");
        LoreHelper.addControlsLore(tooltip, "Open Gui", LoreHelper.Type.USE, true);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        TileEntity te = worldIn.getTileEntity(pos);

        if (worldIn.isRemote) {

            if (te instanceof TileEntityInteractionTerminal) {

                TileEntityInteractionTerminal teTerminal = (TileEntityInteractionTerminal) te;

                teTerminal.huntForInterfaces();
                openGui(playerIn, teTerminal);

                return true;
            }
        }

        return true;
    }

    @SideOnly(Side.CLIENT)
    private void openGui(EntityPlayer playerIn, TileEntityInteractionTerminal te) {

        FMLClientHandler.instance().displayGuiScreen(playerIn, new GuiInteractionTerminal(playerIn, te));
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {

        return new TileEntityInteractionTerminal();
    }

    public EnumBlockRenderType getRenderType(IBlockState state) {

        return EnumBlockRenderType.MODEL;
    }
}
