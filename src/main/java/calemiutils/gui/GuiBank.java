package calemiutils.gui;

import calemiutils.CalemiUtils;
import calemiutils.config.CUConfig;
import calemiutils.gui.base.GuiButtonRect;
import calemiutils.gui.base.GuiContainerBase;
import calemiutils.item.ItemWallet;
import calemiutils.packet.ServerPacketHandler;
import calemiutils.tileentity.TileEntityBank;
import calemiutils.util.helper.ItemHelper;
import calemiutils.util.helper.MathHelper;
import calemiutils.util.helper.PacketHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiBank extends GuiContainerBase {

    private final TileEntityBank teBank;

    private GuiButtonRect withdraw, deposit;

    public GuiBank(EntityPlayer player, TileEntityBank tileEntity) {

        super(tileEntity.getTileContainer(player), player, tileEntity);
        this.teBank = tileEntity;
    }

    @Override
    public String getGuiTextureName() {

        return "bank";
    }

    @Override
    public String getGuiTitle() {

        return "Bank";
    }

    @Override
    public int getGuiSizeY() {

        return 144;
    }

    @Override
    public void initGui() {

        super.initGui();

        withdraw = new GuiButtonRect(0, getScreenX() + (getGuiSizeX() / 2 - 25) + 30, getScreenY() + 40, 50, "Withdraw", buttonList);
        deposit = new GuiButtonRect(1, getScreenX() + (getGuiSizeX() / 2 - 25) - 30, getScreenY() + 40, 50, "Deposit", buttonList);
    }

    @Override
    protected void actionPerformed(GuiButton button) {

        if (teBank.getStackInSlot(1) != null && teBank.getStackInSlot(1).getItem() instanceof ItemWallet) {

            ItemWallet wallet = (ItemWallet) teBank.getStackInSlot(1).getItem();

            int currency = wallet.getBalance(teBank.getStackInSlot(1));
            NBTTagCompound nbt = ItemHelper.getNBT(teBank.getStackInSlot(1));

            if (button.id == deposit.id) {

                int amountToAdd = MathHelper.getAmountToAdd(teBank.storedCurrency, currency, teBank.getMaxCurrency());

                if (amountToAdd > 0) {
                    teBank.addCurrency(amountToAdd);
                    nbt.setInteger("balance", currency - amountToAdd);
                }

                else {

                    int remainder = MathHelper.getRemainder(teBank.storedCurrency, currency, teBank.getMaxCurrency());

                    if (remainder > 0) {
                        teBank.addCurrency(remainder);
                        nbt.setInteger("balance", currency - remainder);
                    }
                }
            }

            if (button.id == withdraw.id) {

                int amountToAdd = MathHelper.getAmountToAdd(currency, teBank.storedCurrency, CUConfig.misc.walletCurrencyCapacity);

                if (amountToAdd > 0) {
                    teBank.addCurrency(-amountToAdd);
                    nbt.setInteger("balance", currency + amountToAdd);
                }

                else {

                    int remainder = MathHelper.getRemainder(currency, teBank.storedCurrency, CUConfig.misc.walletCurrencyCapacity);

                    if (remainder > 0) {
                        teBank.addCurrency(-remainder);
                        nbt.setInteger("balance", currency + remainder);
                    }
                }
            }

            CalemiUtils.network.sendToServer(new ServerPacketHandler("bank-sync" + "%" + teBank.storedCurrency + "%" + wallet.getBalance(teBank.getStackInSlot(1)) + "%" + PacketHelper.sendLocation(teBank.getLocation())));
        }
    }

    @Override
    public void drawGuiBackground(int mouseX, int mouseY) {

    }

    @Override
    public void drawGuiForeground(int mouseX, int mouseY) {

    }
}
