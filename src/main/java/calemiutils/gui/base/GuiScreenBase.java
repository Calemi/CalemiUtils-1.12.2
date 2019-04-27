package calemiutils.gui.base;

import calemiutils.util.helper.GuiHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;

import java.io.IOException;

public abstract class GuiScreenBase extends GuiScreen {

    protected final EntityPlayer player;

    protected GuiScreenBase(EntityPlayer player) {

        super();
        this.player = player;
    }

    @SuppressWarnings("SameReturnValue")
    protected abstract String getGuiTextureName();

    @SuppressWarnings("SameReturnValue")
    protected abstract int getGuiSizeX();

    @SuppressWarnings("SameReturnValue")
    protected abstract int getGuiSizeY();

    protected abstract void drawGuiBackground(int mouseX, int mouseY);

    @SuppressWarnings("EmptyMethod")
    protected abstract void drawGuiForeground(int mouseX, int mouseY);

    protected abstract boolean canCloseWithInvKey();

    protected int getScreenX() {

        return (this.width - getGuiSizeX()) / 2;
    }

    protected int getScreenY() {

        return (this.height - getGuiSizeY()) / 2;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float f1) {

        updateScreen();

        drawDefaultBackground();

        if (getGuiTextureName() != null) {
            GuiHelper.bindTexture(getGuiTextureName());
            drawTexturedModalRect(getScreenX(), getScreenY(), 0, 0, getGuiSizeX(), getGuiSizeY());
        }

        drawGuiBackground(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, f1);
        drawGuiForeground(mouseX, mouseY);
    }

    @Override
    protected void keyTyped(char c, int keyID) throws IOException {

        super.keyTyped(c, keyID);

        if (canCloseWithInvKey() && keyID == mc.gameSettings.keyBindInventory.getKeyCode()) {
            mc.player.closeScreen();
        }
    }
}
