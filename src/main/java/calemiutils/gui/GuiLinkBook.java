package calemiutils.gui;

import calemiutils.CalemiUtils;
import calemiutils.gui.base.GuiButtonRect;
import calemiutils.gui.base.GuiScreenBase;
import calemiutils.gui.base.GuiTextFieldRect;
import calemiutils.item.ItemLinkBookLocation;
import calemiutils.packet.LinkBookPacket;
import calemiutils.util.Location;
import calemiutils.util.helper.GuiHelper;
import calemiutils.util.helper.PacketHelper;
import calemiutils.util.helper.SoundHelper;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiLinkBook extends GuiScreenBase {

    private final ItemStack stack;

    private GuiTextFieldRect nameField;

    private GuiButtonRect resetBookButton;
    private GuiButtonRect teleportButton;
    private GuiButtonRect bindLocationButton;

    private final boolean isBookInHand;

    public GuiLinkBook(EntityPlayer player, ItemStack stack) {

        super(player);
        this.stack = stack;

        isBookInHand = stack != null && !player.getHeldItemMainhand().isEmpty() && stack.equals(player.getHeldItemMainhand());
    }

    private ItemLinkBookLocation getBook() {

        if (stack != null) {

            if (stack.getItem() instanceof ItemLinkBookLocation) {
                return (ItemLinkBookLocation) stack.getItem();
            }
        }

        return null;
    }

    private void setName(String name) {

        if (isBookInHand && getBook() != null) {
            CalemiUtils.network.sendToServer(new LinkBookPacket("name%" + name));
            ItemLinkBookLocation.bindName(stack, name);
        }
    }

    @Override
    public void initGui() {

        Keyboard.enableRepeatEvents(true);

        super.initGui();

        if (isBookInHand) {

            nameField = new GuiTextFieldRect(0, fontRenderer, getScreenX() - 80 - 8, getScreenY() - 50 - 8, 160, 32);

            if (stack != null) {

                if (stack.hasDisplayName()) {
                    nameField.setText(stack.getDisplayName());
                }
            }

            new GuiButtonRect(0, getScreenX() + 80 - 4, getScreenY() - 50 - 8, 16, "+", buttonList);
            bindLocationButton = new GuiButtonRect(2, getScreenX() - 50, getScreenY() + 35 - 8, 100, "Bind Location", buttonList);
            resetBookButton = new GuiButtonRect(3, getScreenX() - 50, getScreenY() + 70 - 8, 100, "Reset Book", buttonList);
        }

        teleportButton = new GuiButtonRect(1, getScreenX() - 50, getScreenY() - 8, 100, "Teleport", buttonList);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {

        super.actionPerformed(button);

        if (isBookInHand) {

            setName(nameField.getText());

            if (button.id == bindLocationButton.id) {

                Location location = new Location(player.world, (int) Math.floor(player.posX), (int) Math.floor(player.posY), (int) Math.floor(player.posZ));
                int dim = player.world.provider.getDimension();

                CalemiUtils.network.sendToServer(new LinkBookPacket("bind%" + PacketHelper.sendLocation(location) + dim));
                ItemLinkBookLocation.bindLocation(stack, player, location, true);
            }

            if (button.id == resetBookButton.id) {
                setName("");
                CalemiUtils.network.sendToServer(new LinkBookPacket("reset%"));
                ItemLinkBookLocation.resetLocation(stack, player);
                nameField.setText("");
            }
        }

        if (button.id == teleportButton.id) {

            if (getBook() != null && getBook().isLinked(stack)) {

                Location location = ItemLinkBookLocation.getLinkedLocation(player.world, stack);

                if (location != null) {
                    mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 0.75F));
                    SoundHelper.playWarp(player.world, player);
                    CalemiUtils.network.sendToServer(new LinkBookPacket("teleport%" + PacketHelper.sendLocation(location) + ItemLinkBookLocation.getLinkedDimension(stack)));

                    mc.player.closeScreen();
                }
            }
        }
    }

    @Override
    public void updateScreen() {

        super.updateScreen();

        Location location = ItemLinkBookLocation.getLinkedLocation(player.world, stack);

        teleportButton.enabled = location != null;
        if (isBookInHand) resetBookButton.enabled = location != null;
    }

    @Override
    public void drawGuiBackground(int mouseX, int mouseY) {

        if (isBookInHand) {
            GuiHelper.drawCenteredString("Name Book", getScreenX(), getScreenY() - 67, 0xFFFFFF);

            nameField.drawTextBox();
        }

        if (getBook() != null) {

            Location location = ItemLinkBookLocation.getLinkedLocation(player.world, stack);
            String string = "Not set";

            if (getBook().isLinked(stack) && location != null) {
                GuiHelper.drawCenteredString(stack.getDisplayName(), getScreenX(), getScreenY() - 28, 0xFFFFFF);
                string = location.toString();
            }

            GuiHelper.drawCenteredString(string, getScreenX(), getScreenY() - 18, 0xFFFFFF);
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
    public int getGuiSizeX() {

        return 0;
    }

    @Override
    public int getGuiSizeY() {

        return 0;
    }

    @Override
    protected void keyTyped(char c, int i) throws IOException {

        super.keyTyped(c, i);
        if (isBookInHand) nameField.textboxKeyTyped(c, i);

        if (getBook() != null) {

            teleportButton.enabled = getBook().isLinked(stack);

            if (isBookInHand) {

                resetBookButton.enabled = getBook().isLinked(stack);

                if (nameField.isFocused()) {

                    if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
                        setName(nameField.getText());
                    }
                }
            }
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int i) throws IOException {

        super.mouseClicked(x, y, i);
        if (isBookInHand) nameField.mouseClicked(x, y, i);
    }

    @Override
    public boolean canCloseWithInvKey() {

        if (isBookInHand) {
            return !nameField.isFocused();
        }

        return true;
    }

    @Override
    public boolean doesGuiPauseGame() {

        return false;
    }
}
