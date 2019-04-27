package calemiutils.gui;

import calemiutils.CalemiUtils;
import calemiutils.gui.base.GuiButtonRect;
import calemiutils.gui.base.GuiContainerBase;
import calemiutils.init.InitItems;
import calemiutils.inventory.ContainerWallet;
import calemiutils.item.ItemWallet;
import calemiutils.packet.ServerPacketHandler;
import calemiutils.util.helper.GuiHelper;
import calemiutils.util.helper.ItemHelper;
import calemiutils.util.helper.ShiftHelper;
import calemiutils.util.helper.StringHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiWallet extends GuiContainerBase {

    public GuiWallet(EntityPlayer player) {

        super(new ContainerWallet(player), player);
    }

    @Override
    public String getGuiTextureName() {

        return "wallet";
    }

    @Override
    public String getGuiTitle() {

        return "Wallet";
    }

    @Override
    public void initGui() {

        super.initGui();

        new GuiButtonRect(0, getScreenX() + 146, getScreenY() + 15, 16, "+", buttonList);
        new GuiButtonRect(1, getScreenX() + 146, getScreenY() + 15 + (18), 16, "+", buttonList);
        new GuiButtonRect(2, getScreenX() + 146, getScreenY() + 15 + (2 * 18), 16, "+", buttonList);
        new GuiButtonRect(3, getScreenX() + 146, getScreenY() + 15 + (3 * 18), 16, "+", buttonList);
    }

    @Override
    protected void actionPerformed(GuiButton button) {

        int price = 1;
        if (button.id == 1) price = 5;
        if (button.id == 2) price = 25;
        if (button.id == 3) price = 100;

        int multiplier = ShiftHelper.getShiftCtrlInt(1, 16, 64, 9 * 64);

        price *= multiplier;

        if (player.getHeldItemMainhand().getItem() instanceof ItemWallet) {

            ItemWallet item = (ItemWallet) player.getHeldItemMainhand().getItem();

            if (item.getBalance(player.getHeldItemMainhand()) >= price) {

                CalemiUtils.network.sendToServer(new ServerPacketHandler("wallet-withdraw%" + button.id + "%" + multiplier));

                NBTTagCompound nbt = ItemHelper.getNBT(player.getHeldItemMainhand());

                nbt.setInteger("balance", nbt.getInteger("balance") - price);
            }
        }
    }

    @Override
    public void drawGuiBackground(int mouseX, int mouseY) {

        GuiHelper.drawItemStack(itemRender, new ItemStack(InitItems.COIN_PENNY), getScreenX() + 127, getScreenY() + 15);
        GuiHelper.drawItemStack(itemRender, new ItemStack(InitItems.COIN_NICKEL), getScreenX() + 127, getScreenY() + 33);
        GuiHelper.drawItemStack(itemRender, new ItemStack(InitItems.COIN_QUARTER), getScreenX() + 127, getScreenY() + 51);
        GuiHelper.drawItemStack(itemRender, new ItemStack(InitItems.COIN_DOLLAR), getScreenX() + 127, getScreenY() + 69);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glColor4f(1, 1, 1, 1);

        player.getHeldItemMainhand();
        if (player.getHeldItemMainhand().getItem() instanceof ItemWallet) {
            GuiHelper.drawCenteredString(StringHelper.printCurrency(ItemHelper.getNBT(player.getHeldItemMainhand()).getInteger("balance")), getScreenX() + getGuiSizeX() / 2 - 16, getScreenY() + 46, TEXT_COLOR);
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
