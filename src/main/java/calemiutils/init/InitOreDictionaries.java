package calemiutils.init;

import net.minecraftforge.oredict.OreDictionary;

public class InitOreDictionaries {

    public static void init() {

        OreDictionary.registerOre("oreRaritanium", InitBlocks.RARITANIUM_ORE);
        OreDictionary.registerOre("gemRaritanium", InitItems.RARITANIUM);
    }
}
