package calemiutils.init;

import calemiutils.config.CUConfig;
import net.minecraftforge.oredict.OreDictionary;

public class InitOreDictionaries {

    public static void init() {

        if (CUConfig.blockUtils.raritaniumOre) OreDictionary.registerOre("oreRaritanium", InitBlocks.RARITANIUM_ORE);
        OreDictionary.registerOre("gemRaritanium", InitItems.RARITANIUM);
    }
}
