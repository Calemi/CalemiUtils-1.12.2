package calemiutils.tileentity;

import calemiutils.block.BlockNetworkGate;
import calemiutils.security.ISecurity;
import calemiutils.tileentity.base.INetwork;
import net.minecraft.util.EnumFacing;

public class TileEntityNetworkGate extends TileEntityNetworkCable implements INetwork, ISecurity {

    private boolean hasChanged = false;

    @Override
    public void update() {

        if (world.isBlockPowered(getPos())) {

            if (!hasChanged) {
                setState(false);
            }

            hasChanged = true;
        }

        else {

            if (hasChanged) {
                setState(true);
            }

            hasChanged = false;
        }
    }

    private void setState(boolean value) {

        BlockNetworkGate.setState(value, world, pos);
    }

    @Override
    public EnumFacing[] getConnectedDirections() {

        if (getLocation() != null && getLocation().getBlock() instanceof BlockNetworkGate) {

            BlockNetworkGate gate = (BlockNetworkGate) getLocation().getBlock();

            if (gate.isConnected) {
                return EnumFacing.VALUES;
            }
        }

        return new EnumFacing[]{};
    }
}
