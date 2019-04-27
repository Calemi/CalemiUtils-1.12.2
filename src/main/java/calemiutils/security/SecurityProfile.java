package calemiutils.security;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class SecurityProfile {

    private String ownerName = "";

    public String getOwnerName() {

        return ownerName;
    }

    public void setOwner(EntityPlayer player) {

        ownerName = player.getName();
    }

    public boolean hasOwner() {

        return ownerName.isEmpty();
    }

    public boolean isOwner(String ownerName) {

        return this.ownerName.equalsIgnoreCase(ownerName);
    }

    public void readFromNBT(NBTTagCompound nbt) {

        if (nbt.hasKey("ownerName")) {
            ownerName = nbt.getString("ownerName");
        }
    }

    public void writeToNBT(NBTTagCompound nbt) {

        if (!ownerName.isEmpty()) {
            nbt.setString("ownerName", ownerName);
        }
    }
}
