package calemiutils.inventory;

import calemiutils.inventory.base.ContainerBase;
import calemiutils.inventory.base.ItemStackInventory;
import calemiutils.item.ItemBuildersKit;
import calemiutils.util.helper.ItemHelper;
import calemiutils.util.helper.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ContainerBuildersKit extends ContainerBase {

    private final EntityPlayer player;
    private final ItemStack stack;
    private final ItemStackInventory stackInv;

    public ContainerBuildersKit(EntityPlayer player) {

        super(player, 1);

        isItemContainer = true;

        this.player = player;
        this.stack = player.getHeldItemMainhand();
        stackInv = new ItemStackInventory(stack, size);

        addPlayerInv(8, 94);
        addPlayerHotbar(8, 152);

        //New Inventory
        this.addSlotToContainer(new Slot(stackInv, 0, 80, 50));
    }

    private ItemStack getHeldStack() {

        ItemStack heldStack = player.getHeldItemMainhand();

        if (!heldStack.isEmpty() && heldStack.getItem() instanceof ItemBuildersKit) {
            return heldStack;
        }

        return null;
    }

    private ItemBuildersKit getKit() {

        if (getHeldStack() == null) {
            return null;
        }

        return (ItemBuildersKit) getHeldStack().getItem();
    }

    private NBTTagCompound getNBT() {

        if (getHeldStack() != null) {
            return ItemHelper.getNBT(player.getHeldItemMainhand());
        }

        return null;
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {

        ItemStack returnStack = super.slotClick(slotId, dragType, clickTypeIn, player);

        if (getHeldStack() != null) {

            NBTTagCompound nbt = getNBT();

            if (nbt != null) {

                ItemStack stack = stackInv.getStackInSlot(0);
                ItemStack stackFilter = NBTHelper.loadItem(nbt, 0);

                int amount = ItemBuildersKit.getAmountOfBlocks(getHeldStack());

                if (stack != null && !stack.isEmpty() && stack.getItem() instanceof ItemBlock) {

                    if (amount == 0 && !stack.isEmpty()) {
                        NBTHelper.saveItem(getNBT(), stack, 0);
                        addAmount(stack);
                    }

                    else if (stack.isItemEqual(stackFilter)) {
                        addAmount(stack);
                    }

                }
            }


        }

        return returnStack;
    }

    private void addAmount(ItemStack stack) {

        int amountAdded = ItemBuildersKit.addAmountOfBlocks(getHeldStack(), stack.getCount());
        stackInv.decrStackSize(0, amountAdded);
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {

        super.onContainerClosed(player);

        stackInv.dump(stack);
    }
}
