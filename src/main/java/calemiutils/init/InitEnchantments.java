package calemiutils.init;

import calemiutils.CUReference;
import calemiutils.enchantment.EnchantmentCrushing;
import calemiutils.item.ItemSledgehammer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = CUReference.MOD_ID)
public class InitEnchantments {

    public static final EnumEnchantmentType HAMMER = EnumHelper.addEnchantmentType("weapons", (item)->(item instanceof ItemSledgehammer));

    public static final Enchantment CRUSHING = new EnchantmentCrushing();

    @SubscribeEvent
    public void registerEnchantments(RegistryEvent.Register<Enchantment> event) {
        event.getRegistry().registerAll(CRUSHING);
    }
}
