package calemiutils.gui.base;

import calemiutils.CUReference;
import calemiutils.item.ItemInteractionInterfaceFilter;
import calemiutils.util.Location;
import calemiutils.util.helper.GuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GuiInteractionTab {

    private final List<GuiButton> buttonList;
    private final RenderItem itemRender;

    public ItemStack filter = ItemStack.EMPTY;
    private final ItemStack icon;
    public final String name;
    public final List<GuiInteractionButton> buttons;

    private final int x;
    private final int y;

    private final int buttonX;

    public final GuiRect rect;

    public GuiInteractionTab(List<GuiButton> buttonList, RenderItem itemRender, ItemStack icon, String name, int x, int y, int buttonX) {

        this.buttonList = buttonList;
        this.itemRender = itemRender;
        this.icon = icon;
        this.name = name;
        buttons = new ArrayList<>();
        this.x = x;
        this.y = y;
        this.buttonX = buttonX;
        this.rect = new GuiRect(x, y, 16, 16);
    }

    public GuiInteractionTab(List<GuiButton> buttonList, RenderItem itemRender, ItemStack filter, int x, int y, int buttonX) {

        this(buttonList, itemRender, ItemInteractionInterfaceFilter.getFilterIcon(filter), ItemInteractionInterfaceFilter.getFilterName(filter), x, y, buttonX);
        this.filter = filter;
    }

    public List<Location> getLocations() {

        List<Location> list = new ArrayList<>();

        for (GuiInteractionButton button : buttons) {
            list.add(button.location);
        }

        return list;
    }

    public void addButton(int id, Location location, EntityPlayer player) {

        if (buttons.size() < 63) {

            IInventory inv = location.getIInventory();

            if (inv != null && !inv.isUsableByPlayer(player)) {
                return;
            }

            ItemStack stack = new ItemStack(location.getBlock());
            GuiInteractionButton button = new GuiInteractionButton(id, x, y, itemRender, location, stack, buttonList);
            button.enabled = false;
            buttons.add(button);
        }
    }

    public void enableButtons(boolean value) {

        for (GuiInteractionButton button : buttons) {
            button.enabled = value;
        }
    }

    public void renderTab(int mouseX, int mouseY) {

        String[] strings = new String[1];
        strings[strings.length - 1] = name;
        GuiHelper.drawItemStack(itemRender, icon, rect.x, rect.y);
        GuiHelper.drawHoveringTextBox(mouseX, mouseY, 150, rect, strings);
    }

    public void renderSelectedTab() {

        Minecraft.getMinecraft().getTextureManager().bindTexture(CUReference.GUI_TEXTURES);
        GuiHelper.drawRect(x, y + 18, 0, 0, 500, 16, 1);
    }

    public void renderButtons() {

        //Buttons
        for (int i = 0; i < buttons.size(); i++) {

            GuiInteractionButton button = buttons.get(i);

            int yOffset = 34;

            int rowSize = 9;

            int xPos = (buttonX) + ((i % rowSize) * 18);
            int yPos = (y + yOffset) + ((i / rowSize) * 18);
            int size = 16;

            button.rect = new GuiRect(xPos, yPos, size, size);
            button.x = xPos;
            button.y = yPos;
        }
    }
}
