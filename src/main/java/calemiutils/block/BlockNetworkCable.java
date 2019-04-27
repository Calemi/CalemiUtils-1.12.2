package calemiutils.block;

import calemiutils.tileentity.base.INetwork;
import calemiutils.util.helper.AABBHelper;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BlockNetworkCable extends BlockNetworkCableOpaque {

    private static final PropertyBool UP = PropertyBool.create("up");
    private static final PropertyBool DOWN = PropertyBool.create("down");
    private static final PropertyBool NORTH = PropertyBool.create("north");
    private static final PropertyBool EAST = PropertyBool.create("east");
    private static final PropertyBool SOUTH = PropertyBool.create("south");
    private static final PropertyBool WEST = PropertyBool.create("west");
    private static final PropertyBool DOWNUP = PropertyBool.create("downup");
    private static final PropertyBool NORTHSOUTH = PropertyBool.create("northsouth");
    private static final PropertyBool EASTWEST = PropertyBool.create("eastwest");
    private static final float pixel = 1F / 16F;
    private static final AxisAlignedBB CORE_AABB = new AxisAlignedBB(pixel * 5, pixel * 5, pixel * 5, pixel * 11, pixel * 11, pixel * 11);

    private static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(pixel * 5, pixel * 0, pixel * 5, pixel * 11, pixel * 11, pixel * 11);
    private static final AxisAlignedBB UP_AABB = new AxisAlignedBB(pixel * 5, pixel * 5, pixel * 5, pixel * 11, pixel * 16, pixel * 11);
    private static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(pixel * 5, pixel * 5, 0, pixel * 11, pixel * 11, pixel * 5);
    private static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(pixel * 5, pixel * 5, pixel * 5, pixel * 16, pixel * 11, pixel * 11);
    private static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(pixel * 5, pixel * 5, pixel * 5, pixel * 11, pixel * 11, pixel * 16);
    private static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(pixel * 0, pixel * 5, pixel * 5, pixel * 11, pixel * 11, pixel * 11);
    private static final AxisAlignedBB DOWNUP_AABB = new AxisAlignedBB(pixel * 5, pixel * 0, pixel * 5, pixel * 11, pixel * 16, pixel * 11);
    private static final AxisAlignedBB NORTHSOUTH_AABB = new AxisAlignedBB(pixel * 5, pixel * 5, pixel * 0, pixel * 11, pixel * 11, pixel * 16);
    private static final AxisAlignedBB EASTWEST_AABB = new AxisAlignedBB(pixel * 0, pixel * 5, pixel * 5, pixel * 16, pixel * 11, pixel * 11);

    public BlockNetworkCable() {

        super("network_cable", true);
        setDefaultState(this.blockState.getBaseState().withProperty(DOWN, Boolean.FALSE).withProperty(UP, Boolean.FALSE).withProperty(NORTH, Boolean.FALSE)
                .withProperty(EAST, Boolean.FALSE).withProperty(SOUTH, Boolean.FALSE).withProperty(WEST, Boolean.FALSE).withProperty(DOWNUP, Boolean.FALSE)
                .withProperty(NORTHSOUTH, Boolean.FALSE).withProperty(EASTWEST, Boolean.FALSE));
    }

    private static AxisAlignedBB getBoundingBoxIdx(IBlockState state) {

        AxisAlignedBB i = CORE_AABB;

        if (state.getValue(DOWN)) i = AABBHelper.addTwoBoundingBoxes(i, DOWN_AABB);
        if (state.getValue(UP)) i = AABBHelper.addTwoBoundingBoxes(i, UP_AABB);
        if (state.getValue(NORTH)) i = AABBHelper.addTwoBoundingBoxes(i, NORTH_AABB);
        if (state.getValue(EAST)) i = AABBHelper.addTwoBoundingBoxes(i, EAST_AABB);
        if (state.getValue(SOUTH)) i = AABBHelper.addTwoBoundingBoxes(i, SOUTH_AABB);
        if (state.getValue(WEST)) i = AABBHelper.addTwoBoundingBoxes(i, WEST_AABB);
        if (state.getValue(DOWNUP)) i = DOWNUP_AABB;
        if (state.getValue(NORTHSOUTH)) i = NORTHSOUTH_AABB;
        if (state.getValue(EASTWEST)) i = EASTWEST_AABB;

        return i;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {

        if (!isActualState) {
            state = state.getActualState(worldIn, pos);
        }

        addCollisionBoxToList(pos, entityBox, collidingBoxes, CORE_AABB);

        if (state.getValue(DOWN)) addCollisionBoxToList(pos, entityBox, collidingBoxes, DOWN_AABB);
        if (state.getValue(UP)) addCollisionBoxToList(pos, entityBox, collidingBoxes, UP_AABB);
        if (state.getValue(NORTH)) addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTH_AABB);
        if (state.getValue(EAST)) addCollisionBoxToList(pos, entityBox, collidingBoxes, EAST_AABB);
        if (state.getValue(SOUTH)) addCollisionBoxToList(pos, entityBox, collidingBoxes, SOUTH_AABB);
        if (state.getValue(WEST)) addCollisionBoxToList(pos, entityBox, collidingBoxes, WEST_AABB);

        if (state.getValue(DOWNUP)) addCollisionBoxToList(pos, entityBox, collidingBoxes, DOWNUP_AABB);
        if (state.getValue(NORTHSOUTH)) addCollisionBoxToList(pos, entityBox, collidingBoxes, NORTHSOUTH_AABB);
        if (state.getValue(EASTWEST)) addCollisionBoxToList(pos, entityBox, collidingBoxes, EASTWEST_AABB);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

        state = this.getActualState(state, source, pos);
        return getBoundingBoxIdx(state);
    }

    private boolean canConnectTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {

        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof INetwork) {

            INetwork network = (INetwork) tileEntity;

            for (EnumFacing dir : network.getConnectedDirections()) {

                if (facing == dir) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {

        return canConnectTo(world, pos.offset(facing), facing.getOpposite());
    }

    private boolean canCableConnectTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {

        BlockPos other = pos.offset(facing);
        Block block = world.getBlockState(other).getBlock();
        return block.canBeConnectedTo(world, other, facing.getOpposite()) || canConnectTo(world, other, facing.getOpposite());
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {

        boolean down = canCableConnectTo(worldIn, pos, EnumFacing.DOWN);
        boolean up = canCableConnectTo(worldIn, pos, EnumFacing.UP);
        boolean north = canCableConnectTo(worldIn, pos, EnumFacing.NORTH);
        boolean east = canCableConnectTo(worldIn, pos, EnumFacing.EAST);
        boolean south = canCableConnectTo(worldIn, pos, EnumFacing.SOUTH);
        boolean west = canCableConnectTo(worldIn, pos, EnumFacing.WEST);

        boolean downup = down && up && (!north && !east && !south && !west);
        boolean northsouth = north && south && (!down && !up && !east && !west);
        boolean eastwest = east && west && (!north && !south && !down && !up);

        if (downup || northsouth || eastwest) {
            down = false;
            up = false;
            north = false;
            east = false;
            south = false;
            west = false;
        }

        return state.withProperty(DOWN, down).withProperty(UP, up).withProperty(NORTH, north).withProperty(EAST, east).withProperty(SOUTH, south).withProperty(WEST, west).withProperty(DOWNUP, downup).withProperty(NORTHSOUTH, northsouth).withProperty(EASTWEST, eastwest);
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {

        switch (rot) {

            case CLOCKWISE_180:
                return state.withProperty(NORTH, state.getValue(SOUTH)).withProperty(EAST, state.getValue(WEST)).withProperty(SOUTH, state.getValue(NORTH)).withProperty(WEST, state.getValue(EAST));
            case COUNTERCLOCKWISE_90:
                return state.withProperty(NORTH, state.getValue(EAST)).withProperty(EAST, state.getValue(SOUTH)).withProperty(SOUTH, state.getValue(WEST)).withProperty(WEST, state.getValue(NORTH));
            case CLOCKWISE_90:
                return state.withProperty(NORTH, state.getValue(WEST)).withProperty(EAST, state.getValue(NORTH)).withProperty(SOUTH, state.getValue(EAST)).withProperty(WEST, state.getValue(SOUTH));
            default:
                return state;
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {

        return new BlockStateContainer(this, DOWN, UP, NORTH, EAST, WEST, SOUTH, DOWNUP, NORTHSOUTH, EASTWEST);
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
}
