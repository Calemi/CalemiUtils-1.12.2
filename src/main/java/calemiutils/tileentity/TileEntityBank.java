package calemiutils.tileentity;

import calemiutils.config.CUConfig;
import calemiutils.gui.GuiBank;
import calemiutils.inventory.ContainerBank;
import calemiutils.item.ItemCurrency;
import calemiutils.security.ISecurity;
import calemiutils.security.SecurityProfile;
import calemiutils.tileentity.base.*;
import calemiutils.util.Location;
import calemiutils.util.VeinScan;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class TileEntityBank extends TileEntityInventoryBase implements ITileEntityGuiHandler, ICurrencyNetworkBank, ISecurity {

    public List<Location> connectedUnits = new ArrayList<>();

    public int storedCurrency = 0;

    private final SecurityProfile profile = new SecurityProfile();
    private VeinScan scan;

    public TileEntityBank() {

        setInputSlots(0, 1);
        setSideInputSlots(0, 1);
        setExtractSlots(0, 1);
    }

    @Override
    public void update() {

        if (getLocation() != null && scan == null) {
            scan = new VeinScan(getLocation());
        }

        if (scan != null) {

            if (world.getWorldTime() % 40 == 0) {

                connectedUnits.clear();

                boolean foundAnotherBank = false;

                scan.reset();
                scan.startNetworkScan(getConnectedDirections());

                for (Location location : scan.buffer) {

                    if (!location.equals(getLocation()) && location.getTileEntity() instanceof TileEntityBank) {
                        foundAnotherBank = true;
                    }

                    if (location.getTileEntity() instanceof ICurrencyNetworkUnit) {

                        ICurrencyNetworkUnit unit = (ICurrencyNetworkUnit) location.getTileEntity();

                        connectedUnits.add(location);

                        if (unit.getBankLocation() == null) {

                            unit.setBankLocation(getLocation());
                        }
                    }
                }

                enable = !foundAnotherBank;
            }
        }

        if (!world.isRemote) {

            if (getStackInSlot(0) != null && getStackInSlot(0).getItem() instanceof ItemCurrency) {

                int amountToAdd = ((ItemCurrency) getStackInSlot(0).getItem()).value;

                int stackSize = 0;

                for (int i = 0; i < getStackInSlot(0).getCount(); i++) {

                    if (canAddAmount(amountToAdd)) {
                        stackSize++;
                        amountToAdd += ((ItemCurrency) getStackInSlot(0).getItem()).value;
                    }
                }

                if (stackSize != 0) {

                    addCurrency(stackSize * ((ItemCurrency) getStackInSlot(0).getItem()).value);
                    decrStackSize(0, stackSize);
                }
            }
        }
    }

    private boolean canAddAmount(int amount) {

        int storedAmount = storedCurrency;
        return storedAmount + amount <= getMaxCurrency();
    }

    public void addCurrency(int amount) {

        markForUpdate();
        setCurrency(storedCurrency + amount);
    }

    @Override
    public Container getTileContainer(EntityPlayer player) {

        return new ContainerBank(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiContainer getTileGuiContainer(EntityPlayer player) {

        return new GuiBank(player, this);
    }

    @Override
    public int getSizeInventory() {

        return 2;
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
    public SecurityProfile getSecurityProfile() {

        return profile;
    }

    @Override
    public EnumFacing[] getConnectedDirections() {

        return EnumFacing.VALUES;
    }

    @Override
    public int getMaxCurrency() {

        return CUConfig.misc.bankCurrencyCapacity;
    }
}
