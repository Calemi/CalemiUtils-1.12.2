package calemiutils.item;

import calemiutils.config.CUConfig;
import calemiutils.item.base.ItemBase;
import calemiutils.util.helper.LoreHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ItemUpgrade extends ItemBase {

    public ItemUpgrade(String name) {

        super(name + "_upgrade", 5);
        if (CUConfig.itemUtils.upgrades) addItem();
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

        LoreHelper.addInformationLore(tooltip, "Placed in certain machines to upgrade their abilities.");
    }
}
