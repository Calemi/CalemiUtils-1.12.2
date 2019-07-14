package calemiutils.gui;

import calemiutils.CalemiUtils;
import calemiutils.gui.base.GuiContainerBase;
import calemiutils.gui.base.GuiFakeSlot;
import calemiutils.gui.base.GuiTextFieldRect;
import calemiutils.item.ItemInteractionInterfaceFilter;
import calemiutils.packet.InteractionUnitPacket;
import calemiutils.tileentity.TileEntityInteractionInterface;
import calemiutils.util.helper.ItemHelper;
import calemiutils.util.helper.PacketHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiInteractionInterface extends GuiContainerBase {

    private final TileEntityInteractionInterface teInterface;
    private GuiFakeSlot tabIconSlot;
    private GuiFakeSlot blockIconSlot;
    private GuiTextFieldRect blockNameField;

    public GuiInteractionInterface(EntityPlayer player, TileEntityInteractionInterface te) {

        super(te.getTileContainer(player), player, te);
        this.teInterface = te;
    }

    @Override
    public String getGuiTextureName() {

        return "interaction_interface";
    }

    @Override
    public String getGuiTitle() {

        return "Interaction Interface";
    }

    @Override
    public void initGui() {

        super.initGui();

        int fieldWidth = 100;

        tabIconSlot = new GuiFakeSlot(0, getScreenX() + 44, getScreenY() + 18, itemRender, buttonList);
        blockIconSlot = new GuiFakeSlot(1, getScreenX() + 116, getScreenY() + 18, itemRender, buttonList);
        blockNameField = new GuiTextFieldRect(0, fontRenderer, (getScreenX() + getGuiSizeX()) - fieldWidth - 9, getScreenY() + 39, fieldWidth, 32);

        tabIconSlot.setItemStack(teInterface.tabIconStack);
        blockIconSlot.setItemStack(teInterface.blockIconStack);
        blockNameField.setText(teInterface.blockName);
    }

    @Override
    protected void actionPerformed(GuiButton button) {

        InventoryPlayer inv = mc.player.inventory;

        if (button.id == tabIconSlot.id || button.id == blockIconSlot.id) {

            ItemStack stack = inv.getItemStack();

            if (stack.isEmpty()) {

                if (button.id == tabIconSlot.id) {

                    CalemiUtils.network.sendToServer(new InteractionUnitPacket("interface-clearfilter%" + PacketHelper.sendLocation(teInterface.getLocation())));

                    teInterface.tabIconStack = ItemStack.EMPTY;
                    tabIconSlot.setItemStack(ItemStack.EMPTY);
                }

                if (button.id == blockIconSlot.id) {

                    CalemiUtils.network.sendToServer(new InteractionUnitPacket("interface-clearblock%" + PacketHelper.sendLocation(teInterface.getLocation())));

                    teInterface.blockIconStack = ItemStack.EMPTY;
                    blockIconSlot.setItemStack(ItemStack.EMPTY);
                }
            }

            else {

                if (button.id == tabIconSlot.id && teInterface.canSetFilter(stack)) {

                    ItemStack filterIcon = ItemInteractionInterfaceFilter.getFilterIcon(stack);
                    String filterName = ItemInteractionInterfaceFilter.getFilterName(stack);

                    CalemiUtils.network.sendToServer(new InteractionUnitPacket("interface-setfilter%" + PacketHelper.sendLocation(teInterface.getLocation()) + ItemHelper.getStringFromStack(filterIcon) + "%" + filterName));

                    teInterface.tabIconStack = stack;
                    tabIconSlot.setItemStack(stack);
                }

                else if (button.id == blockIconSlot.id) {
                    CalemiUtils.network.sendToServer(new InteractionUnitPacket("interface-setblock%" + PacketHelper.sendLocation(teInterface.getLocation()) + ItemHelper.getStringFromStack(stack)));

                    teInterface.blockIconStack = stack;
                    blockIconSlot.setItemStack(stack);
                }
            }
        }
    }

    @Override
    public void updateScreen() {

        super.updateScreen();

        if (blockNameField.isFocused()) {

            if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {

                CalemiUtils.network.sendToServer(new InteractionUnitPacket("interface-setblockname%" + PacketHelper.sendLocation(teInterface.getLocation()) + blockNameField.getText()));
                teInterface.blockName = blockNameField.getText();
                blockNameField.setFocused(false);
            }
        }
    }

    @Override
    public int getGuiSizeY() {

        return 144;
    }

    @Override
    public void drawGuiBackground(int mouseX, int mouseY) {

        mc.fontRenderer.drawString("Filter", getScreenX() + 8, getScreenY() + 22, TEXT_COLOR);
        mc.fontRenderer.drawString("Icon", getScreenX() + 84, getScreenY() + 22, TEXT_COLOR);

        mc.fontRenderer.drawString("Block Name", getScreenX() + 8, getScreenY() + 43, TEXT_COLOR);

        blockNameField.drawTextBox();
    }

    @Override
    public void drawGuiForeground(int mouseX, int mouseY) {

        tabIconSlot.renderButton(mouseX, mouseY, 500);
        blockIconSlot.renderButton(mouseX, mouseY, 500);
    }

    @Override
    public void onGuiClosed() {

        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void keyTyped(char c, int i) throws IOException {

        blockNameField.textboxKeyTyped(c, i);

        if (!blockNameField.isFocused()) {
            super.keyTyped(c, i);
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int i) throws IOException {

        super.mouseClicked(x, y, i);
        blockNameField.mouseClicked(x, y, i);
    }
}
