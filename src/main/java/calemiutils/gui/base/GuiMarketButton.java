package calemiutils.gui.base;

import calemiutils.config.MarketItemsFile;
import calemiutils.util.helper.GuiHelper;
import calemiutils.util.helper.StringHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiMarketButton extends GuiButton {

    GuiRect rect;
    private final MarketItemsFile.MarketItem marketItem;
    private final RenderItem itemRender;

    GuiMarketButton(int id, int x, int y, RenderItem itemRender, MarketItemsFile.MarketItem marketItem, List<GuiButton> buttonList) {

        super(id, x, y, 16, 16, "");
        rect = new GuiRect(this.x, this.y, width, height);
        this.marketItem = marketItem;
        this.itemRender = itemRender;
        buttonList.add(this);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {

        if (this.visible && enabled) {

            hovered = rect.contains(mouseX, mouseY);

            ItemStack[] stacks;

            if (!marketItem.isBuy && MarketItemsFile.MarketItem.doesOreNameExist(marketItem.stackObj)) {
                stacks = MarketItemsFile.MarketItem.getStacksFromOreDict(marketItem.stackObj);
            }

            else {
                stacks = new ItemStack[]{marketItem.getStack()};
            }

            if (stacks.length > 0 && !stacks[0].isEmpty()) {

                String[] strings = new String[2];

                strings[0] = marketItem.amount + "x " + stacks[0].getDisplayName();
                strings[1] = "Value " + StringHelper.printCurrency(marketItem.value);

                GuiHelper.drawItemStack(itemRender, stacks[0], rect.x, rect.y);
                GuiHelper.drawHoveringTextBox(mouseX, mouseY, 150, rect, strings);
                this.displayString = stacks[0].getDisplayName();

                GL11.glColor4f(1, 1, 1, 1);
            }

            else {
                enabled = false;
            }
        }
    }
}
