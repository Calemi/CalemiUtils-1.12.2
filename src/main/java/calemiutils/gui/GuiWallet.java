package calemiutils.gui;

import calemiutils.CalemiUtils;
import calemiutils.config.CUConfig;
import calemiutils.gui.base.GuiButtonRect;
import calemiutils.gui.base.GuiContainerBase;
import calemiutils.init.InitItems;
import calemiutils.inventory.ContainerWallet;
import calemiutils.item.ItemWallet;
import calemiutils.packet.WalletPacket;
import calemiutils.util.helper.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiWallet extends GuiContainerBase {

    private GuiButtonRect toggleSuckButton;
    private GuiButtonRect setActiveButton;

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

        toggleSuckButton = new GuiButtonRect(4, getScreenX() + (getGuiSizeX() / 2) - (54 / 2) - 54, getScreenY() + 18 + (3 * 18), 54, "", buttonList);
        setActiveButton = new GuiButtonRect(5, getScreenX() + (getGuiSizeX() / 2) - (48 / 2), getScreenY() + 18 + (3 * 18), 48, "Activate", buttonList);
    }

    private ItemStack getCurrentWalletStack() {

        ItemStack walletStack = CurrencyHelper.getCurrentWalletStack(player, false);

        if (!walletStack.isEmpty()) {
            return walletStack;
        }

        else {
            Minecraft.getMinecraft().player.closeScreen();
            return ItemStack.EMPTY;
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {

        int price = InitItems.COIN_PENNY.value;
        if (button.id == 1) price = InitItems.COIN_NICKEL.value;
        if (button.id == 2) price = InitItems.COIN_QUARTER.value;
        if (button.id == 3) price = InitItems.COIN_DOLLAR.value;

        int multiplier = ShiftHelper.getShiftCtrlInt(1, 16, 64, 9 * 64);

        price *= multiplier;

        ItemStack walletStack = getCurrentWalletStack();

        if (!walletStack.isEmpty()) {

            ItemWallet walletItem = (ItemWallet) walletStack.getItem();

            if (button.id < 4 && ItemWallet.getBalance(walletStack) >= price) {

                if (ItemHelper.getNBT(walletStack).getBoolean("suck")) {
                    walletItem.toggleSuck(walletStack);
                    CalemiUtils.network.sendToServer(new WalletPacket("togglesuck"));
                }

                CalemiUtils.network.sendToServer(new WalletPacket("withdraw%" + button.id + "%" + multiplier));

                NBTTagCompound nbt = ItemHelper.getNBT(walletStack);

                nbt.setInteger("balance", nbt.getInteger("balance") - price);
            }

            if (button.id == toggleSuckButton.id) {
                CalemiUtils.network.sendToServer(new WalletPacket("togglesuck"));
                walletItem.toggleSuck(walletStack);
            }

            if (button.id == setActiveButton.id) {

                CalemiUtils.network.sendToServer(new WalletPacket("activate"));

                List<ItemStack> list = CurrencyHelper.checkForActiveWallets(player);

                for (ItemStack stack : list) {

                    ItemWallet.activate(stack, false);
                }

                ItemWallet.activate(walletStack, true);
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

        ItemStack stack = getCurrentWalletStack();

        if (!stack.isEmpty()) {

            toggleSuckButton.displayString = "Suck: " + (ItemHelper.getNBT(stack).getBoolean("suck") ? "ON" : "OFF");

            GuiHelper.drawCenteredString(StringHelper.printCommas(ItemHelper.getNBT(stack).getInteger("balance")), getScreenX() + getGuiSizeX() / 2 - 16, getScreenY() + 42, TEXT_COLOR);
            GuiHelper.drawCenteredString(CUConfig.economy.currencyName, getScreenX() + getGuiSizeX() / 2 - 16, getScreenY() + 51, TEXT_COLOR);
        }
    }

    @Override
    public void drawGuiForeground(int mouseX, int mouseY) {

        GL11.glDisable(GL11.GL_LIGHTING);
        addInfoIcon(0);
        addInfoIconText(mouseX, mouseY, "Button Click Info", "Shift: 16, Ctrl: 64, Shift + Ctrl: 64 * 9");

        if (!ItemWallet.isActive(getCurrentWalletStack())) {

            leftTabOffset += 17;
            addInfoIcon(1);
            addInfoIconText(mouseX, mouseY, "Inactive!", "Press the activate button.", "There can only be one active Wallet.");
        }
    }
}
