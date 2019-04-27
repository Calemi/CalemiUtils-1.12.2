package calemiutils.gui.base;

import calemiutils.CUReference;
import calemiutils.util.helper.GuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiButtonRect extends GuiButton {

    private final GuiRect rect;

    public GuiButtonRect(int id, int x, int y, int width, String text, List<GuiButton> buttonList) {

        super(id, x, y, width, 16, text);
        rect = new GuiRect(this.x, this.y, width, height);
        buttonList.add(this);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {

        if (this.visible) {

            if (rect.contains(mouseX, mouseY)) {
                GL11.glColor4f(1F, 1F, 1F, 1F);
            }

            else {
                GL11.glColor4f(0.8F, 0.8F, 0.8F, 8F);
            }

            if (!enabled) {
                GL11.glColor4f(0.5F, 0.5F, 0.5F, 1F);
            }

            mc.getTextureManager().bindTexture(CUReference.GUI_TEXTURES);
            GuiHelper.drawCappedRect(rect.x, rect.y, 0, 240, (int) zLevel, rect.width, rect.height, 256, 16);

            GL11.glColor4f(1, 1, 1, 1);

            GuiHelper.drawCenteredString(displayString, rect.x + (rect.width / 2), rect.y + (this.height - 8) / 2, 0xFFFFFF);
        }
    }
}
