package calemiutils.tileentity.base;

import calemiutils.util.Location;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public abstract class TileEntityBase extends TileEntity implements ITickable {

    public boolean enable;

    protected TileEntityBase() {

        enable = true;
    }

    public Location getLocation() {

        return new Location(world, pos);
    }

    public void markForUpdate() {

        markDirty();
        world.markBlockRangeForRenderUpdate(getPos(), getPos().add(64, 64, 64));
        world.addBlockEvent(getPos(), this.getBlockType(), 1, 1);
        world.notifyBlockUpdate(getPos(), getLocation().getBlockState(), getLocation().getBlockState(), 0);
        world.notifyNeighborsOfStateChange(getPos(), getBlockType(), false);
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {

        int meta = getBlockMetadata();
        return new SPacketUpdateTileEntity(getPos(), meta, getTileData());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {

        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag() {

        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return nbt;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {

        readFromNBT(tag);
    }

    @Override
    public NBTTagCompound getTileData() {

        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {

        enable = nbt.getBoolean("enable");
        super.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

        nbt.setBoolean("enable", enable);
        return super.writeToNBT(nbt);
    }
}
