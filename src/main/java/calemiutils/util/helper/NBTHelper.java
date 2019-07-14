package calemiutils.util.helper;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NBTHelper {

    public static void saveItem(NBTTagCompound nbt, ItemStack stack, int index) {

        NBTTagCompound nbttagcompound = new NBTTagCompound();

        if (stack != null && !stack.isEmpty()) {
            stack.writeToNBT(nbttagcompound);
        }

        nbt.setTag("SingleItem" + index, nbttagcompound);

    }

    public static ItemStack loadItem(NBTTagCompound nbt, int index) {

        NBTTagCompound tag = (NBTTagCompound) nbt.getTag("SingleItem" + index);

        if (tag == null) {
            return ItemStack.EMPTY;
        }

        return new ItemStack(tag);
    }
}
