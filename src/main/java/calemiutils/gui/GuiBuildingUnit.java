package calemiutils.gui;

import calemiutils.CUReference;
import calemiutils.CalemiUtils;
import calemiutils.blueprint.BlueprintBuild;
import calemiutils.gui.base.GuiButtonRect;
import calemiutils.gui.base.GuiRect;
import calemiutils.gui.base.GuiScreenBase;
import calemiutils.gui.base.GuiTextFieldRect;
import calemiutils.packet.BuildingUnitPacket;
import calemiutils.tileentity.TileEntityBuildingUnit;
import calemiutils.util.helper.GuiHelper;
import calemiutils.util.helper.PacketHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiBuildingUnit extends GuiScreenBase {

    private final TileEntityBuildingUnit teBuildingUnit;

    private GuiButtonRect upBtn, downBtn;
    private GuiButtonRect buildBtn, readBlueprintsBtn, readBlocksBtn, rotateBtn;

    private GuiTextFieldRect nameField;

    public GuiBuildingUnit(EntityPlayer player, TileEntityBuildingUnit te) {

        super(player);
        this.teBuildingUnit = te;
    }

    @Override
    public void initGui() {

        Keyboard.enableRepeatEvents(true);

        upBtn = new GuiButtonRect(0, getScreenX() - 85, getScreenY() - 66, 16, "/\\", buttonList);
        downBtn = new GuiButtonRect(1, getScreenX() - 85, getScreenY() + 50, 16, "\\/", buttonList);

        rotateBtn = new GuiButtonRect(2, getScreenX() - 24, getScreenY() - 20, 48, "Rotate", buttonList);
        buildBtn = new GuiButtonRect(3, getScreenX() - 24, getScreenY() + 4, 48, "Build", buttonList);

        int fieldWidth = 120;
        nameField = new GuiTextFieldRect(4, fontRenderer, (getScreenX() + getGuiSizeX() / 2) + 40, getScreenY() - 20, fieldWidth, 32);
        readBlueprintsBtn = new GuiButtonRect(5, (getScreenX() + getGuiSizeX() / 2) + 40 + (fieldWidth / 2) - (87 / 2), getScreenY() + 4, 87, "Read Blueprints", buttonList);
        readBlocksBtn = new GuiButtonRect(6, (getScreenX() + getGuiSizeX() / 2) + 40 + (fieldWidth / 2) - (87 / 2), getScreenY() + 28, 87, "Read Blocks", buttonList);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {

        super.actionPerformed(button);

        if (button.id == upBtn.id) teBuildingUnit.currentBuildBlueprint--;
        if (button.id == downBtn.id) teBuildingUnit.currentBuildBlueprint++;

        if (button.id == buildBtn.id) {
            teBuildingUnit.placeBlueprints();
            CalemiUtils.network.sendToServer(new BuildingUnitPacket("build%" + PacketHelper.sendLocation(teBuildingUnit.getLocation())));
            return;
        }

        if (button.id == readBlueprintsBtn.id) {
            CalemiUtils.network.sendToServer(new BuildingUnitPacket("readblueprints%" + PacketHelper.sendLocation(teBuildingUnit.getLocation()) + nameField.getText()));
            return;
        }

        if (button.id == readBlocksBtn.id) {
            CalemiUtils.network.sendToServer(new BuildingUnitPacket("readblocks%" + PacketHelper.sendLocation(teBuildingUnit.getLocation()) + nameField.getText()));
            return;
        }

        if (button.id == rotateBtn.id) {
            teBuildingUnit.addRotation();
            CalemiUtils.network.sendToServer(new BuildingUnitPacket("rotate%" + PacketHelper.sendLocation(teBuildingUnit.getLocation())));
            return;
        }

        wrapBuildBlueprintIndexes();
    }

    private List<BlueprintBuild> getBuildBlueprints() {

        return teBuildingUnit.buildBlueprints;
    }

    private void wrapBuildBlueprintIndexes() {

        int size = getBuildBlueprints().size();

        if (size > 0) {

            getBuildBlueprints().size();

            teBuildingUnit.currentBuildBlueprint %= size;

            if (teBuildingUnit.currentBuildBlueprint < 0) {
                teBuildingUnit.currentBuildBlueprint = size - 1;
            }

            CalemiUtils.network.sendToServer(new BuildingUnitPacket("synccurrentbuildblueprint%" + PacketHelper.sendLocation(teBuildingUnit.getLocation()) + teBuildingUnit.currentBuildBlueprint));
        }
    }

    @Override
    public void updateScreen() {

        if (teBuildingUnit == null) {
            mc.player.closeScreen();
        }
    }

    @Override
    public void drawGuiBackground(int mouseX, int mouseY) {

        GL11.glPushMatrix();

        GlStateManager.enableBlend();

        Minecraft.getMinecraft().getTextureManager().bindTexture(CUReference.GUI_TEXTURES);
        GL11.glTranslatef(0, -0.25F, 0);
        GuiHelper.drawRect(0, getScreenY() - 95, 0, 1, 0, width, 18);

        GL11.glPopMatrix();

        nameField.drawTextBox();

        GuiHelper.drawCenteredString("Building Unit", getScreenX(), getScreenY() - 90, 0xFFFFFF);

        int range = 4;

        for (int i = -range; i <= range; i++) {

            if (i + teBuildingUnit.currentBuildBlueprint >= 0 && i + teBuildingUnit.currentBuildBlueprint < teBuildingUnit.buildBlueprints.size()) {

                String name = null;

                BlueprintBuild blueprint = getBuildBlueprints().get(i + teBuildingUnit.currentBuildBlueprint);

                if (blueprint != null) {
                    name = blueprint.name;
                }

                if (name != null) {
                    GuiHelper.drawLimitedString((i == 0 ? "> " : "") + name, getScreenX() - 115, getScreenY() - 4 + (i * 10), 12, 0xFFFFFF);
                    GuiHelper.drawHoveringTextBox(mouseX, mouseY, 1, new GuiRect(getScreenX() - 115, getScreenY() - 4 + (i * 10), 78, 9), name);
                }
            }
        }
    }

    @Override
    public void drawGuiForeground(int mouseX, int mouseY) {

    }

    @Override
    public void onGuiClosed() {

        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public String getGuiTextureName() {

        return null;
    }

    @Override
    protected int getGuiSizeX() {

        return 0;
    }

    @Override
    public int getGuiSizeY() {

        return 0;
    }

    @Override
    protected void keyTyped(char c, int i) throws IOException {

        nameField.textboxKeyTyped(c, i);

        if (!nameField.isFocused()) {
            super.keyTyped(c, i);
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int i) throws IOException {

        super.mouseClicked(x, y, i);
        nameField.mouseClicked(x, y, i);
    }

    @Override
    public void handleMouseInput() throws IOException {

        super.handleMouseInput();

        int i = Mouse.getEventDWheel();

        if (i != 0) {

            if (i > 0) {
                teBuildingUnit.currentBuildBlueprint--;
            }

            if (i < 0) {
                teBuildingUnit.currentBuildBlueprint++;
            }

            wrapBuildBlueprintIndexes();
        }
    }

    @Override
    public boolean canCloseWithInvKey() {

        return true;
    }

    @Override
    public boolean doesGuiPauseGame() {

        return false;
    }
}
