package calemiutils.gui.base;

import calemiutils.CUReference;
import calemiutils.config.MarketItemsFile;
import calemiutils.util.helper.GuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GuiMarketTab {

    private final List<GuiButton> buttonList;
    private final RenderItem itemRender;

    public ItemStack filter = ItemStack.EMPTY;
    private final String name;
    public final List<GuiMarketButton> buttons;

    private final int buttonX;

    public final GuiRect rect;

    public GuiMarketTab(List<GuiButton> buttonList, RenderItem itemRender, String name, int x, int y, int buttonX) {

        this.buttonList = buttonList;
        this.itemRender = itemRender;
        this.name = name;
        buttons = new ArrayList<>();
        this.buttonX = buttonX;
        this.rect = new GuiRect(x - 24, y, 48, 16);
    }

    public void addButton(int id, MarketItemsFile.MarketItem marketItem) {

        if (buttons.size() < 63) {

            GuiMarketButton button = new GuiMarketButton(id, rect.x, rect.y, itemRender, marketItem, buttonList);
            button.enabled = false;
            buttons.add(button);
        }
    }

    public void renderButtons() {

        int count = 0;

        //Buttons
        for (GuiMarketButton button : buttons) {

            if (button.enabled) {

                int yOffset = 15;

                int rowSize = 9;

                int xPos = (buttonX) + ((count % rowSize) * 18);
                int yPos = (rect.y + yOffset) + ((count / rowSize) * 18);
                int size = 16;

                button.rect = new GuiRect(xPos, yPos, size, size);
                button.x = xPos;
                button.y = yPos;

                count++;
            }
        }
    }

    public void enableButtons(boolean value) {

        for (GuiMarketButton button : buttons) {
            button.enabled = value;
        }
    }

    public void renderTab() {

        GuiHelper.drawCenteredString(name, rect.x + rect.width / 2, rect.y, 0xFFFFFF);
    }

    public void renderSelectedTab() {

        Minecraft.getMinecraft().getTextureManager().bindTexture(CUReference.GUI_TEXTURES);
        GuiHelper.drawRect(rect.x, rect.y + 9, 0, 0, 100, rect.width - 1, 1);
    }
}
