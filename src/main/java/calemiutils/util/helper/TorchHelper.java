package calemiutils.util.helper;

import calemiutils.util.Location;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.EnumSkyBlock;

public class TorchHelper {

    public static boolean canPlaceTorchAt(Location location) {

        if (location.world.getLightFor(EnumSkyBlock.BLOCK, location.getBlockPos()) > 7) {
            return false;
        }

        if (!location.isAirBlock()) {

            if (!location.getBlock().getMaterial(location.getBlockState()).isReplaceable()) {
                return false;
            }
        }

        if (location.getBlock() instanceof BlockLiquid) {
            return false;
        }

        Location nextLocationDown = new Location(location, EnumFacing.DOWN);

        if (!nextLocationDown.getBlock().isFullCube(nextLocationDown.getBlockState())) {
            return false;
        }

        return nextLocationDown.getBlock().isOpaqueCube(nextLocationDown.getBlockState());

    }
}
