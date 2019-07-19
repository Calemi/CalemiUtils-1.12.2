package calemiutils.enchantment;

import calemiutils.CUReference;
import calemiutils.init.InitEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CUReference.MOD_ID)
public class EnchantmentCrushing extends Enchantment {

    public EnchantmentCrushing() {

        super(Rarity.UNCOMMON, InitEnchantments.HAMMER, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
        setName("crushing");
        setRegistryName("crushing");
    }

    @Override
    public int getMaxLevel() {

        return 2;
    }

    public int getMinEnchantability(int enchantmentLevel)
    {
        return 15 + (enchantmentLevel - 1) * 9;
    }
    public int getMaxEnchantability(int enchantmentLevel)
    {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }
}
