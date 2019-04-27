package calemiutils.gui;

import calemiutils.CUReference;
import calemiutils.CalemiUtils;
import calemiutils.gui.base.GuiInteractionTab;
import calemiutils.gui.base.GuiScreenBase;
import calemiutils.init.InitBlocks;
import calemiutils.item.ItemInteractionInterfaceFilter;
import calemiutils.packet.ServerPacketHandler;
import calemiutils.tileentity.TileEntityInteractionTerminal;
import calemiutils.util.Location;
import calemiutils.util.helper.GuiHelper;
import calemiutils.util.helper.PacketHelper;
import calemiutils.util.helper.SecurityHelper;
import calemiutils.util.helper.ShiftHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiInteractionTerminal extends GuiScreenBase {

    private final List<GuiInteractionTab> tabs = new ArrayList<>();
    private GuiInteractionTab activeTab;
    private int id = 0;
    private int offset;
    private final TileEntityInteractionTerminal teTerminal;

    public GuiInteractionTerminal(EntityPlayer player, TileEntityInteractionTerminal te) {

        super(player);
        this.teTerminal = te;
    }

    private int getButtonId() {

        id++;
        return id;
    }

    private void setActiveTab(GuiInteractionTab tab) {

        for (GuiInteractionTab otherTab : tabs) {
            otherTab.enableButtons(false);
        }

        tab.enableButtons(true);
        activeTab = tab;
    }

    @Override
    public void initGui() {

        Keyboard.enableRepeatEvents(true);

        super.initGui();

        offset = 0;

        int x = getScreenX() - 80;
        int y = getScreenY() - 75;

        GuiInteractionTab mainTab = new GuiInteractionTab(buttonList, itemRender, new ItemStack(InitBlocks.INTERACTION_TERMINAL), "Main", x, y, x);
        tabs.add(mainTab);

        if (teTerminal != null) {

            for (Location location : teTerminal.interfaces) {

                Location interfaceUnit = new Location(location, EnumFacing.DOWN);

                if (teTerminal.isValidFilter(interfaceUnit)) {
                    searchForTabs(location, interfaceUnit, x, y);
                }

                else {
                    mainTab.addButton(getButtonId(), location, player);
                }
            }
        }

        setActiveTab(mainTab);
    }

    private void searchForTabs(Location location, Location interfaceUnit, int x, int y) {

        for (GuiInteractionTab tab : tabs) {

            if (ItemInteractionInterfaceFilter.isSameFilter(tab.filter, teTerminal.getFilterStack(interfaceUnit))) {
                tab.addButton(getButtonId(), location, player);
                return;
            }
        }

        if (addTab(location, interfaceUnit, x, y) == null) {
            tabs.get(0).addButton(getButtonId(), location, player);
        }
    }

    private GuiInteractionTab addTab(Location location, Location interfaceUnit, int x, int y) {

        if (tabs.size() < 9) {

            offset += 18;
            GuiInteractionTab newTab = new GuiInteractionTab(buttonList, itemRender, teTerminal.getFilterStack(interfaceUnit), x + offset, y, x);
            tabs.add(newTab);

            newTab.addButton(getButtonId(), location, player);

            return newTab;
        }

        return null;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {

        super.actionPerformed(button);

        for (int i = 0; i < activeTab.buttons.size(); i++) {

            if (button.id == activeTab.buttons.get(i).id) {

                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 0.75F));

                Location location = activeTab.buttons.get(i).location;
                CalemiUtils.network.sendToServer(new ServerPacketHandler("interactionterminal-interact%" + PacketHelper.sendLocation(location) + ShiftHelper.isShift()));
                player.setSneaking(ShiftHelper.isShift());
                location.getBlock().onBlockActivated(player.world, location.getBlockPos(), location.getBlockState(), player, EnumHand.MAIN_HAND, EnumFacing.UP, 0, 0, 0);
            }
        }
    }

    @Override
    public void updateScreen() {

        activeTab.buttons.sort((o1, o2) -> o1.displayString.compareToIgnoreCase(o2.displayString));
    }

    @Override
    public void drawGuiBackground(int mouseX, int mouseY) {

        GuiHelper.drawCenteredString("Interaction Interface" + SecurityHelper.getSecuredGuiName(teTerminal), getScreenX(), getScreenY() - 90, 0xFFFFFF);
        GuiHelper.drawCenteredString(activeTab.name, getScreenX(), getScreenY() - 53, 0xFFFFFF);

        GL11.glPushMatrix();

        GlStateManager.enableBlend();

        Minecraft.getMinecraft().getTextureManager().bindTexture(CUReference.GUI_TEXTURES);
        GL11.glTranslatef(0, -0.25F, 0);
        GuiHelper.drawRect(0, getScreenY() - 76, 0, 1, 50, width, 18);

        GL11.glPopMatrix();

        for (GuiInteractionTab tab : tabs) {
            tab.renderTab(mouseX, mouseY);
        }

        activeTab.renderSelectedTab();
        activeTab.renderButtons();
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
        player.setSneaking(false);
    }

    @Override
    protected void mouseClicked(int x, int y, int i) throws IOException {

        super.mouseClicked(x, y, i);

        for (GuiInteractionTab tab : tabs) {

            if (tab.rect.contains(x, y)) {
                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
                setActiveTab(tab);
            }
        }
    }

    @Override
    public boolean canCloseWithInvKey() {

        return true;
    }

    @Override
    public boolean doesGuiPauseGame() {

        return false;
    }
}
