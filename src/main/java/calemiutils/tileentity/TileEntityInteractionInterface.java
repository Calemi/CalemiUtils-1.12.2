package calemiutils.tileentity;

import calemiutils.gui.GuiInteractionInterface;
import calemiutils.inventory.base.ContainerBase;
import calemiutils.item.ItemInteractionInterfaceFilter;
import calemiutils.security.ISecurity;
import calemiutils.security.SecurityProfile;
import calemiutils.tileentity.base.INetwork;
import calemiutils.tileentity.base.TileEntityInventoryBase;
import calemiutils.util.helper.NBTHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityInteractionInterface extends TileEntityInventoryBase implements INetwork, ISecurity {

    private final SecurityProfile profile = new SecurityProfile();

    public ItemStack filter = ItemStack.EMPTY;

    @Override
    public void update() {

    }

    public boolean canSetFilter(ItemStack stack) {

        return stack.isEmpty() || stack.getItem() instanceof ItemInteractionInterfaceFilter;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {

        super.readFromNBT(nbt);
        filter = NBTHelper.loadItem(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

        super.writeToNBT(nbt);
        NBTHelper.saveItem(nbt, filter);
        return nbt;
    }

    @Override
    public SecurityProfile getSecurityProfile() {

        return profile;
    }

    @Override
    public EnumFacing[] getConnectedDirections() {

        return new EnumFacing[]{EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST};
    }

    @Override
    public int getSizeInventory() {

        return 0;
    }

    @Override
    public Container getTileContainer(EntityPlayer player) {

        return new ContainerBase(player, this, 8, 41);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiContainer getTileGuiContainer(EntityPlayer player) {

        return new GuiInteractionInterface(player, this);
    }
}
