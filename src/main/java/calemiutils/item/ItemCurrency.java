package calemiutils.item;

import calemiutils.config.CUConfig;
import calemiutils.item.base.ItemBase;
import calemiutils.util.CoinColor;
import calemiutils.util.helper.StringHelper;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ItemCurrency extends ItemBase implements IItemColor {

    private final String configName;
    public final CoinColor color;
    public final int value;

    public ItemCurrency(String name, String configName, CoinColor color, int value) {

        super("coin_" + name);
        this.configName = configName;
        this.color = color;
        this.value = value;
        if (CUConfig.economy.economy) addItem();
    }

    @Override
    public int colorMultiplier(ItemStack stack, int tintIndex) {
        return color.hexCode;
    }

    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {

        tooltip.add(color.format + configName);

        tooltip.add("Value (1): " + ChatFormatting.GOLD + StringHelper.printCurrency(value));

        if (stack.getCount() > 1) {
            tooltip.add("Value (" + stack.getCount() + "): " + ChatFormatting.GOLD + StringHelper.printCurrency(value * stack.getCount()));
        }
    }
}
