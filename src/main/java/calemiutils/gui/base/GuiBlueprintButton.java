package calemiutils.gui.base;

import calemiutils.util.helper.GuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiBlueprintButton extends GuiButton {

    private final GuiRect rect;
    private final ItemStack stack;
    private final RenderItem itemRender;

    public GuiBlueprintButton(int id, int x, int y, RenderItem itemRender, ItemStack stack, List<GuiButton> buttonList) {

        super(id, x, y, 16, 16, "");
        rect = new GuiRect(this.x, this.y, width, height);
        this.stack = stack;
        this.itemRender = itemRender;
        buttonList.add(this);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {

        if (this.visible && enabled) {

            hovered = rect.contains(mouseX, mouseY);

            ItemStack icon = stack;

            GuiHelper.drawItemStack(itemRender, icon, rect.x, rect.y);
            GuiHelper.drawHoveringTextBox(mouseX, mouseY, 150, rect, icon.getDisplayName());
            this.displayString = icon.getDisplayName();

            GL11.glColor4f(1, 1, 1, 1);
        }
    }
}
