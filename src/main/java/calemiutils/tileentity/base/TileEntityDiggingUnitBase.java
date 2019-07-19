package calemiutils.tileentity.base;

import calemiutils.tileentity.TileEntityBank;
import calemiutils.util.Location;
import calemiutils.util.helper.InventoryHelper;
import calemiutils.util.helper.MathHelper;
import calemiutils.util.helper.NetworkHelper;
import calemiutils.util.helper.WorldEditHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

public abstract class TileEntityDiggingUnitBase extends TileEntityUpgradable implements ICurrencyNetworkUnit {

    public List<Location> locationsToMine = new ArrayList<>();
    private Location currentLocationToMine = null;

    private boolean shouldCheckForLocations;

    private Location bankLocation;

    protected TileEntityDiggingUnitBase() {

        setInputSlots(MathHelper.getCountingArray(0, 28));
        setSideInputSlots(MathHelper.getCountingArray(0, 28));
        setExtractSlots(MathHelper.getCountingArray(2, 28));
        enable = false;
        shouldCheckForLocations = true;
    }

    protected abstract List<Location> getLocationsToMine();

    public abstract int getCurrentOreCost();

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

    private int getStoredCurrencyInBank() {

        if (getBank() != null) {
            return getBank().getStoredCurrency();
        }

        return 0;
    }

    private void decrStoredCurrencyInBank(int amount) {

        if (getBank() != null) {
            getBank().addCurrency(-amount);
        }
    }

    @Override
    public void update() {

        if (enable) {

            if (shouldCheckForLocations) {
                locationsToMine = getLocationsToMine();
                shouldCheckForLocations = false;
            }

            if (getBank() != null) {

                if (!locationsToMine.isEmpty()) {

                    if (currentLocationToMine == null) {
                        findCurrentLocationToMine();
                    }

                    else {

                        ItemStack stack = new ItemStack(currentLocationToMine.getBlock(), 1, currentLocationToMine.getBlockMeta());

                        if (InventoryHelper.canInsertItem(stack, this) && getStoredCurrencyInBank() >= getCurrentOreCost()) {

                            tickProgress();

                            if (isDoneAndReset()) {

                                decrStoredCurrencyInBank(getCurrentOreCost());
                                if (!world.isRemote) InventoryHelper.insertItem(stack, this, 2);
                                currentLocationToMine.setBlock(getBlockToReplace());
                                locationsToMine.remove(currentLocationToMine);
                                currentLocationToMine = null;
                            }
                        }

                        else {
                            currentProgress = 0;
                        }
                    }
                }

                else if (currentRange < getScaledRange()) {
                    currentRange++;
                    shouldCheckForLocations = true;
                }
            }
        }

        else {
            currentLocationToMine = null;
            currentProgress = 0;
            currentRange = 0;
            shouldCheckForLocations = true;
        }
    }

    private Block getBlockToReplace() {

        int dim = world.provider.getDimensionType().getId();

        if (dim == -1) return Blocks.NETHERRACK;
        if (dim == 1) return Blocks.END_STONE;

        return Blocks.STONE;
    }

    public ItemStack getCurrentLocationStack() {

        if (currentLocationToMine != null) {
            return new ItemStack(currentLocationToMine.getBlock(), 1, currentLocationToMine.getBlockMeta());
        }

        return null;
    }

    private void findCurrentLocationToMine() {

        if (locationsToMine.size() > 0) {

            Location location = locationsToMine.get(0);

            if (!location.isAirBlock() && location.getBlockState() != getBlockToReplace().getDefaultState()) {
                currentLocationToMine = location;
            }

            else {
                locationsToMine.remove(location);
                findCurrentLocationToMine();
            }
        }

        else currentLocationToMine = null;
    }

    protected ArrayList<Location> getLocationsWithinRange() {

        return WorldEditHelper.selectWallsFromRadius(getLocation(), currentRange, -getLocation().y + 1, 0);
    }

    @Override
    public EnumFacing[] getConnectedDirections() {

        return EnumFacing.VALUES;
    }

    @Override
    public int getMaxProgress() {

        return 100;
    }

    @Override
    public int getSizeInventory() {

        return 29;
    }

    @Override
    public int getSpeedSlot() {

        return 0;
    }

    @Override
    public int getRangeSlot() {

        return 1;
    }

    @Override
    public int getScaledRangeMin() {

        return 10;
    }
}
