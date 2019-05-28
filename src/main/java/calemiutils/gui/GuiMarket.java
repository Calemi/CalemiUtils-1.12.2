package calemiutils.gui;

import calemiutils.CUReference;
import calemiutils.CalemiUtils;
import calemiutils.config.CUConfig;
import calemiutils.config.MarketItemsFile;
import calemiutils.gui.base.GuiButtonRect;
import calemiutils.gui.base.GuiMarketButton;
import calemiutils.gui.base.GuiMarketTab;
import calemiutils.gui.base.GuiScreenBase;
import calemiutils.init.InitBlocks;
import calemiutils.item.ItemWallet;
import calemiutils.packet.ServerPacketHandler;
import calemiutils.tileentity.TileEntityMarket;
import calemiutils.util.UnitChatMessage;
import calemiutils.util.helper.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiMarket extends GuiScreenBase {

    private final TileEntityMarket teMarket;

    private int id = 0;
    private GuiMarketTab buyTab, sellTab;
    private GuiMarketTab activeTab;

    private GuiButtonRect buyButton;
    private GuiButtonRect automateButton;

    public GuiMarket(EntityPlayer player, TileEntityMarket te) {

        super(player);
        this.teMarket = te;
    }

    private UnitChatMessage getUnitChatMessage() {

        return new UnitChatMessage("Market", player);
    }

    private ItemStack getCurrentWalletStack() {
        return CurrencyHelper.getCurrentWalletStack(player);
    }

    private int getCurrency() {

        if (!getCurrentWalletStack().isEmpty()) {

            int balance = ItemWallet.getBalance(getCurrentWalletStack());

            if (balance > 0) {
                return balance;
            }
        }

        else if (teMarket.getBank() != null) {

            return teMarket.getBank().storedCurrency;
        }

        return 0;
    }

    private int getMaxCurrency() {

        if (!getCurrentWalletStack().isEmpty()) {

            return CUConfig.wallet.walletCurrencyCapacity;
        }

        else if (teMarket.getBank() != null) {

            return CUConfig.misc.bankCurrencyCapacity;
        }

        return 0;
    }

    @Override
    public void initGui() {

        super.initGui();

        buyTab = new GuiMarketTab(buttonList, itemRender, "Buy Items", getScreenX() - 30, getScreenY() - 53, getScreenX() - 80);
        sellTab = new GuiMarketTab(buttonList, itemRender, "Sell Items", getScreenX() + 30, getScreenY() - 53, getScreenX() - 80);

        for (int i = 0; i < teMarket.marketItemList.size(); i++) {

            MarketItemsFile.MarketItem marketItem = teMarket.marketItemList.get(i);

            if (marketItem.isBuy) buyTab.addButton(i, marketItem);
            else sellTab.addButton(i, marketItem);
        }

        setBuyMode(teMarket.buyMode);
        setCurrentOffer(teMarket.selectedOffer);

        buyButton = new GuiButtonRect(teMarket.marketItemList.size(), getScreenX(), getScreenY(), 55, "", buttonList);
        automateButton = new GuiButtonRect(teMarket.marketItemList.size() + 1, getScreenX() - (75 / 2), getScreenY() + 50, 75, "", buttonList);
    }

    @Override
    public void drawGuiBackground(int mouseX, int mouseY) {

        GL11.glPushMatrix();

        GlStateManager.enableBlend();

        Minecraft.getMinecraft().getTextureManager().bindTexture(CUReference.GUI_TEXTURES);
        GL11.glTranslatef(0, -0.25F, 0);
        GuiHelper.drawRect(0, getScreenY() - 76, 0, 1, 50, width, 18);

        GL11.glTranslatef(0, 0, 100);
        GuiHelper.drawCenteredString("Market", getScreenX(), getScreenY() - 71, 0xFFFFFF);

        GL11.glPopMatrix();

        int xOffset = 40;
        int yOffset = getScreenY() - 38;

        if (teMarket.getSelectedOffer() != null) {

            int x = (int) ((width / (teMarket.automationMode ? 2.3F : 4)) - xOffset);

            String name = teMarket.getSelectedOffer().amount + "x " + teMarket.getSelectedOffer().getStack().getDisplayName();
            int nameWidth = mc.fontRenderer.getStringWidth(name) - 1;

            GuiHelper.drawItemStack(itemRender, teMarket.getSelectedOffer().getStack(), x - 8, yOffset);
            GuiHelper.drawCenteredString(name, x, yOffset + 18, 0xFFFFFF);

            Minecraft.getMinecraft().getTextureManager().bindTexture(CUReference.GUI_TEXTURES);
            GuiHelper.drawRect(x - nameWidth / 2 - (nameWidth % 2 == 1 ? 1 : 0), yOffset + 27, 0, 0, 100, nameWidth, 1);

            GuiHelper.drawCenteredString((teMarket.buyMode ? "Cost " : "Sell ") + StringHelper.printCurrency(teMarket.getSelectedOffer().value), x, yOffset + 32, 0xFFFFFF);

            buyButton.visible = true;
            buyButton.x = x - (buyButton.width / 2);
            buyButton.y = yOffset + 45;
            buyButton.rect.x = x - (buyButton.width / 2);
            buyButton.rect.y = yOffset + 45;
            buyButton.displayString = (teMarket.buyMode ? "Purchase" : "Sell");
        }

        else {
            buyButton.visible = false;
        }

        if (!getCurrentWalletStack().isEmpty()) {

            int x = (int) (((width / 4) * (teMarket.automationMode ? 2.3F : 3)) + xOffset);

            GuiHelper.drawItemStack(itemRender, getCurrentWalletStack(), x - 8, yOffset);
            GuiHelper.drawCenteredString("Balance " + StringHelper.printCurrency(ItemWallet.getBalance(getCurrentWalletStack())), x, yOffset + 18, 0xFFFFFF);

            yOffset += 35;
        }

        if (teMarket.getBank() != null) {

            int x = (int) (((width / 4) * (teMarket.automationMode ? 2.3F : 3)) + xOffset);

            GuiHelper.drawItemStack(itemRender, new ItemStack(InitBlocks.BANK), x - 8, yOffset);
            GuiHelper.drawCenteredString("Balance " + StringHelper.printCurrency(teMarket.getBank().storedCurrency), x, yOffset + 18, 0xFFFFFF);
        }

        automateButton.displayString = "Automate: " + (teMarket.automationMode ? "ON" : "OFF");

        if (!teMarket.automationMode) {

            buyTab.renderTab();
            sellTab.renderTab();

            if (activeTab != null) {
                activeTab.renderSelectedTab();
                activeTab.renderButtons();
            }

            for (GuiMarketButton button : activeTab.buttons) {
                button.enabled = true;
            }

            buyButton.visible = true;
        }

        else {
            for (GuiMarketButton button : activeTab.buttons) {
                button.enabled = false;
            }

            buyButton.visible = false;
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {

        super.actionPerformed(button);

        if (button.id == buyButton.id){
            if (!teMarket.automationMode) handleTrade();
        }

        else if (button.id == automateButton.id) {

            teMarket.automationMode = !teMarket.automationMode;
            CalemiUtils.network.sendToServer(new ServerPacketHandler("market-setautomode%" + PacketHelper.sendLocation(teMarket.getLocation()) + teMarket.automationMode));
        }

        else {
            if (!teMarket.automationMode) setCurrentOffer(button.id);
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int i) throws IOException {

        super.mouseClicked(x, y, i);

        if (!teMarket.automationMode) {

            if (buyTab.rect.contains(x, y)) {
                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
                setBuyMode(true);
                if (buyTab.buttons.size() > 0) setCurrentOffer(buyTab.buttons.get(0).id);
            }

            if (sellTab.rect.contains(x, y)) {
                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
                setBuyMode(false);
                if (sellTab.buttons.size() > 0) setCurrentOffer(sellTab.buttons.get(0).id);
            }
        }
    }

    private void setBuyMode(boolean value) {

        sellTab.enableButtons(false);
        buyTab.enableButtons(false);

        teMarket.buyMode = value;
        CalemiUtils.network.sendToServer(new ServerPacketHandler("market-setbuymode%" + PacketHelper.sendLocation(teMarket.getLocation()) + value));

        if (teMarket.buyMode) {
            activeTab = buyTab;
        }

        else {
            activeTab = sellTab;
        }

        activeTab.enableButtons(true);
    }

    private void setCurrentOffer(int id) {
        teMarket.selectedOffer = id;
        CalemiUtils.network.sendToServer(new ServerPacketHandler("market-setoffer%" + PacketHelper.sendLocation(teMarket.getLocation()) + id));
    }

    private void handleTrade() {

        if (teMarket.getSelectedOffer() != null) {

            MarketItemsFile.MarketItem marketItem = teMarket.getSelectedOffer();
            ItemStack stack = marketItem.getStack();

            ItemStack walletStack = CurrencyHelper.getCurrentWalletStack(player);

            if (!walletStack.isEmpty() || teMarket.getBank() != null) {

                int currency = getCurrency();

                if (marketItem.isBuy) {

                    if (currency >= marketItem.value) {

                        ItemHelper.getNBT(walletStack).setInteger("balance", ItemHelper.getNBT(walletStack).getInteger("balance") - marketItem.value);
                        CalemiUtils.network.sendToServer(new ServerPacketHandler("market-buy%" + marketItem.stackObj + "%" + marketItem.meta + "%" + marketItem.amount + "%" + marketItem.value + "%" + (teMarket.getBank() != null ? PacketHelper.sendLocation(teMarket.getBank().getLocation()) : "")));
                    }

                    else getUnitChatMessage().printMessage(TextFormatting.RED, "You don't have enough money!");
                }

                else {

                    if (currency + marketItem.value <= getMaxCurrency()) {

                        if (InventoryHelper.countItems(player.inventory, false, stack) >= marketItem.amount) {

                            InventoryHelper.consumeItem(player.inventory, marketItem.amount, false, stack);
                            ItemHelper.getNBT(walletStack).setInteger("balance", ItemHelper.getNBT(walletStack).getInteger("balance") + marketItem.value);

                            CalemiUtils.network.sendToServer(new ServerPacketHandler("market-sell%" + marketItem.stackObj + "%" + marketItem.meta + "%" + marketItem.amount + "%" + marketItem.value + "%" + (teMarket.getBank() != null ? PacketHelper.sendLocation(teMarket.getBank().getLocation()) : "")));
                        }

                        else getUnitChatMessage().printMessage(TextFormatting.RED, "You don't have the required items and amount");
                    }

                    else getUnitChatMessage().printMessage(TextFormatting.RED, "You don't have space for the income!");
                }
            }

            else getUnitChatMessage().printMessage(TextFormatting.RED, "You need to have a wallet or a connected bank!");
        }
    }

    @Override
    public void drawGuiForeground(int mouseX, int mouseY) {

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
    public boolean canCloseWithInvKey() {
        return true;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
