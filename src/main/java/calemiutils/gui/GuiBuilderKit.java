package calemiutils.gui;

import calemiutils.CalemiUtils;
import calemiutils.config.CUConfig;
import calemiutils.gui.base.GuiButtonRect;
import calemiutils.gui.base.GuiContainerBase;
import calemiutils.inventory.ContainerBuildersKit;
import calemiutils.item.ItemBuildersKit;
import calemiutils.packet.BuildersKitPacket;
import calemiutils.util.helper.GuiHelper;
import calemiutils.util.helper.ItemHelper;
import calemiutils.util.helper.ShiftHelper;
import calemiutils.util.helper.StringHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiBuilderKit extends GuiContainerBase {

    private GuiButtonRect extractButton;
    private GuiButtonRect toggleSuckButton;
    private GuiButtonRect resetBlockButton;

    public GuiBuilderKit(EntityPlayer player) {

        super(new ContainerBuildersKit(player), player);
    }

    @Override
    public String getGuiTextureName() {

        return "builders_kit";
    }

    @Override
    public String getGuiTitle() {

        return "Builder's Kit";
    }

    @Override
    public void initGui() {

        super.initGui();

        extractButton = new GuiButtonRect(0, getScreenX() + 124, getScreenY() + 50, 16, "+", buttonList);

        toggleSuckButton = new GuiButtonRect(1, getScreenX() + 14, getScreenY() + 50, 54, "", buttonList);
        resetBlockButton = new GuiButtonRect(2, getScreenX() + getGuiSizeX() / 2 - (66 / 2), getScreenY() + 71, 66, "Reset Block", buttonList);
    }

    @Override
    protected void actionPerformed(GuiButton button) {

        int multiplier = ShiftHelper.getShiftCtrlInt(1, 16, 64, 9 * 64);

        if (player.getHeldItemMainhand().getItem() instanceof ItemBuildersKit) {

            if (button.id == extractButton.id) {
                CalemiUtils.network.sendToServer(new BuildersKitPacket("extractblocks%" + multiplier));

                if (ItemHelper.getNBT(player.getHeldItemMainhand()).getBoolean("suck")) {
                    CalemiUtils.network.sendToServer(new BuildersKitPacket("togglesuck"));
                }
            }

            if (button.id == toggleSuckButton.id) {
                CalemiUtils.network.sendToServer(new BuildersKitPacket("togglesuck"));
            }

            if (button.id == resetBlockButton.id) {
                CalemiUtils.network.sendToServer(new BuildersKitPacket("resetblock"));
            }
        }
    }

    @Override
    public void drawGuiBackground(int mouseX, int mouseY) {

        toggleSuckButton.displayString = "Suck: " + (ItemHelper.getNBT(player.getHeldItemMainhand()).getBoolean("suck") ? "ON" : "OFF");

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glColor4f(1, 1, 1, 1);

        ItemStack stack = player.getHeldItemMainhand();

        if (stack.getItem() instanceof ItemBuildersKit) {
            GuiHelper.drawCenteredString("Block: " + ((ItemBuildersKit) stack.getItem()).getBlockName(stack), getScreenX() + getGuiSizeX() / 2, getScreenY() + 25, TEXT_COLOR);
            GuiHelper.drawCenteredString("Amount: " + StringHelper.printCommas(ItemBuildersKit.getAmountOfBlocks(stack)) + " / " + StringHelper.printCommas(CUConfig.misc.buildersKitCapacity), getScreenX() + getGuiSizeX() / 2, getScreenY() + 35, TEXT_COLOR);
        }

        else {
            Minecraft.getMinecraft().player.closeScreen();
        }
    }

    @Override
    public void drawGuiForeground(int mouseX, int mouseY) {

        GL11.glDisable(GL11.GL_LIGHTING);
        addInfoIcon();
        addInfoIconText(mouseX, mouseY, "Button Click Info", "Shift: 16, Ctrl: 64, Shift + Ctrl: 64 * 9");
    }
}
