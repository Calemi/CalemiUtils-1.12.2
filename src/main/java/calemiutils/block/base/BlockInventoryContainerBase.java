package calemiutils.block.base;

import calemiutils.CalemiUtils;
import calemiutils.util.HardnessConstants;
import calemiutils.util.Location;
import calemiutils.util.MaterialSound;
import calemiutils.util.helper.InventoryHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

public abstract class BlockInventoryContainerBase extends BlockContainerBase {

    protected BlockInventoryContainerBase(String name, MaterialSound matSound, float hardness, int harvestLevel, float resistance) {

        super(name, matSound, hardness, harvestLevel, resistance);
    }

    protected BlockInventoryContainerBase(String name, MaterialSound matSound, HardnessConstants constant) {

        super(name, matSound, constant);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {

        Location location = new Location(world, pos);

        if (location.getTileEntity() != null && location.getTileEntity() instanceof IInventory) {

            IInventory inv = (IInventory) location.getTileEntity();

            InventoryHelper.breakInventory(world, inv, location);

            world.updateComparatorOutputLevel(pos, this);
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if (!worldIn.isRemote) {

            FMLNetworkHandler.openGui(playerIn, CalemiUtils.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;
    }
}
