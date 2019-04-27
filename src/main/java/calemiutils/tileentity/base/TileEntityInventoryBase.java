package calemiutils.tileentity.base;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;

public abstract class TileEntityInventoryBase extends TileEntityBase implements ISidedInventory, ITileEntityGuiHandler {

    public NonNullList<ItemStack> slots = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
    public final Slot[] containerSlots = new Slot[getSizeInventory()];

    private int[] inputSlots, sideInputSlots, extractSlots;

    protected void setExtractSlots(int... extractSlots) {

        this.extractSlots = extractSlots;
    }

    protected void setInputSlots(int... inputSlots) {

        this.inputSlots = inputSlots;
    }

    protected void setSideInputSlots(int... sideInputSlots) {

        this.sideInputSlots = sideInputSlots;
    }

    @Override
    public ItemStack getStackInSlot(int i) {

        return slots.get(i);
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {

        ItemStack itemstack;

        if (this.slots.get(i).getCount() <= j) {

            itemstack = this.slots.get(i);
            this.slots.set(i, ItemStack.EMPTY);
            return itemstack;
        }

        else {

            itemstack = this.slots.get(i).splitStack(j);

            if (slots.get(i) == ItemStack.EMPTY) {
                this.slots.set(i, ItemStack.EMPTY);
            }

            return itemstack;
        }

    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack) {

        this.slots.set(i, itemstack);

        if (itemstack.getCount() > this.getInventoryStackLimit()) {

            decrStackSize(i, itemstack.getCount() - this.getInventoryStackLimit());
        }
    }

    @Override
    public boolean hasCustomName() {

        return false;
    }

    @Override
    public int getInventoryStackLimit() {

        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {

        return true;
    }

    @Override
    public String getName() {

        return null;
    }

    @Override
    public void openInventory(EntityPlayer player) {

        markForUpdate();
    }

    @Override
    public void closeInventory(EntityPlayer player) {

        markForUpdate();
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {

        return containerSlots[slot] != null && containerSlots[slot].isItemValid(stack);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {

        int[] slots = new int[getSizeInventory()];

        for (int i = 0; i < slots.length; i++) {
            slots[i] = i;
        }

        return slots;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStackIn, EnumFacing direction) {

        int dir = direction.getIndex();

        if (direction == EnumFacing.UP && inputSlots != null) {

            for (int id : inputSlots) {

                if (id == slot) {

                    return true;
                }
            }
        }

        if (dir > 1 && sideInputSlots != null) {

            for (int id : sideInputSlots) {

                if (id == slot) return true;
            }
        }

        return false;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction) {

        if (direction == EnumFacing.DOWN && extractSlots != null) {

            for (int id : extractSlots) {

                if (id == slot) return true;
            }
        }

        return false;
    }

    @Override
    public boolean isEmpty() {

        for (ItemStack stack : slots) {

            if (!stack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {

        ItemStack copy = getStackInSlot(index).copy();
        setInventorySlotContents(index, ItemStack.EMPTY);
        return copy;
    }

    @Override
    public int getField(int id) {

        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {

        return 0;
    }

    @Override
    public void clear() {

        for (int i = 0; i < getSizeInventory(); i++) {
            slots.set(i, ItemStack.EMPTY);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {

        super.readFromNBT(nbt);

        this.slots = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);

        ItemStackHelper.loadAllItems(nbt, slots);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

        super.writeToNBT(nbt);

        ItemStackHelper.saveAllItems(nbt, slots);

        return nbt;
    }
}
