package calemiutils.tileentity;

import calemiutils.block.BlockBookStand;
import calemiutils.gui.GuiOneSlot;
import calemiutils.init.InitItems;
import calemiutils.inventory.ContainerOneSlot;
import calemiutils.item.ItemLinkBookLocation;
import calemiutils.tileentity.base.INetwork;
import calemiutils.tileentity.base.ITileEntityGuiHandler;
import calemiutils.tileentity.base.TileEntityInventoryBase;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityBookStand extends TileEntityInventoryBase implements ITileEntityGuiHandler, INetwork {

    private boolean hasChanged = false;

    public TileEntityBookStand() {

        setInputSlots(0);
        setSideInputSlots(0);
        setExtractSlots(0);
    }

    @Override
    public void update() {

        ItemStack stack = getStackInSlot(0);

        if (!stack.isEmpty() && stack.getItem() instanceof ItemLinkBookLocation) {

            if (!hasChanged) {
                setState(true);
                hasChanged = true;
            }

        }

        else {
            setState(false);
            hasChanged = false;
        }
    }

    private void setState(boolean value) {

        BlockBookStand.setState(value, world, pos, getBlockMetadata());
    }

    @Override
    public Container getTileContainer(EntityPlayer player) {

        return new ContainerOneSlot(player, this, InitItems.LINK_BOOK_LOCATION);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiContainer getTileGuiContainer(EntityPlayer player) {

        return new GuiOneSlot(player, this, "Book Stand", InitItems.LINK_BOOK_LOCATION);
    }

    @Override
    public int getSizeInventory() {

        return 1;
    }

    @Override
    public EnumFacing[] getConnectedDirections() {

        return new EnumFacing[]{EnumFacing.DOWN};
    }
}
