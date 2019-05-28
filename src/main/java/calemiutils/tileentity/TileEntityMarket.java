package calemiutils.tileentity;

import calemiutils.config.MarketItemsFile;
import calemiutils.security.ISecurity;
import calemiutils.security.SecurityProfile;
import calemiutils.tileentity.base.INetwork;
import calemiutils.tileentity.base.TileEntityBase;
import calemiutils.util.Location;
import calemiutils.util.helper.CurrencyHelper;
import calemiutils.util.helper.InventoryHelper;
import calemiutils.util.helper.NetworkHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TileEntityMarket extends TileEntityBase implements ISecurity, INetwork {

    private final SecurityProfile profile = new SecurityProfile();

    public final List<MarketItemsFile.MarketItem> marketItemList = new ArrayList<>();
    public boolean buyMode = true;
    public int selectedOffer;

    private Location bankLocation;

    public boolean automationMode = false;

    public TileEntityMarket() {}

    public TileEntityMarket(World world) {

        if (!world.isRemote) {
            registerMarketItems();
            marketItemList.sort((o1, o2) -> o1.stackObj.compareToIgnoreCase(o2.stackObj));
        }
    }

    private void registerMarketItems() {
        marketItemList.addAll(MarketItemsFile.registeredBlocks.values());
    }

    public MarketItemsFile.MarketItem getSelectedOffer() {

        if (selectedOffer >= marketItemList.size() || selectedOffer < 0) {
            return null;
        }

        else return marketItemList.get(selectedOffer);
    }

    public TileEntityBank getBank() {

        if (bankLocation != null && bankLocation.getTileEntity() instanceof TileEntityBank) {
            return (TileEntityBank) bankLocation.getTileEntity();
        }

        return null;
    }

    private IInventory getConnectedInventory() {

        for (EnumFacing face : EnumFacing.VALUES) {

            Location location = getLocation().translate(face, 1);

            if (location.getTileEntity() != null && location.getTileEntity() instanceof IInventory) {

                return (IInventory) location.getTileEntity();
            }
        }

        return null;
    }

    @Override
    public void update() {

        MarketItemsFile.MarketItem marketItem = getSelectedOffer();
        TileEntityBank bank = getBank();
        IInventory inv = getConnectedInventory();

        if (world.getWorldTime() % 15 == 0) {

            bankLocation = NetworkHelper.getConnectedBank(this);

            if (automationMode && marketItem != null && bank != null && inv != null) {

                boolean buyMode = marketItem.isBuy;

                if (buyMode) {

                    if (bank.getStoredCurrency() >= marketItem.value) {

                        if (InventoryHelper.canInsertItem(marketItem.getStack(), inv)) {

                            InventoryHelper.insertItem(marketItem.getStack(), inv);
                            bank.addCurrency(-marketItem.value);
                        }
                    }
                }

                else {

                    if (CurrencyHelper.canFitAddedCurrencyToNetwork(bank, marketItem.value)) {

                        if (InventoryHelper.countItems(inv, false, marketItem.getStack()) >= marketItem.amount) {

                            InventoryHelper.consumeItem(inv, marketItem.amount, false, marketItem.getStack());
                            bank.addCurrency(marketItem.value);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound root) {

        buyMode = root.getBoolean("buyMode");
        selectedOffer = root.getInteger("selectedOffer");

        automationMode = root.getBoolean("autoMode");

        marketItemList.clear();

        if (root.hasKey("MarketItems")) {

            NBTTagCompound marketParent = root.getCompoundTag("MarketItems");

            for (int i = 0; i < marketParent.getSize(); i++) {

                NBTTagCompound itemTag = marketParent.getCompoundTag("Item" + i);

                marketItemList.add(MarketItemsFile.MarketItem.readFromNBT(itemTag));
            }
        }

        super.readFromNBT(root);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound root) {

        NBTTagCompound parent = new NBTTagCompound();

        for (int i = 0; i < marketItemList.size(); i++) {

            MarketItemsFile.MarketItem marketItem = marketItemList.get(i);

            NBTTagCompound marketTag = marketItem.writeToNBT();

            //Write Item to parent
            parent.setTag("Item" + i, marketTag);
        }

        //Write parent to root
        root.setTag("MarketItems", parent);

        root.setBoolean("buyMode", buyMode);
        root.setInteger("selectedOffer", selectedOffer);

        root.setBoolean("autoMode", automationMode);

        return super.writeToNBT(root);
    }

    @Override
    public EnumFacing[] getConnectedDirections() {

        return EnumFacing.VALUES;
    }

    @Override
    public SecurityProfile getSecurityProfile() {

        return profile;
    }
}
