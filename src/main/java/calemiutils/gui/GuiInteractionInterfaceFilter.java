package calemiutils.gui;

import calemiutils.CalemiUtils;
import calemiutils.gui.base.GuiContainerBase;
import calemiutils.gui.base.GuiFakeSlot;
import calemiutils.gui.base.GuiTextFieldRect;
import calemiutils.inventory.ContainerInteractionInterfaceFilter;
import calemiutils.item.ItemInteractionInterfaceFilter;
import calemiutils.packet.ServerPacketHandler;
import calemiutils.util.helper.ItemHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiInteractionInterfaceFilter extends GuiContainerBase {

    private GuiFakeSlot fakeSlot;
    private GuiTextFieldRect nameField;

    public GuiInteractionInterfaceFilter(EntityPlayer player) {

        super(new ContainerInteractionInterfaceFilter(player), player);
    }

    @Override
    public int getGuiSizeY() {

        return 144;
    }

    @Override
    public String getGuiTextureName() {

        return "interaction_interface_filter";
    }

    @Override
    public String getGuiTitle() {

        return "Interaction Interface Filter";
    }

    private ItemStack getHeldFilterStack() {

        return player.getHeldItemMainhand();
    }


    private ItemInteractionInterfaceFilter getHeldFilter() {

        if (!getHeldFilterStack().isEmpty()) {

            if (getHeldFilterStack().getItem() instanceof ItemInteractionInterfaceFilter) {
                return (ItemInteractionInterfaceFilter) getHeldFilterStack().getItem();
            }
        }

        return null;
    }

    @Override
    public void initGui() {

        Keyboard.enableRepeatEvents(true);

        super.initGui();

        int fieldWidth = 120;

        fakeSlot = new GuiFakeSlot(6, getScreenX() + 80, getScreenY() + 18, itemRender, buttonList);
        nameField = new GuiTextFieldRect(0, fontRenderer, (getScreenX() + getGuiSizeX() / 2) - (fieldWidth / 2) - 1, getScreenY() + 38, fieldWidth, 32);

        if (getHeldFilter() != null) {

            ItemStack stack = ItemInteractionInterfaceFilter.getFilterIcon(getHeldFilterStack());

            fakeSlot.setItemStack(stack);

            nameField.setText(ItemInteractionInterfaceFilter.getFilterName(getHeldFilterStack()));
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {

        InventoryPlayer inv = mc.player.inventory;

        if (button.id == fakeSlot.id) {

            if (getHeldFilter() != null) {

                ItemStack stack = new ItemStack(inv.getItemStack().getItem(), 1, inv.getItemStack().getMetadata());

                ItemInteractionInterfaceFilter.setFilterIcon(getHeldFilterStack(), stack);
                fakeSlot.setItemStack(stack);
                CalemiUtils.network.sendToServer(new ServerPacketHandler("iifilter-seticon%" + ItemHelper.getStringFromStack(stack)));
            }
        }
    }

    @Override
    public void updateScreen() {

        super.updateScreen();

        if (getHeldFilter() != null) {

            if (nameField.isFocused()) {

                if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
                    CalemiUtils.network.sendToServer(new ServerPacketHandler("iifilter-setname%" + nameField.getText()));
                }
            }
        }

        else {
            Minecraft.getMinecraft().player.closeScreen();
        }
    }

    @Override
    public void drawGuiBackground(int mouseX, int mouseY) {

        nameField.drawTextBox();
    }

    @Override
    public void drawGuiForeground(int mouseX, int mouseY) {

        fakeSlot.renderButton(mouseX, mouseY, 150);
    }

    @Override
    public void onGuiClosed() {

        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
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
}
