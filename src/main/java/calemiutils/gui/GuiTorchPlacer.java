package calemiutils.gui;

import calemiutils.CalemiUtils;
import calemiutils.gui.base.GuiButtonRect;
import calemiutils.gui.base.GuiContainerBase;
import calemiutils.packet.ServerPacketHandler;
import calemiutils.tileentity.TileEntityTorchPlacer;
import calemiutils.util.helper.PacketHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiTorchPlacer extends GuiContainerBase {

    private GuiButtonRect enable;

    public GuiTorchPlacer(EntityPlayer player, TileEntityTorchPlacer te) {

        super(te.getTileContainer(player), player, te);
    }

    @Override
    public int getGuiSizeY() {

        return 192;
    }

    @Override
    public String getGuiTextureName() {

        return "torch_placer";
    }

    @Override
    public String getGuiTitle() {

        return "Torch Placer";
    }

    private String getEnabledText() {

        return te.enable ? "Enabled" : "Disabled";
    }

    @Override
    public void initGui() {

        super.initGui();

        int btnWidth = 62;

        enable = new GuiButtonRect(0, getScreenX() + (getGuiSizeX() / 2) - (btnWidth / 2), getScreenY() + 24, btnWidth, getEnabledText(), buttonList);
    }

    @Override
    public void updateScreen() {

        super.updateScreen();

        enable.displayString = getEnabledText();
    }

    @Override
    protected void actionPerformed(GuiButton button) {

        if (button.id == enable.id) {

            boolean value = !te.enable;

            CalemiUtils.network.sendToServer(new ServerPacketHandler("te-enable%" + PacketHelper.sendLocation(te.getLocation()) + value));
            te.enable = value;
        }
    }

    @Override
    public void drawGuiBackground(int mouseX, int mouseY) {

    }

    @Override
    public void drawGuiForeground(int mouseX, int mouseY) {

    }
}
