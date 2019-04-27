package calemiutils.tileentity.base;

import calemiutils.util.helper.MathHelper;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileEntityUpgradable extends TileEntityInventoryBase implements IProgress, IRange {

    protected int currentProgress;
    public int currentRange;

    @SuppressWarnings("SameReturnValue")
    protected abstract int getSpeedSlot();

    @SuppressWarnings("SameReturnValue")
    protected abstract int getRangeSlot();

    @SuppressWarnings("SameReturnValue")
    protected abstract int getScaledSpeedMin();

    @SuppressWarnings("SameReturnValue")
    protected abstract int getScaledSpeedMax();

    @SuppressWarnings("SameReturnValue")
    protected abstract int getScaledRangeMin();

    protected abstract int getScaledRangeMax();

    private int getScaledSpeed() {

        return getScaledSlot(getSpeedSlot(), getScaledSpeedMin(), getScaledSpeedMax());
    }

    public int getScaledRange() {

        return getScaledSlot(getRangeSlot(), getScaledRangeMin(), getScaledRangeMax());
    }

    private int getScaledSlot(int slot, int min, int max) {

        int difference = max - min;

        return min + MathHelper.scaleInt(getStackInSlot(slot).getCount(), 5, difference);
    }

    protected void tickProgress() {

        currentProgress += getScaledSpeed();
    }

    protected boolean isDoneAndReset() {

        if (currentProgress >= getMaxProgress()) {
            currentProgress = 0;
            return true;
        }

        return false;
    }

    @Override
    public int getCurrentProgress() {

        return currentProgress;
    }

    @Override
    public int getCurrentRange() {

        return currentRange;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {

        super.readFromNBT(nbt);

        currentProgress = nbt.getInteger("currentProgress");
        currentRange = nbt.getInteger("currentRange");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

        super.writeToNBT(nbt);

        nbt.setInteger("currentProgress", currentProgress);
        nbt.setInteger("currentRange", currentRange);

        return nbt;
    }
}
