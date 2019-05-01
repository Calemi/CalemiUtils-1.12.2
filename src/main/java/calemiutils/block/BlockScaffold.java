package calemiutils.block;

import calemiutils.CalemiUtils;
import calemiutils.block.base.BlockBase;
import calemiutils.config.CUConfig;
import calemiutils.init.InitBlocks;
import calemiutils.util.Location;
import calemiutils.util.MaterialSound;
import calemiutils.util.VeinScan;
import calemiutils.util.helper.BlockHelper;
import calemiutils.util.helper.EntityHelper;
import calemiutils.util.helper.ItemHelper;
import calemiutils.util.helper.LoreHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockScaffold extends BlockBase {

    public BlockScaffold() {

        super("iron_scaffold", MaterialSound.IRON, 1, 1, 1);
        setCreativeTab(CalemiUtils.TAB);
        if (CUConfig.blockUtils.ironScaffold) addBlock();
    }

    @Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {

        LoreHelper.addInformationLore(tooltip, "Temporary block used for getting to the hard to reach places!");
        LoreHelper.addControlsLore(tooltip, "Teleport to the top", LoreHelper.Type.USE_OPEN_HAND, true);
        LoreHelper.addControlsLore(tooltip, "Break all connected scaffolds", LoreHelper.Type.SNEAK_BREAK_BLOCK);
        LoreHelper.addControlsLore(tooltip, "Place blocks in a line", LoreHelper.Type.LEFT_CLICK_BLOCK);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        ItemStack stack = player.getHeldItemMainhand();

        if (stack.isEmpty()) {

            Location location = new Location(world, pos);

            if (CUConfig.misc.scaffoldMaxHeightTp == 0) {
                return false;
            }

            if (facing != EnumFacing.UP) {

                for (int i = 0; i < CUConfig.misc.scaffoldMaxHeightTp; i++) {

                    Location nextLocation = new Location(location, EnumFacing.UP, i);
                    Location nextLocationUp = new Location(location, EnumFacing.UP, i + 1);

                    if (!nextLocation.isAirBlock() && !(nextLocation.getBlock() instanceof BlockScaffold)) {
                        return false;
                    }

                    if (nextLocation.isAirBlock() && nextLocationUp.isAirBlock()) {

                        if (!world.isRemote) {

                            if (EntityHelper.canTeleportAt((EntityPlayerMP) player, nextLocation)) {
                                EntityHelper.teleportPlayer((EntityPlayerMP) player, nextLocation, ((EntityPlayerMP) player).dimension);
                                return true;
                            }
                        }
                    }
                }
            }

            else {

                for (int i = 0; i < CUConfig.misc.scaffoldMaxHeightTp; i++) {

                    Location nextLocation = new Location(location, EnumFacing.DOWN, i);
                    Location nextLocationDown = new Location(location, EnumFacing.DOWN, i + 1);

                    if (!(nextLocationDown.getBlock() instanceof BlockScaffold)) {

                        for (EnumFacing dir : EnumFacing.VALUES) {

                            if (!world.isRemote) {

                                if (EntityHelper.canTeleportAt((EntityPlayerMP) player, new Location(nextLocation, dir))) {
                                    EntityHelper.teleportPlayer((EntityPlayerMP) player, new Location(nextLocation, dir), ((EntityPlayerMP) player).dimension);
                                    return true;
                                }
                            }
                        }

                        break;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {

        BlockHelper.placeBlockInArray(world, pos, player, this);
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {

        if (player.isSneaking()) {

            Location location = new Location(world, pos);

            VeinScan scan = new VeinScan(location, this);

            scan.startScan();

            for (Location nextLocation : scan.buffer) {

                nextLocation.setBlockToAir();

                if (!player.capabilities.isCreativeMode) {
                    if (!world.isRemote) ItemHelper.spawnItem(world, new Location(player), new ItemStack(InitBlocks.IRON_SCAFFOLD));
                }
            }
        }

        else super.onBlockHarvested(world, pos, state, player);
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {

        return BlockRenderLayer.CUTOUT;
    }

    public boolean isFullCube(IBlockState state) {

        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {

        return false;
    }
}
