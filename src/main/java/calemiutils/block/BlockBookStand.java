package calemiutils.block;

import calemiutils.CalemiUtils;
import calemiutils.block.base.BlockInventoryContainerBase;
import calemiutils.config.CUConfig;
import calemiutils.item.ItemLinkBookLocation;
import calemiutils.tileentity.TileEntityBookStand;
import calemiutils.util.IExtraInformation;
import calemiutils.util.Location;
import calemiutils.util.MaterialSound;
import calemiutils.util.helper.LoreHelper;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BlockBookStand extends BlockInventoryContainerBase implements IExtraInformation {

    private static final PropertyDirection FACING = BlockHorizontal.FACING;
    private static final PropertyBool BOOK = PropertyBool.create("book");

    private static final float pixel = 1F / 16F;
    private static final AxisAlignedBB AABB = new AxisAlignedBB(pixel * 3, pixel * 0, pixel * 3, pixel * 13, pixel * 12, pixel * 13);

    public BlockBookStand() {

        super("book_stand", MaterialSound.WOOD, 1, 0, 1);
        setCreativeTab(CalemiUtils.TAB);
        setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(BOOK, Boolean.FALSE));
        if (CUConfig.blockUtils.bookStand && CUConfig.itemUtils.locationLinkBook) addBlock();
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

        LoreHelper.addInformationLore(tooltip, "Holds Link Books.");
        LoreHelper.addControlsLore(tooltip, "Open Gui", LoreHelper.Type.USE, true);
        LoreHelper.addControlsLore(tooltip, "Open Inventory", LoreHelper.Type.SNEAK_USE);
        LoreHelper.addControlsLore(tooltip, "Place Book (Copies data from stored book if it exists)", LoreHelper.Type.USE_BOOK);
    }

    @Override
    public void getButtonInformation(List<String> list, World world, Location location, ItemStack stack) {

        ItemLinkBookLocation book = getBook(world, location.getBlockPos());
        ItemStack bookStack = getBookStack(world, location.getBlockPos());

        if (book != null && bookStack != null && book.isLinked(bookStack)) {
            list.add("Book: " + bookStack.getDisplayName());
            list.add("Book Location: " + ItemLinkBookLocation.getLinkedLocation(world, bookStack));
        }
    }

    @Override
    public ItemStack getButtonIcon(World world, Location location, ItemStack stack) {

        return stack;
    }

    private ItemStack getBookStack(World world, BlockPos pos) {

        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof TileEntityBookStand) {

            TileEntityBookStand bookStand = (TileEntityBookStand) tileEntity;

            return bookStand.getStackInSlot(0);
        }

        return null;
    }

    private ItemLinkBookLocation getBook(World world, BlockPos pos) {

        if (getBookStack(world, pos) != null) {

            ItemStack stack = getBookStack(world, pos);

            if (stack != null && !stack.isEmpty()) {

                if (stack.getItem() instanceof ItemLinkBookLocation) {

                    return (ItemLinkBookLocation) stack.getItem();
                }
            }
        }

        return null;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        ItemLinkBookLocation book = getBook(worldIn, pos);

        if (hand != null && !playerIn.getHeldItem(hand).isEmpty() && playerIn.getHeldItem(hand).getItem() instanceof ItemLinkBookLocation) {
            return false;
        }

        if (playerIn.isSneaking()) {

            if (!worldIn.isRemote) {

                FMLNetworkHandler.openGui(playerIn, CalemiUtils.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
                return true;
            }
        }

        else if (book != null) {

            if (worldIn.isRemote) {
                book.openGui(playerIn, getBookStack(worldIn, pos));
            }
            return true;
        }

        return false;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {

        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {

        setDefaultFacing(worldIn, pos, state);
    }

    private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state) {

        if (!worldIn.isRemote) {

            IBlockState blockState = worldIn.getBlockState(pos.north());
            IBlockState blockState1 = worldIn.getBlockState(pos.south());
            IBlockState blockState2 = worldIn.getBlockState(pos.west());
            IBlockState blockState3 = worldIn.getBlockState(pos.east());
            EnumFacing face = state.getValue(FACING);

            if (face == EnumFacing.NORTH && blockState.isFullBlock() && !blockState1.isFullBlock()) {
                face = EnumFacing.SOUTH;
            }
            else if (face == EnumFacing.SOUTH && blockState1.isFullBlock() && !blockState.isFullBlock()) {
                face = EnumFacing.NORTH;
            }
            else if (face == EnumFacing.WEST && blockState2.isFullBlock() && !blockState3.isFullBlock()) {
                face = EnumFacing.EAST;
            }
            else if (face == EnumFacing.EAST && blockState3.isFullBlock() && !blockState2.isFullBlock()) {
                face = EnumFacing.WEST;
            }

            worldIn.setBlockState(pos, state.withProperty(FACING, face), 2);
        }
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {

        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    public IBlockState getStateFromMeta(int meta) {

        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    public int getMetaFromState(IBlockState state) {

        return (state.getValue(FACING)).getIndex();
    }

    public IBlockState withRotation(IBlockState state, Rotation rot) {

        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {

        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @Override
    protected BlockStateContainer createBlockState() {

        return new BlockStateContainer(this, FACING, BOOK);
    }

    public EnumBlockRenderType getRenderType(IBlockState state) {

        return EnumBlockRenderType.MODEL;
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

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {

        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {

        return new TileEntityBookStand();
    }
}
