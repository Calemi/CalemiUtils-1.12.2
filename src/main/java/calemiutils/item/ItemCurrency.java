package calemiutils.item;

import calemiutils.item.base.ItemBase;
import calemiutils.util.helper.StringHelper;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ItemCurrency extends ItemBase {

    public final int value;

    public ItemCurrency(String name, int value) {

        super("coin_" + name);
        this.value = value;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

        tooltip.add("Value (1): " + ChatFormatting.GOLD + StringHelper.printCurrency(value));

        if (stack.getCount() > 1) {
            tooltip.add("Value (" + stack.getCount() + "): " + ChatFormatting.GOLD + StringHelper.printCurrency(value * stack.getCount()));
        }
    }
}
