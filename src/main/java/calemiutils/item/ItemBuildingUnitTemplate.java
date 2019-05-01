package calemiutils.item;

import calemiutils.config.CUConfig;
import calemiutils.item.base.ItemBase;
import calemiutils.util.helper.ItemHelper;
import calemiutils.util.helper.LoreHelper;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;

public class ItemBuildingUnitTemplate extends ItemBase {

    public ItemBuildingUnitTemplate() {

        super("building_unit_template", 1);
        if (CUConfig.blockUtils.buildingUnit && CUConfig.blockUtils.blueprint) addItem();
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

        NBTTagCompound nbt = ItemHelper.getNBT(stack);

        LoreHelper.addInformationLore(tooltip, "Placed in Building Units. Stores a list Blueprint data within a single item!");
        tooltip.add("");
        tooltip.add("Template Name: " + ChatFormatting.AQUA + (nbt.hasKey("buildName") ? nbt.getString("buildName") : "Unnamed"));
    }
}
