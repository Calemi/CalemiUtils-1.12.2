package calemiutils.tileentity.base;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public interface ITileEntityGuiHandler {

    Container getTileContainer(EntityPlayer player);

    GuiContainer getTileGuiContainer(EntityPlayer player);
}
