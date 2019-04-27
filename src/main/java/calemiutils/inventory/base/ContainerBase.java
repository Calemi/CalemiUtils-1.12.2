package calemiutils.inventory.base;

import calemiutils.tileentity.base.TileEntityInventoryBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBase extends Container {

    private final EntityPlayer player;

    protected TileEntityInventoryBase tileEntity;
    protected int size;

    protected boolean isItemContainer;

    protected ContainerBase(EntityPlayer player, TileEntityInventoryBase tileEntity) {

        this.player = player;
        this.tileEntity = tileEntity;
    }

    public ContainerBase(EntityPlayer player, TileEntityInventoryBase tileEntity, int x, int y) {

        this(player, tileEntity);
        addPlayerInv(x, y);
        addPlayerHotbar(x, y + 58);
    }

    protected ContainerBase(EntityPlayer player, int size) {

        this.player = player;
        this.size = size;
    }

    private int getTileEntitySlotAmount() {

        return isItemContainer ? size : tileEntity.getSizeInventory();
    }

    protected void addPlayerInv(int x, int y) {

        addStorageInv(player.inventory, 9, x, y, 3);
    }

    protected void addPlayerHotbar(int x, int y) {

        addStorageInv(player.inventory, 0, x, y, 1);
    }

    private void addStorageInv(IInventory inv, int idOffset, int x, int y, int height) {

        for (int i = 0; i < height; i++) {

            for (int j = 0; j < 9; j++) {

                this.addSlotToContainer(new Slot(inv, j + i * 9 + idOffset, x + (j * 18), y + (i * 18)));
            }
        }
    }

    protected void addTileEntityStorageInv(IInventory inv, int idOffset, int x, int y, int height) {

        int id = idOffset;

        for (int i = 0; i < height; i++) {

            for (int j = 0; j < 9; j++) {

                tileEntity.containerSlots[id] = this.addSlotToContainer(new Slot(inv, id, x + (j * 18), y + (i * 18)));
                id++;
            }
        }
    }

    public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(slotId);

        if (slot != null && slot.getHasStack()) {

            ItemStack itemStack1 = slot.getStack();
            itemstack = itemStack1.copy();

            //Does tile entity have slots
            if (getTileEntitySlotAmount() > 0) {

                //inventory -> tile entity
                if (slotId <= 35) {

                    if (mergeIfPossible(slot, itemStack1, itemstack, 36, 36 + getTileEntitySlotAmount())) {

                        if (mergeInvHotbarIfPossible(slot, itemStack1, itemstack, slotId)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }

                //tile entity -> inventory
                else {

                    if (mergeIfPossible(slot, itemStack1, itemstack, 0, 35)) {
                        return ItemStack.EMPTY;
                    }

                    slot.onSlotChange(itemStack1, itemstack);
                }
            }

            else {

                if (mergeInvHotbarIfPossible(slot, itemStack1, itemstack, slotId)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemStack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            }

            else {
                slot.onSlotChanged();
            }

            if (itemStack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemStack1);
        }

        return itemstack;
    }

    private boolean mergeIfPossible(Slot slot, ItemStack is, ItemStack is2, int id, int maxId) {

        if (!this.mergeItemStack(is, id, maxId, false)) {
            return true;
        }

        slot.onSlotChange(is, is2);
        return false;
    }

    private boolean mergeInvHotbarIfPossible(Slot slot, ItemStack is, ItemStack is2, int id) {

        if (id < 27) {

            if (mergeIfPossible(slot, is, is2, 27, 35)) {
                return true;
            }

            slot.onSlotChange(is, is2);
        }

        else {

            if (mergeIfPossible(slot, is, is2, 0, 26)) {
                return true;
            }

            slot.onSlotChange(is, is2);
        }

        return false;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {

        return true;
    }
}
