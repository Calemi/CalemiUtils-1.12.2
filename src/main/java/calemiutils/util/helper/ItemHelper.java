package calemiutils.util.helper;

import calemiutils.util.Location;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Random;

public class ItemHelper {

    private static final Random rand = new Random();

    public static NBTTagCompound getNBT(ItemStack is) {

        if (is.getTagCompound() == null) {
            is.setTagCompound(new NBTTagCompound());
        }

        return is.getTagCompound();
    }

    public static String getStringFromStack(ItemStack stack) {

        return Item.getIdFromItem(stack.getItem()) + "&" + stack.getCount() + "&" + stack.getMetadata();
    }

    public static ItemStack getStackFromString(String string) {

        String[] data = string.split("&");

        if (data.length == 3) {

            int itemId = Integer.parseInt(data[0]);
            int stackSize = Integer.parseInt(data[1]);
            int meta = Integer.parseInt(data[2]);

            return new ItemStack(Item.getItemById(itemId), stackSize, meta);
        }

        return null;
    }

    public static String countByStacks(int count) {

        int remainder = (count % 64);

        return StringHelper.printCommas(count) + " blocks" + ((count > 64) ? " (" + ((int) Math.floor(count / 64)) + " stack(s)" + ((remainder > 0) ? (" + " + remainder + " blocks)") : ")") : "");
    }

    public static EntityItem spawnItem(World world, Location location, ItemStack is) {

        return spawnItem(world, location.x + 0.5F, location.y + 0.5F, location.z + 0.5F, is);
    }

    public static EntityItem spawnItem(World world, Entity entity, ItemStack is) {

        return spawnItem(world, (float) entity.posX, (float) entity.posY, (float) entity.posZ, is);
    }

    private static EntityItem spawnItem(World world, float x, float y, float z, ItemStack is) {

        EntityItem item = new EntityItem(world, x, y, z, is);
        item.setNoPickupDelay();
        item.motionX = -0.1F + rand.nextFloat() * 0.2F;
        item.motionY = -0.1F + rand.nextFloat() * 0.2F;
        item.motionZ = -0.1F + rand.nextFloat() * 0.2F;
        world.spawnEntity(item);
        return item;
    }
}
