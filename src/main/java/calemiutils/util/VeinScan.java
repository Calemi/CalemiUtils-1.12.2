package calemiutils.util;

import calemiutils.config.CUConfig;
import calemiutils.tileentity.base.INetwork;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;

public class VeinScan {

    public final ArrayList<Location> buffer = new ArrayList<>();

    private final Location location;
    private Block block;
    private IBlockState state;

    public VeinScan(Location location, Block block) {

        this.location = location;
        this.block = block;
    }

    public VeinScan(Location location, IBlockState state) {

        this.location = location;
        this.state = state;
    }

    public VeinScan(Location location) {

        this.location = location;
    }

    public void startScan() {

        reset();
        scan(location, 0, false);
    }

    public void startScan(int customMaxSize, boolean useRadiusToBranch) {

        reset();
        scan(location, customMaxSize, useRadiusToBranch);
    }

    public void startNetworkScan(EnumFacing[] directions) {

        for (EnumFacing dir : directions) {
            scanNetwork(new Location(location, dir), dir);
        }
    }

    public void reset() {

        buffer.clear();
    }

    private void scan(Location location, int customMazSize, boolean useRadiusToBranch) {

        if (customMazSize == 0) {
            customMazSize =  CUConfig.blockScans.veinScanMaxSize;
        }

        if (buffer.size() >= customMazSize) {
            return;
        }

        if (!buffer.contains(location) && location.getBlock() != null) {

            if (state != null) {

                if (location.getBlockState() != state) {
                    return;
                }
            }

            if (block != null) {

                if (location.getBlock() != block) {
                    return;
                }
            }

            buffer.add(location);

            if (useRadiusToBranch) {

                for (int x = -1; x <= 1; x++) {

                    for (int y = -1; y <= 1; y++) {

                        for (int z = -1; z <= 1; z++) {

                            Location nextLocation = new Location(location.world, location.x + x, location.y + y, location.z + z);
                            scan(nextLocation, customMazSize, useRadiusToBranch);
                        }
                    }
                }
            }

            else {
                for (EnumFacing dir : EnumFacing.VALUES) {

                    scan(new Location(location, dir), customMazSize, useRadiusToBranch);
                }
            }
        }
    }

    private void scanNetwork(Location location, EnumFacing oldDir) {

        if (buffer.size() >= CUConfig.blockScans.veinScanMaxSize) {
            return;
        }

        TileEntity tileEntity = location.getTileEntity();

        if (tileEntity != null) {

            if (tileEntity instanceof INetwork) {

                INetwork network = (INetwork) tileEntity;

                for (EnumFacing dir : network.getConnectedDirections()) {

                    if (oldDir == dir.getOpposite()) {

                        if (!contains(location)) {

                            buffer.add(location);

                            for (EnumFacing searchDir : network.getConnectedDirections()) {
                                scanNetwork(new Location(location, searchDir), searchDir);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean contains(Location location) {

        for (Location nextLocation : buffer) {

            if (nextLocation.equals(location)) {
                return true;
            }
        }

        return false;
    }
}
