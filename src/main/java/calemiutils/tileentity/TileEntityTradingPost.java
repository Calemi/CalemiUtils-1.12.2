package calemiutils.tileentity;

import calemiutils.config.CUConfig;
import calemiutils.gui.GuiTradingPost;
import calemiutils.inventory.ContainerTradingPost;
import calemiutils.security.ISecurity;
import calemiutils.security.SecurityProfile;
import calemiutils.tileentity.base.ICurrencyNetworkProducer;
import calemiutils.tileentity.base.ITileEntityGuiHandler;
import calemiutils.tileentity.base.TileEntityInventoryBase;
import calemiutils.util.Location;
import calemiutils.util.UnitChatMessage;
import calemiutils.util.helper.MathHelper;
import calemiutils.util.helper.NBTHelper;
import calemiutils.util.helper.NetworkHelper;
import calemiutils.util.helper.StringHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityTradingPost extends TileEntityInventoryBase implements ITileEntityGuiHandler, ICurrencyNetworkProducer, ISecurity {

    private final SecurityProfile profile = new SecurityProfile();
    public int storedCurrency = 0;
    private Location bankLocation;

    public int amountForSale;
    public int salePrice;
    public boolean hasValidTradeOffer;
    private ItemStack stackForSale = ItemStack.EMPTY;

    public boolean adminMode = false;
    public boolean buyMode = false;

    public TileEntityTradingPost() {

        setInputSlots(MathHelper.getCountingArray(0, 26));
        setSideInputSlots(MathHelper.getCountingArray(0, 26));
        amountForSale = 1;
        salePrice = 0;
        hasValidTradeOffer = false;
    }

    private TileEntityBank getBank() {

        if (bankLocation != null && bankLocation.getTileEntity() instanceof TileEntityBank) {
            return (TileEntityBank) bankLocation.getTileEntity();
        }

        return null;
    }

    public int getStoredCurrencyInBank() {

        if (getBank() != null) {
            return getBank().getStoredCurrency();
        }

        return 0;
    }

    public void decrStoredCurrencyInBank(int amount) {

        if (getBank() != null) {
            getBank().addCurrency(-amount);
        }
    }

    public UnitChatMessage getUnitName(EntityPlayer player) {

        if (adminMode) {
            return new UnitChatMessage("Admin Post", player);
        }

        return new UnitChatMessage(getSecurityProfile().getOwnerName() + "'s Trading Post", player);
    }

    @Override
    public void update() {

        if (getBank() == null || getBank().storedCurrency == 0) {
            bankLocation = NetworkHelper.getConnectedBank(this);
        }

        hasValidTradeOffer = getStackForSale() != null && !getStackForSale().isEmpty() && amountForSale >= 1;
    }

    public int getStock() {

        if (getStackForSale() != null) {

            int count = 0;

            for (int i = 0; i < getSizeInventory(); i++) {

                if (getStackInSlot(i) != null && getStackInSlot(i).isItemEqual(getStackForSale())) {
                    count += getStackInSlot(i).getCount();
                }
            }

            return count;
        }

        return 0;
    }

    public ItemStack getStackForSale() {

        return stackForSale;
    }

    public void setStackForSale(ItemStack stack) {

        stackForSale = stack;
    }

    @Override
    public Container getTileContainer(EntityPlayer player) {

        return new ContainerTradingPost(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiContainer getTileGuiContainer(EntityPlayer player) {

        return new GuiTradingPost(player, this);
    }

    @Override
    public int getSizeInventory() {

        return 27;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {

        super.readFromNBT(nbt);

        amountForSale = nbt.getInteger("amount");
        salePrice = nbt.getInteger("price");

        stackForSale = NBTHelper.loadItem(nbt);

        adminMode = nbt.getBoolean("adminMode");
        buyMode = nbt.getBoolean("buyMode");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

        super.writeToNBT(nbt);

        nbt.setInteger("amount", amountForSale);
        nbt.setInteger("price", salePrice);

        NBTHelper.saveItem(nbt, stackForSale);

        nbt.setBoolean("adminMode", adminMode);
        nbt.setBoolean("buyMode", buyMode);

        return nbt;
    }

    @Override
    public int getStoredCurrency() {

        return storedCurrency;
    }

    @Override
    public void setCurrency(int amount) {

        int setAmount = amount;

        if (amount > getMaxCurrency()) {
            setAmount = getMaxCurrency();
        }

        storedCurrency = setAmount;

    }

    @Override
    public int extractAllCurrency() {

        int i = storedCurrency;
        storedCurrency = 0;
        return i;
    }

    @Override
    public SecurityProfile getSecurityProfile() {

        return profile;
    }

    @Override
    public EnumFacing[] getConnectedDirections() {

        return new EnumFacing[]{EnumFacing.DOWN};
    }

    @Override
    public int getMaxCurrency() {

        return CUConfig.misc.postCurrencyCapacity;
    }

    @Override
    public ITextComponent getDisplayName() {

        if (hasValidTradeOffer) {
            return new TextComponentString((buyMode ? "Buying " : "Selling ") + amountForSale + "x " + getStackForSale().getDisplayName() + " for " + StringHelper.printCurrency(salePrice));
        }

        return null;
    }
}
