package calemiutils.gui;

import calemiutils.CalemiUtils;
import calemiutils.gui.base.GuiButtonRect;
import calemiutils.gui.base.GuiContainerBase;
import calemiutils.packet.ServerPacketHandler;
import calemiutils.tileentity.base.TileEntityDiggingUnitBase;
import calemiutils.util.helper.GuiHelper;
import calemiutils.util.helper.PacketHelper;
import calemiutils.util.helper.StringHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiDiggingUnit extends GuiContainerBase {

    private final TileEntityDiggingUnitBase teDiggingUnit;

    private GuiButtonRect enable;

    private final String name;

    public GuiDiggingUnit(EntityPlayer player, TileEntityDiggingUnitBase te, String name) {

        super(te.getTileContainer(player), player, te);
        this.teDiggingUnit = te;
        this.name = name;
    }

    @Override
    public int getGuiSizeY() {

        return 225;
    }

    @Override
    public String getGuiTextureName() {

        return "digging_unit";
    }

    @Override
    public String getGuiTitle() {

        return name;
    }

    private String getEnabledText() {

        return te.enable ? "Enabled" : "Disabled";
    }

    @Override
    public void initGui() {

        super.initGui();

        int btnWidth = 62;

        enable = new GuiButtonRect(0, getScreenX() + (getGuiSizeX() / 2) - (btnWidth / 2), getScreenY() + 60, btnWidth, getEnabledText(), buttonList);
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

        GuiHelper.drawCenteredString("Range: " + teDiggingUnit.currentRange, getScreenX() + (getGuiSizeX() / 2) + 58, getScreenY() + 19, TEXT_COLOR);
        GuiHelper.drawCenteredString("Max: " + teDiggingUnit.getScaledRange(), getScreenX() + (getGuiSizeX() / 2) + 58, getScreenY() + 29, TEXT_COLOR);

        GuiHelper.drawCenteredString("To Mine:", getScreenX() + (getGuiSizeX() / 2) - 58, getScreenY() + 19, TEXT_COLOR);
        GuiHelper.drawCenteredString("" + teDiggingUnit.locationsToMine.size(), getScreenX() + (getGuiSizeX() / 2) - 58, getScreenY() + 29, TEXT_COLOR);

        if (teDiggingUnit.getCurrentLocationStack() != null) {
            GuiHelper.drawItemStack(itemRender, teDiggingUnit.getCurrentLocationStack(), getScreenX() + 80, getScreenY() + 19);
            GuiHelper.drawCenteredString(teDiggingUnit.getCurrentLocationStack().getDisplayName(), getScreenX() + (getGuiSizeX() / 2), getScreenY() + 39, TEXT_COLOR);
            GuiHelper.drawCenteredString("Cost: " + StringHelper.printCurrency(teDiggingUnit.getCurrentOreCost()), getScreenX() + (getGuiSizeX() / 2), getScreenY() + 49, TEXT_COLOR);
        }
    }

    @Override
    public void drawGuiForeground(int mouseX, int mouseY) {

    }
}
