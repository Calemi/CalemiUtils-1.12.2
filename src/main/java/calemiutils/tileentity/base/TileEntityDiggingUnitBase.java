package calemiutils.tileentity.base;

import calemiutils.util.Location;
import calemiutils.util.helper.MathHelper;
import calemiutils.util.helper.WorldEditHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

public abstract class TileEntityDiggingUnitBase extends TileEntityUpgradable implements ICurrencyNetworkReciever {

    private int storedCurrency = 0;

    public List<Location> locationsToMine = new ArrayList<>();
    private Location currentLocationToMine = null;

    private boolean shouldCheckForLocations;

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
    public void update() {

        if (enable) {

            if (shouldCheckForLocations) {
                locationsToMine = getLocationsToMine();
                shouldCheckForLocations = false;
            }

            if (!locationsToMine.isEmpty()) {

                if (currentLocationToMine == null) {
                    findCurrentLocationToMine();
                }

                else {

                    ItemStack stack = new ItemStack(currentLocationToMine.getBlock(), 1, currentLocationToMine.getBlockMeta());

                    if (canFitStackIntoInv(stack) && storedCurrency >= getCurrentOreCost()) {

                        tickProgress();

                        if (isDoneAndReset()) {

                            storedCurrency -= getCurrentOreCost();
                            if (!world.isRemote) insertMinedStackIntoInv(stack);
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

    private boolean canFitStackIntoInv(ItemStack stack) {

        for (int i = 2; i < getSizeInventory(); i++) {

            if (getStackInSlot(i).isEmpty() || (ItemStack.areItemsEqual(getStackInSlot(i), stack)) && getStackInSlot(i).getCount() < getInventoryStackLimit()) {
                return true;
            }
        }

        return false;
    }

    private void insertMinedStackIntoInv(ItemStack stack) {

        for (int i = 2; i < getSizeInventory(); i++) {

            if (ItemStack.areItemsEqual(getStackInSlot(i), stack) && getStackInSlot(i).getCount() < getInventoryStackLimit()) {
                decrStackSize(i, -1);
                return;
            }

            else if (getStackInSlot(i).isEmpty()) {
                setInventorySlotContents(i, stack);
                return;
            }
        }
    }

    public ItemStack getCurrentLocationStack() {

        if (currentLocationToMine != null) {
            return new ItemStack(currentLocationToMine.getBlock(), 1, currentLocationToMine.getBlockMeta());
        }

        return null;
    }

    private void findCurrentLocationToMine() {

        for (Location location : locationsToMine) {

            if (!location.isAirBlock() && location.getBlockState() != getBlockToReplace().getDefaultState()) {
                currentLocationToMine = location;
                return;
            }
        }

        currentLocationToMine = null;
    }

    protected ArrayList<Location> getLocationsWithinRange() {

        return WorldEditHelper.selectWallsFromRadius(getLocation(), currentRange, -getLocation().y + 1, 0);
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
