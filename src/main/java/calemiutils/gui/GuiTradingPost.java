package calemiutils.gui;

import calemiutils.CalemiUtils;
import calemiutils.gui.base.GuiButtonRect;
import calemiutils.gui.base.GuiContainerBase;
import calemiutils.gui.base.GuiFakeSlot;
import calemiutils.packet.ServerPacketHandler;
import calemiutils.tileentity.TileEntityTradingPost;
import calemiutils.util.helper.*;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiTradingPost extends GuiContainerBase {

    private final TileEntityTradingPost tePost;

    private GuiButtonRect sellModeButton;
    private GuiFakeSlot fakeSlot;

    private final int upY = 40;
    private final int downY = 59;

    public GuiTradingPost(EntityPlayer player, TileEntityTradingPost te) {

        super(te.getTileContainer(player), player, te);
        this.tePost = te;
    }

    @Override
    public int getGuiSizeY() {

        return 223;
    }

    @Override
    public String getGuiTextureName() {

        return "trading_post";
    }

    @Override
    public String getGuiTitle() {

        return "Trading Post";
    }

    @Override
    public void initGui() {

        super.initGui();

        new GuiButtonRect(0, getScreenX() + 49, getScreenY() + upY, 16, "-", buttonList);
        new GuiButtonRect(1, getScreenX() + 111, getScreenY() + upY, 16, "+", buttonList);

        new GuiButtonRect(2, getScreenX() + 49, getScreenY() + downY, 16, "-", buttonList);
        new GuiButtonRect(3, getScreenX() + 111, getScreenY() + downY, 16, "+", buttonList);

        new GuiButtonRect(4, getScreenX() + 130, getScreenY() + upY, 16, "R", buttonList);
        new GuiButtonRect(5, getScreenX() + 130, getScreenY() + downY, 16, "R", buttonList);

        sellModeButton = new GuiButtonRect(6, getScreenX() + 21, getScreenY() + 19, 39, tePost.buyMode ? "Buying" : "Selling", buttonList);

        fakeSlot = new GuiFakeSlot(7, getScreenX() + 80, getScreenY() + 19, itemRender, buttonList);
        fakeSlot.setItemStack(tePost.getStackForSale());
    }

    @Override
    protected void actionPerformed(GuiButton button) {

        int i = ShiftHelper.getShiftCtrlInt(1, 10, 100, 1000);

        InventoryPlayer inv = mc.player.inventory;

        if (button.id == fakeSlot.id) {

            ItemStack stack = new ItemStack(inv.getItemStack().getItem(), 1, inv.getItemStack().getMetadata());

            CalemiUtils.network.sendToServer(new ServerPacketHandler("tradingpost-setstackforsale%" + PacketHelper.sendLocation(tePost.getLocation()) + ItemHelper.getStringFromStack(stack)));
            tePost.setStackForSale(stack);
            fakeSlot.setItemStack(stack);
        }

        else if (button.id == sellModeButton.id) {

            boolean mode = !tePost.buyMode;

            CalemiUtils.network.sendToServer(new ServerPacketHandler("tradingpost-togglesellmode%" + PacketHelper.sendLocation(tePost.getLocation()) + mode));
            tePost.buyMode = mode;
        }

        else {

            if (button.id == 0 || button.id == 2) i *= -1;

            String str = "";

            int value = 0;

            if (button.id == 0 || button.id == 1) {
                str = "amount";

                tePost.amountForSale += i;
                value = tePost.amountForSale;
            }

            if (button.id == 2 || button.id == 3) {
                str = "price";

                tePost.salePrice += i;
                value = tePost.salePrice;
            }

            if (button.id == 4) {
                str = "amount";
                tePost.amountForSale = 1;
                value = 1;
            }

            if (button.id == 5) {
                str = "price";
                tePost.salePrice = 0;
                value = 0;
            }

            tePost.amountForSale = MathHelper.clamp(tePost.amountForSale, 1, 1000);
            tePost.salePrice = MathHelper.clamp(tePost.salePrice, 0, 10000);
            CalemiUtils.network.sendToServer(new ServerPacketHandler("tradingpost-setoptions" + "%" + str + "%" + value + "%" + tePost.getPos().getX() + "%" + tePost.getPos().getY() + "%" + tePost.getPos().getZ()));
        }
    }

    @Override
    public void drawGuiBackground(int mouseX, int mouseY) {

        // Titles
        mc.fontRenderer.drawString("Amount", getScreenX() + 10, getScreenY() + upY + 4, TEXT_COLOR);
        mc.fontRenderer.drawString("Price", getScreenX() + 10, getScreenY() + downY + 4, TEXT_COLOR);

        GuiHelper.drawCenteredString(StringHelper.printCommas(tePost.amountForSale), getScreenX() + getGuiSizeX() / 2, getScreenY() + upY + 4, TEXT_COLOR);
        GuiHelper.drawCenteredString(StringHelper.printCommas(tePost.salePrice), getScreenX() + getGuiSizeX() / 2, getScreenY() + downY + 4, TEXT_COLOR);

        sellModeButton.displayString = tePost.buyMode ? "Buying" : "Selling";
    }

    @Override
    public void drawGuiForeground(int mouseX, int mouseY) {

        GL11.glDisable(GL11.GL_LIGHTING);
        addInfoIcon();
        addInfoIconText(mouseX, mouseY, "Button Click Info", "Shift: 10, Ctrl: 100, Shift + Ctrl: 100");

        fakeSlot.renderButton(mouseX, mouseY, 150);
    }
}
