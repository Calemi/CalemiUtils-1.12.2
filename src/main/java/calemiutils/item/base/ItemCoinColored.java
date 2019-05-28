package calemiutils.item.base;

import calemiutils.item.ItemCurrency;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

public class ItemCoinColored implements IItemColor {

    @Override
    public int colorMultiplier(ItemStack stack, int tintIndex) {

        if (stack.getItem() instanceof ItemCurrency) {
            return ((ItemCurrency)stack.getItem()).color.hexCode;
        }

        return 0xFFFFFF;
    }
}