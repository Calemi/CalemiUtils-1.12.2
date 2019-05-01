package calemiutils;

import calemiutils.config.CUConfig;
import calemiutils.init.InitItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

class CUTab extends CreativeTabs {

    CUTab() {

        super(CUReference.MOD_ID + ".tabMain");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ItemStack getTabIconItem() {

        ItemStack stack = new ItemStack(InitItems.RARITANIUM);

        if (CUConfig.itemUtils.pencil && CUConfig.blockUtils.blueprint) {
            stack = new ItemStack(InitItems.PENCIL);
        }

        return stack;
    }
}
