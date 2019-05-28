package calemiutils.block;

import calemiutils.block.base.BlockColoredBase;
import calemiutils.config.CUConfig;
import calemiutils.init.InitBlocks;
import calemiutils.item.ItemBuildersKit;
import calemiutils.util.Location;
import calemiutils.util.MaterialSound;
import calemiutils.util.UnitChatMessage;
import calemiutils.util.VeinScan;
import calemiutils.util.helper.InventoryHelper;
import calemiutils.util.helper.ItemHelper;
import calemiutils.util.helper.SoundHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockBlueprint extends BlockColoredBase {

    public BlockBlueprint() {

        super("blueprint", MaterialSound.STONE, 0.1F, 0, 0);
        setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.BLUE));
    }

    public IBlockState getStateByPrefix(String prefix) {

        EnumDyeColor dye = EnumDyeColor.BLUE;

        for (EnumDyeColor dyes : EnumDyeColor.values()) {
            if (dyes.getName().startsWith(prefix)) {
                dye = dyes;
            }
        }

        return getStateFromMeta(dye.getMetadata());
    }

    @Override
    public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {

        Location location = new Location(world, pos);
        ItemStack currentStack = player.getHeldItemMainhand();
        UnitChatMessage message = new UnitChatMessage("Blueprint", player);

        VeinScan scan = new VeinScan(location, location.getBlockState());
        scan.startScan();

        if (!currentStack.isEmpty() && currentStack.getItem() != Item.getItemFromBlock(this)) {

            if (currentStack.getItem() instanceof ItemBlock) {
                replaceAllBlocks(world, player, location, currentStack, scan, message);
            }

            else if (currentStack.getItem() instanceof ItemBuildersKit) {
                replaceAllBlocks(world, player, location, ((ItemBuildersKit) currentStack.getItem()).getBlockType(currentStack), scan, message);
            }
        }

        else if (!world.isRemote && player.isSneaking() && currentStack.isEmpty()) {

            if (scan.buffer.size() >= CUConfig.blockScans.veinScanMaxSize) {
                message.printMessage(TextFormatting.GREEN, "There are " + CUConfig.blockScans.veinScanMaxSize + "+ connected Blueprints");
            }

            else message.printMessage(TextFormatting.GREEN, "There are " + ItemHelper.countByStacks(scan.buffer.size()) + " connected Blueprints");
        }
    }

    private void replaceAllBlocks(World world, EntityPlayer player, Location location, ItemStack currentStack, VeinScan scan, UnitChatMessage message) {

        IBlockState state = Block.getBlockFromItem(currentStack.getItem()).getStateForPlacement(player.world, location.getBlockPos(), EnumFacing.UP, 0, 0, 0, currentStack.getItemDamage(), player, EnumHand.MAIN_HAND);

        if (player.isSneaking()) {
            replaceBlock(location, player, state);
            InventoryHelper.consumeItem(player.inventory, 1, true, currentStack);
            SoundHelper.playBlockPlaceSound(world, player, Block.getBlockFromItem(currentStack.getItem()).getDefaultState(), location);
        }

        else {

            int itemCount = InventoryHelper.countItems(player.inventory, true, currentStack);

            if (itemCount >= scan.buffer.size()) {

                int amountToConsume = 0;

                for (Location nextLocation : scan.buffer) {

                    amountToConsume++;
                    replaceBlock(nextLocation, player, state);
                }

                if (amountToConsume > 0) {

                    SoundHelper.playDing(player.world, player);
                    SoundHelper.playBlockPlaceSound(world, player, Block.getBlockFromItem(currentStack.getItem()).getDefaultState(), location);

                    if (!world.isRemote) message.printMessage(TextFormatting.GREEN, "Placed " + ItemHelper.countByStacks(amountToConsume));
                    InventoryHelper.consumeItem(player.inventory, amountToConsume, true, currentStack);
                }
            }

            else if (!world.isRemote) {

                message.printMessage(TextFormatting.RED, "You don't have enough blocks of that type!");
                message.printMessage(TextFormatting.RED, "You're missing: " + ItemHelper.countByStacks((scan.buffer.size() - itemCount)));
            }
        }
    }

    private void replaceBlock(Location location, EntityPlayer player, IBlockState state) {

        if (!player.world.isRemote) {

            location.setBlock(state, player);

            ForgeEventFactory.onPlayerBlockPlace(player, BlockSnapshot.getBlockSnapshot(player.world, location.getBlockPos()), EnumFacing.UP, EnumHand.MAIN_HAND);
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {

        if (state.getBlock() instanceof BlockBlueprint) {
            return (state.getValue(COLOR)).getMetadata();
        }

        return 0;
    }

    @Override
    public int quantityDropped(Random rand) {

        return 0;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {

        return BlockRenderLayer.CUTOUT;
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {

        return false;
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {

        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {

        IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
        Block block = iblockstate.getBlock();

        if (this == InitBlocks.BLUEPRINT) {

            if (blockState != iblockstate) {
                return true;
            }

            if (block == this) {
                return false;
            }
        }

        return block != this && super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }
}
