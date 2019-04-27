package calemiutils.gui;

import calemiutils.gui.base.GuiContainerBase;
import calemiutils.tileentity.base.TileEntityInventoryBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiOneSlot extends GuiContainerBase {

    private final String name;

    public GuiOneSlot(EntityPlayer player, TileEntityInventoryBase te, String name, Item... filters) {

        super(te.getTileContainer(player), player);
        this.name = name;
    }

    @Override
    public String getGuiTextureName() {

        return "one_slot";
    }

    @Override
    public String getGuiTitle() {

        return name;
    }

    @Override
    public int getGuiSizeY() {

        return 123;
    }

    @Override
    public void drawGuiBackground(int mouseX, int mouseY) {

    }

    @Override
    public void drawGuiForeground(int mouseX, int mouseY) {

    }
}
