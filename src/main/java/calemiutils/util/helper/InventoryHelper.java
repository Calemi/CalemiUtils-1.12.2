package calemiutils.util.helper;

import calemiutils.item.ItemBuildersKit;
import calemiutils.tileentity.base.TileEntityInventoryBase;
import calemiutils.util.Location;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class InventoryHelper {

    public static boolean insertHeldItem(EntityPlayer player, EnumHand hand, Location location, IInventory inventory, int slot, boolean removeStack) {

        ItemStack stack = player.getHeldItem(hand);
        TileEntity te = location.getTileEntity();

        if (inventory.getSizeInventory() > slot) {

            if (!stack.isEmpty() && inventory.getStackInSlot(slot).isEmpty()) {

                inventory.setInventorySlotContents(slot, stack.copy());

                if (removeStack) {
                    player.setHeldItem(hand, ItemStack.EMPTY);
                }

                if (te instanceof TileEntityInventoryBase) {
                    TileEntityInventoryBase teBase = (TileEntityInventoryBase) te;

                    teBase.markForUpdate();
                }

                return true;
            }
        }

        return false;
    }

    public static void breakInventory(World world, IInventory inventory, Location location) {

        for (int i = 0; i < inventory.getSizeInventory(); i++) {

            ItemStack stack = inventory.getStackInSlot(i);

            if (!stack.isEmpty()) {

                EntityItem dropEntity = ItemHelper.spawnItem(world, location, stack);

                if (stack.hasTagCompound()) {
                    dropEntity.getItem().setTagCompound(stack.getTagCompound());
                }
            }
        }
    }

    public static void consumeItem(IInventory inventory, ItemStack itemStack, int amount, boolean suckFromBuildersKit) {

        consumeItem(0, inventory, itemStack, amount, suckFromBuildersKit);
    }

    private static void consumeItem(int slotOffset, IInventory inventory, ItemStack itemStack, int amount, boolean suckFromBuildersKit) {

        int amountLeft = amount;

        if (countItems(inventory, itemStack, suckFromBuildersKit) >= amount) {

            for (int i = slotOffset; i < inventory.getSizeInventory(); i++) {

                if (amountLeft <= 0) {
                    break;
                }

                ItemStack stack = inventory.getStackInSlot(i);

                if (!stack.isEmpty()) {

                    if (suckFromBuildersKit) {

                        if (stack.getItem() instanceof ItemBuildersKit) {

                            ItemBuildersKit buildersKit = (ItemBuildersKit) stack.getItem();

                            if (buildersKit.getBlockType(stack) != null && buildersKit.getBlockType(stack).isItemEqual(itemStack)) {

                                int stackAmount = ItemBuildersKit.getAmountOfBlocks(stack);

                                if (amountLeft >= stackAmount) {

                                    amountLeft -= stackAmount;
                                    ItemBuildersKit.setAmountOfBlocks(stack, 0);
                                }

                                else {

                                    ItemBuildersKit.setAmountOfBlocks(stack, stackAmount - amountLeft);
                                    amountLeft -= stackAmount;
                                }
                            }
                        }
                    }

                    if (stack.isItemEqual(itemStack)) {

                        if (amountLeft >= stack.getCount()) {

                            amountLeft -= stack.getCount();
                            inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                        }

                        else {

                            ItemStack copy = stack.copy();

                            inventory.decrStackSize(i, amountLeft);
                            amountLeft -= copy.getCount();
                        }
                    }
                }
            }
        }

    }

    public static int countItems(IInventory inventory, ItemStack itemStack, boolean countFromBuildersKit) {

        int count = 0;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {

            ItemStack stack = inventory.getStackInSlot(i);

            if (countFromBuildersKit && stack.getItem() instanceof ItemBuildersKit) {

                ItemBuildersKit buildersKit = (ItemBuildersKit) stack.getItem();

                if (buildersKit.getBlockType(stack) != null && buildersKit.getBlockType(stack).isItemEqual(itemStack)) {

                    count += ItemBuildersKit.getAmountOfBlocks(stack);
                }
            }

            if (stack.isItemEqual(itemStack)) {
                count += stack.getCount();
            }
        }

        return count;
    }
}
