package calemiutils.util;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public interface IExtraInformation {

    void getButtonInformation(List<String> list, World world, Location location, ItemStack stack);

    ItemStack getButtonIcon(World world, Location location, ItemStack stack);
}
