package calemiutils.tileentity.base;

import calemiutils.security.ISecurity;
import calemiutils.util.Location;
import calemiutils.util.UnitChatMessage;
import net.minecraft.entity.player.EntityPlayer;
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

    protected UnitChatMessage getUnitName(EntityPlayer player) {
        return new UnitChatMessage(getLocation().getBlock().getLocalizedName(), player);
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

        if (this instanceof ISecurity) {

            ISecurity security = (ISecurity) this;

            security.getSecurityProfile().readFromNBT(nbt);
        }

        if (this instanceof ICurrencyNetwork) {

            ICurrencyNetwork currency = (ICurrencyNetwork) this;

            currency.setCurrency(nbt.getInteger("currency"));
        }

        enable = nbt.getBoolean("enable");
        super.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

        if (this instanceof ISecurity) {

            ISecurity security = (ISecurity) this;

            security.getSecurityProfile().writeToNBT(nbt);
        }

        if (this instanceof ICurrencyNetwork) {

            ICurrencyNetwork currency = (ICurrencyNetwork) this;

            nbt.setInteger("currency", currency.getStoredCurrency());
        }

        nbt.setBoolean("enable", enable);
        return super.writeToNBT(nbt);
    }
}
