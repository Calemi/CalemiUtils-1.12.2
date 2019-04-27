package calemiutils.inventory.base;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

public class ItemStackInventory implements IInventory {

    private final NonNullList<ItemStack> slots;

    public ItemStackInventory(ItemStack stack, int size) {

        slots = NonNullList.withSize(size, ItemStack.EMPTY);

        NBTTagCompound mainTag = stack.getTagCompound();

        if (mainTag != null && mainTag.hasKey("inv")) {

            NBTTagCompound itemListTag = mainTag.getCompoundTag("inv");

            ItemStackHelper.loadAllItems(itemListTag, slots);
        }
    }

    public void dump(ItemStack stack) {

        NBTTagCompound mainTag = stack.getTagCompound();

        if (mainTag == null) {

            mainTag = new NBTTagCompound();
        }

        NBTTagCompound itemListTag = new NBTTagCompound();

        ItemStackHelper.saveAllItems(itemListTag, slots);

        mainTag.setTag("inv", itemListTag);

        stack.setTagCompound(mainTag);
    }

    @Override
    public int getSizeInventory() {

        return slots.size();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {

        return slots.get(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {

        if (this.slots.get(slot) != ItemStack.EMPTY) {

            ItemStack itemstack;

            if (this.slots.get(slot).getCount() <= amount) {

                itemstack = this.slots.get(slot);
                this.slots.set(slot, ItemStack.EMPTY);
                return itemstack;

            }

            else {

                itemstack = this.slots.get(slot).splitStack(amount);

                if (this.slots.get(slot).getCount() == 0) {
                    this.slots.set(slot, ItemStack.EMPTY);
                }

                return itemstack;
            }
        }

        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {

        this.slots.set(slot, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {

            decrStackSize(slot, stack.getCount() - this.getInventoryStackLimit());
        }
    }

    @Override
    public String getName() {

        return null;
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
    public void markDirty() {

    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {

        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {

        return false;
    }

    @Override
    public ITextComponent getDisplayName() {

        return null;
    }

    @Override
    public boolean isEmpty() {

        return false;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {

        return null;
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

    }
}
