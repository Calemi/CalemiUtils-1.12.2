package calemiutils.gui;

import calemiutils.gui.base.GuiContainerBase;
import calemiutils.tileentity.TileEntityBuildingUnit;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiBuildingUnitInventory extends GuiContainerBase {

    public GuiBuildingUnitInventory(EntityPlayer player, TileEntityBuildingUnit te) {

        super(te.getTileContainer(player), player, te);
    }

    @Override
    public int getGuiSizeY() {

        return 158;
    }

    @Override
    public String getGuiTextureName() {

        return "building_unit";
    }

    @Override
    public String getGuiTitle() {

        return "Building Unit";
    }

    @Override
    protected void actionPerformed(GuiButton button) {

    }

    @Override
    public void drawGuiBackground(int mouseX, int mouseY) {

    }

    @Override
    public void drawGuiForeground(int mouseX, int mouseY) {

    }
}
