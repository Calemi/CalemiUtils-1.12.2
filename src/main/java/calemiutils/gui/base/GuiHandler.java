package calemiutils.gui.base;

import calemiutils.CalemiUtils;
import calemiutils.gui.GuiBuilderKit;
import calemiutils.gui.GuiInteractionInterfaceFilter;
import calemiutils.gui.GuiWallet;
import calemiutils.inventory.ContainerBuildersKit;
import calemiutils.inventory.ContainerInteractionInterfaceFilter;
import calemiutils.inventory.ContainerWallet;
import calemiutils.tileentity.base.ITileEntityGuiHandler;
import calemiutils.util.Location;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        Location loc = new Location(world, x, y, z);

        TileEntity tileentity = loc.getTileEntity();

        if (ID == CalemiUtils.guiIdWallet) {
            return new ContainerWallet(player);
        }

        if (ID == CalemiUtils.guiIdBuildersKit) {
            return new ContainerBuildersKit(player);
        }

        if (ID == CalemiUtils.guiIdInteractionInterfaceFilter) {
            return new ContainerInteractionInterfaceFilter(player);
        }

        if (tileentity instanceof ITileEntityGuiHandler) {
            return ((ITileEntityGuiHandler) tileentity).getTileContainer(player);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        Location loc = new Location(world, x, y, z);

        TileEntity tileentity = loc.getTileEntity();

        if (ID == CalemiUtils.guiIdWallet) {
            return new GuiWallet(player);
        }

        if (ID == CalemiUtils.guiIdBuildersKit) {
            return new GuiBuilderKit(player);
        }

        if (ID == CalemiUtils.guiIdInteractionInterfaceFilter) {
            return new GuiInteractionInterfaceFilter(player);
        }

        if (tileentity instanceof ITileEntityGuiHandler) {
            return ((ITileEntityGuiHandler) tileentity).getTileGuiContainer(player);
        }

        return null;
    }
}
