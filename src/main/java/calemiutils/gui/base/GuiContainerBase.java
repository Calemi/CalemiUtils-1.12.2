package calemiutils.gui.base;

import calemiutils.security.ISecurity;
import calemiutils.tileentity.base.ICurrencyNetwork;
import calemiutils.tileentity.base.IProgress;
import calemiutils.tileentity.base.TileEntityBase;
import calemiutils.tileentity.base.TileEntityUpgradable;
import calemiutils.util.helper.GuiHelper;
import calemiutils.util.helper.MathHelper;
import calemiutils.util.helper.StringHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import org.lwjgl.opengl.GL11;

public abstract class GuiContainerBase extends GuiContainer {

    protected static final int TEXT_COLOR = 0x555555;

    protected final EntityPlayer player;
    protected TileEntityBase te;

    private int leftTabOffset;
    private int rightTabOffset;

    protected GuiContainerBase(Container container, EntityPlayer player) {

        super(container);
        this.xSize = getGuiSizeX();
        this.ySize = getGuiSizeY();
        this.player = player;

        leftTabOffset = 4;
        rightTabOffset = 4;
    }

    protected GuiContainerBase(Container container, EntityPlayer player, TileEntityBase te) {

        this(container, player);
        this.te = te;
    }

    protected abstract String getGuiTextureName();

    protected abstract String getGuiTitle();

    protected abstract void drawGuiBackground(int mouseX, int mouseY);

    protected abstract void drawGuiForeground(int mouseX, int mouseY);

    @SuppressWarnings("SameReturnValue")
    protected int getGuiSizeX() {

        return 176;
    }

    protected int getGuiSizeY() {

        return 176;
    }

    protected int getScreenX() {

        return (this.width - getGuiSizeX()) / 2;
    }

    protected int getScreenY() {

        return (this.height - getGuiSizeY()) / 2;
    }

    private void addUpgradeSlot(int index) {

        GuiHelper.bindGuiTextures();
        addRightInfoText("", 15, 22);

        GuiHelper.bindGuiTextures();
        GuiHelper.drawRect(getScreenX() + getGuiSizeX() + 2, getScreenY() + 6 + (index * 24), (index * 18), 19, 5, 18, 18);
    }

    private void addProgressBar(int progress, int maxProgress) {

        GuiHelper.bindGuiTextures();
        GuiRect rect = new GuiRect(getScreenX() - 13, getScreenY() + leftTabOffset, 13, 35);

        int scale = MathHelper.scaleInt(progress, maxProgress, 26);

        GuiHelper.drawRect(rect.x, rect.y, 0, 37, 0, rect.width, rect.height);
        GuiHelper.drawRect(getScreenX() - 8, getScreenY() + 30 + leftTabOffset - scale, 13, 62 - scale, 0, 5, scale);
    }

    private void addProgressBarText(int mouseX, int mouseY, int progress, int maxProgress) {

        GuiRect rect = new GuiRect(getScreenX() - 13, getScreenY() + leftTabOffset, 13, 35);
        GuiHelper.drawHoveringTextBox(mouseX, mouseY, 170, rect, "Progress: " + MathHelper.scaleInt(progress, maxProgress, 100) + "%");
    }

    protected void addInfoIcon() {

        GL11.glDisable(GL11.GL_LIGHTING);
        GuiHelper.bindGuiTextures();
        GuiHelper.drawRect(getScreenX() - 13, getScreenY() + leftTabOffset, 0, 72, 2, 13, 15);
    }

    protected void addInfoIconText(int mouseX, int mouseY, String... text) {

        GuiRect rect = new GuiRect(getScreenX() - 13, getScreenY() + leftTabOffset, 13, 15);
        GuiHelper.drawHoveringTextBox(mouseX, mouseY, 170, rect, text);
    }

    private void addCurrencyInfo(int mouseX, int mouseY, int currency, int maxCurrency) {

        String fullName = StringHelper.printCurrency(currency) + " / " + StringHelper.printCurrency(maxCurrency);

        int fullWidth = mc.fontRenderer.getStringWidth(fullName) + 6;

        GuiRect rect = new GuiRect(getScreenX() - fullWidth, getScreenY() + leftTabOffset, fullWidth, 15);
        String text = StringHelper.printCurrency(currency);

        if (rect.contains(mouseX, mouseY)) {
            text = fullName;
        }

        addLeftInfoText(text, 15);
    }

    private void addRightInfoText(String text, int sizeAdd, int sizeY) {

        int width = mc.fontRenderer.getStringWidth(text) + sizeAdd + 7;

        GuiHelper.bindGuiTextures();
        GuiHelper.drawCappedRect(getScreenX() + getGuiSizeX(), getScreenY() + rightTabOffset, 0, 116, 2, width, sizeY, 256, 22);

        if (!text.isEmpty()) {
            GlStateManager.pushMatrix();
            GL11.glTranslatef(0, 0, 5);
            GL11.glColor3f(0.35F, 0.35F, 0.35F);
            mc.fontRenderer.drawString(text, getScreenX() + getGuiSizeX() + 4, getScreenY() + (sizeY / 2) - 3 + rightTabOffset, TEXT_COLOR);
            GL11.glColor3f(1, 1, 1);
            GlStateManager.popMatrix();
        }

        rightTabOffset += (sizeY + 2);
    }

    private void addLeftInfoText(String text, int sizeY) {

        int width = mc.fontRenderer.getStringWidth(text) + 6;

        GuiHelper.bindGuiTextures();
        GuiHelper.drawCappedRect(getScreenX() - width, getScreenY() + leftTabOffset, 0, 116, 2, width, sizeY, 255, 22);

        if (!text.isEmpty()) {
            GlStateManager.pushMatrix();
            GL11.glColor3f(0.35F, 0.35F, 0.35F);
            GL11.glTranslatef(0, 0, 5);
            mc.fontRenderer.drawString(text, getScreenX() - width + 4, getScreenY() + (sizeY / 2) - 3 + leftTabOffset, TEXT_COLOR);
            GL11.glColor3f(1, 1, 1);
            GlStateManager.popMatrix();
        }

        leftTabOffset += (sizeY + 2);
    }

    private void addGraphicsBeforeRendering() {

        GuiHelper.bindGuiTextures();

        if (te != null) {

            if (te instanceof TileEntityUpgradable) {

                addUpgradeSlot(0);
                addUpgradeSlot(1);
            }

            if (te instanceof IProgress) {
                addProgressBar(((IProgress) te).getCurrentProgress(), ((IProgress) te).getMaxProgress());
            }
        }
    }

    private void addGraphicsAfterRendering(int mouseX, int mouseY) {

        GlStateManager.disableLighting();
        GuiHelper.bindGuiTextures();

        if (te != null) {

            if (te instanceof ISecurity) {
                String name = ((ISecurity) te).getSecurityProfile().getOwnerName();
                int width = mc.fontRenderer.getStringWidth(name) + 5;
                GuiHelper.drawCappedRect(getScreenX() + (getGuiSizeX() / 2) - (width / 2), getScreenY() + getGuiSizeY(), 0, 116, 0, width, 13, 256, 22);
                GuiHelper.drawCenteredString(name, getScreenX() + (getGuiSizeX() / 2), getScreenY() + getGuiSizeY() + 3, TEXT_COLOR);
                GL11.glColor3f(1, 1, 1);
            }

            if (te instanceof ICurrencyNetwork) {
                GL11.glColor3f(1, 1, 1);
                addCurrencyInfo(mouseX, mouseY, ((ICurrencyNetwork) te).getStoredCurrency(), ((ICurrencyNetwork) te).getMaxCurrency());
            }

            if (te instanceof IProgress) {
                GL11.glColor3f(1, 1, 1);
                addProgressBarText(mouseX, mouseY, ((IProgress) te).getCurrentProgress(), ((IProgress) te).getMaxProgress());
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {

        leftTabOffset = 4;
        rightTabOffset = 4;

        updateScreen();

        GuiHelper.bindTexture(getGuiTextureName());
        GuiHelper.drawRect(getScreenX(), getScreenY(), 0, 0, 0, getGuiSizeX(), getGuiSizeY());

        drawGuiBackground(mouseX, mouseY);

        GlStateManager.disableColorMaterial();
        GuiHelper.drawCenteredString(getGuiTitle(), getScreenX() + getGuiSizeX() / 2, getScreenY() + 6, TEXT_COLOR);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float f) {

        this.drawDefaultBackground();

        addGraphicsBeforeRendering();

        super.drawScreen(mouseX, mouseY, f);

        addGraphicsAfterRendering(mouseX, mouseY);

        drawGuiForeground(mouseX, mouseY);

        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
