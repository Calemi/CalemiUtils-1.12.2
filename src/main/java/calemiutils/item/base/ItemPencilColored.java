package calemiutils.item.base;

import calemiutils.init.InitItems;
import calemiutils.item.ItemPencil;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;

public class ItemPencilColored implements IItemColor {

    @Override
    public int colorMultiplier(ItemStack stack, int tintIndex) {

        ItemPencil pencil = (ItemPencil) InitItems.PENCIL;

        if (tintIndex == 1) {
            int colorMeta = pencil.getColorMeta(stack);
            EnumDyeColor dye = EnumDyeColor.byMetadata(colorMeta);

            return dye.getColorValue();
        }

        return 0xFFFFFF;
    }
}