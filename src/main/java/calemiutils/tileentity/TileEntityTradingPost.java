package calemiutils.tileentity;

import calemiutils.gui.GuiTradingPost;
import calemiutils.inventory.ContainerTradingPost;
import calemiutils.security.ISecurity;
import calemiutils.security.SecurityProfile;
import calemiutils.tileentity.base.ICurrencyNetworkUnit;
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

public class TileEntityTradingPost extends TileEntityInventoryBase implements ITileEntityGuiHandler, ICurrencyNetworkUnit, ISecurity {

    private final SecurityProfile profile = new SecurityProfile();
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

    @Override
    public Location getBankLocation() {
        return bankLocation;
    }

    @Override
    public void setBankLocation(Location location) {
        bankLocation = location;
    }

    public TileEntityBank getBank() {

        TileEntityBank bank = NetworkHelper.getConnectedBank(getLocation(), bankLocation);

        if (bank == null) bankLocation = null;

        return bank;
    }

    public int getStoredCurrencyInBank() {

        if (getBank() != null) {
            return getBank().getStoredCurrency();
        }

        return 0;
    }

    public void addStoredCurrencyInBank(int amount) {

        if (getBank() != null) {

           getBank().setCurrency(getBank().getStoredCurrency() + amount);
        }
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

        hasValidTradeOffer = getStackForSale() != null && !getStackForSale().isEmpty() && amountForSale >= 1;
    }

    public int getStock() {

        if (getStackForSale() != null) {

            int count = 0;

            for (int i = 0; i < getSizeInventory(); i++) {

                if (getStackInSlot(i) != null && getStackInSlot(i).isItemEqual(getStackForSale())) {

                    if (getStackForSale().hasTagCompound()) {

                        if (getStackInSlot(i).hasTagCompound() && getStackInSlot(i).getTagCompound().equals(getStackForSale().getTagCompound())) {
                            count += getStackInSlot(i).getCount();
                        }
                    }

                    else count += getStackInSlot(i).getCount();
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

        stackForSale = NBTHelper.loadItem(nbt, 0);

        adminMode = nbt.getBoolean("adminMode");
        buyMode = nbt.getBoolean("buyMode");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

        super.writeToNBT(nbt);

        nbt.setInteger("amount", amountForSale);
        nbt.setInteger("price", salePrice);

        NBTHelper.saveItem(nbt, stackForSale, 0);

        nbt.setBoolean("adminMode", adminMode);
        nbt.setBoolean("buyMode", buyMode);

        return nbt;
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
    public ITextComponent getDisplayName() {

        if (hasValidTradeOffer) {
            return new TextComponentString((buyMode ? "Buying " : "Selling ") + amountForSale + "x " + getStackForSale().getDisplayName() + " for " + StringHelper.printCurrency(salePrice));
        }

        return null;
    }
}
