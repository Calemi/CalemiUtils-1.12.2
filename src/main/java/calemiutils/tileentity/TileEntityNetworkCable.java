package calemiutils.tileentity;

import calemiutils.security.ISecurity;
import calemiutils.security.SecurityProfile;
import calemiutils.tileentity.base.INetwork;
import calemiutils.tileentity.base.TileEntityBase;
import net.minecraft.util.EnumFacing;

public class TileEntityNetworkCable extends TileEntityBase implements INetwork, ISecurity {

    private final SecurityProfile profile = new SecurityProfile();

    @Override
    public void update() {

    }

    @Override
    public SecurityProfile getSecurityProfile() {

        return profile;
    }

    @Override
    public EnumFacing[] getConnectedDirections() {

        return EnumFacing.VALUES;
    }
}
