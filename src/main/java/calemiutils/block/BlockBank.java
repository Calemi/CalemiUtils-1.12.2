package calemiutils.block;

import calemiutils.CalemiUtils;
import calemiutils.block.base.BlockInventoryContainerBase;
import calemiutils.tileentity.TileEntityBank;
import calemiutils.util.HardnessConstants;
import calemiutils.util.IExtraInformation;
import calemiutils.util.Location;
import calemiutils.util.MaterialSound;
import calemiutils.util.helper.LoreHelper;
import calemiutils.util.helper.SecurityHelper;
import calemiutils.util.helper.StringHelper;
import com.mojang.realmsclient.gui.ChatFormatting;
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

public class BlockBank extends BlockInventoryContainerBase implements IExtraInformation {

    public BlockBank() {

        super("bank", MaterialSound.IRON, HardnessConstants.SECURED);
        setCreativeTab(CalemiUtils.TAB);
        setBlockUnbreakable();
    }

    @Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {

        LoreHelper.addInformationLore(tooltip, "Collects RC from all connected Trading Posts.");
        LoreHelper.addControlsLore(tooltip, "Open Inventory", LoreHelper.Type.USE, true);
    }

    @Override
    public void getButtonInformation(List<String> list, World world, Location location, ItemStack stack) {

        TileEntity te = location.getTileEntity();

        if (te instanceof TileEntityBank) {

            TileEntityBank bank = (TileEntityBank) te;

            list.add("Currency: " + ChatFormatting.GOLD + StringHelper.printCurrency(bank.getStoredCurrency()));
        }
    }

    @Override
    public ItemStack getButtonIcon(World world, Location location, ItemStack stack) {

        return stack;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        Location location = new Location(worldIn, pos);

        if (SecurityHelper.openSecuredBlock(location, playerIn, true)) {
            return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
        }

        else return false;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {

        return new TileEntityBank();
    }

    public EnumBlockRenderType getRenderType(IBlockState state) {

        return EnumBlockRenderType.MODEL;
    }
}
