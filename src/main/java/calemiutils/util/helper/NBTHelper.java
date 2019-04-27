package calemiutils.util.helper;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NBTHelper {

    public static void saveItem(NBTTagCompound nbt, ItemStack stack) {

        NBTTagCompound nbttagcompound = new NBTTagCompound();

        if (stack != null && !stack.isEmpty()) {
            stack.writeToNBT(nbttagcompound);
        }

        nbt.setTag("SingleItem", nbttagcompound);

    }

    public static ItemStack loadItem(NBTTagCompound nbt) {

        NBTTagCompound tag = (NBTTagCompound) nbt.getTag("SingleItem");

        if (tag == null) {
            return ItemStack.EMPTY;
        }

        return new ItemStack(tag);
    }
}
