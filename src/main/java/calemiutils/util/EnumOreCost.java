package calemiutils.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public enum EnumOreCost {

    COAL("Coal", 1),
    COPPER("Copper", 2),
    TIN("Tin", 2),
    IRON("Iron", 2),
    ALUMINUM("Aluminum", 3),
    LEAD("Lead", 3),
    SILVER("Silver", 4),
    GOLD("Gold", 5),
    REDSTONE("Redstone", 5),
    LAPIS("Lapis", 5),
    RUBY("Ruby", 5),
    SAPPHIRE("Sapphire", 5),
    TOPAZ("Topaz", 5),
    QUARTZ("Quartz", 5),
    SULFUR("Sulfur", 10),
    NICKEL("Nickel", 10),
    PLATINUM("Platinum", 25),
    IRIDIUM("Iridium", 25),
    MANA_INFUSED("ManaInfused", 50),
    EMERALD("Emerald", 50),
    DIAMOND("Diamond", 50),
    URANIUM("Uranium", 50),
    COBALT("Cobalt", 50),
    ARDITE("Ardite", 50);

    public final String oreDict;
    public final int cost;

    EnumOreCost(String oreDict, int cost) {

        this.oreDict = "ore" + oreDict;
        this.cost = cost;
    }

    public static EnumOreCost getFromStack(ItemStack stack) {

        for (EnumOreCost oreCost : values()) {

            for (ItemStack oreStack : OreDictionary.getOres(oreCost.oreDict)) {

                if (ItemStack.areItemsEqual(oreStack, stack)) {

                    return oreCost;
                }
            }
        }

        return null;
    }
}
