package calemiutils.tileentity;

import calemiutils.security.ISecurity;
import calemiutils.security.SecurityProfile;
import calemiutils.tileentity.base.INetwork;
import calemiutils.tileentity.base.TileEntityBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class TileEntityNetworkCable extends TileEntityBase implements INetwork, ISecurity {

    private final SecurityProfile profile = new SecurityProfile();

    @Override
    public void update() {

    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {

        super.readFromNBT(nbt);

        profile.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

        super.writeToNBT(nbt);

        profile.writeToNBT(nbt);
        return nbt;
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
