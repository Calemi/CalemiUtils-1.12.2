package calemiutils.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MiningUnitCostsFile {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().disableHtmlEscaping().create();
    public static Map<String, BlockInformation> registeredBlocks = new HashMap<>();

    public static ArrayList<Item> oreBlocks = new ArrayList<>();

    public static void init() {

        File jsonConfig = new File(Loader.instance().getConfigDir() + "/CalemiUtils", "MiningUnitCosts.json");

        try {
            // Create the config if it doesn't already exist.
            if (!jsonConfig.exists() && jsonConfig.createNewFile()) {
                // Get a default map of blocks. You could just use a blank map, however.
                Map<String, BlockInformation> defaultMap = getDefaults();
                // Convert the map to JSON format. There is a built in (de)serializer for it already.
                String json = gson.toJson(defaultMap, new TypeToken<Map<String, BlockInformation>>(){}.getType());
                FileWriter writer = new FileWriter(jsonConfig);
                // Write to the file you passed
                writer.write(json);
                // Always close when done.
                writer.close();
            }

            // If the file exists (or we just made one exist), convert it from JSON format to a populated Map object
            registeredBlocks = gson.fromJson(new FileReader(jsonConfig), new TypeToken<Map<String, BlockInformation>>(){}.getType());
        }

        catch (IOException e) {
            // Print an error if something fails. Please use a real logger, not System.out.
            System.out.println("Error creating default configuration.");
        }

        for (MiningUnitCostsFile.BlockInformation information : MiningUnitCostsFile.registeredBlocks.values()) {

            Item item = Item.getByNameOrId(information.oreName);

            if (item != null && Block.getBlockFromItem(item) != Blocks.AIR) {

                oreBlocks.add(item);
            }
        }
    }

    private static Map<String, BlockInformation> getDefaults() {
        Map<String, BlockInformation> ret = new HashMap<>();
        addDefault(ret, "minecraft:coal", 1);
        addDefault(ret, "oreCopper", 2);
        addDefault(ret, "oreTin", 2);
        addDefault(ret, "oreIron", 2);
        addDefault(ret, "oreAluminum", 3);
        addDefault(ret, "oreLead", 3);
        addDefault(ret, "oreSilver", 4);
        addDefault(ret, "oreGold", 5);
        addDefault(ret, "oreRedstone", 5);
        addDefault(ret, "oreLapis", 5);
        addDefault(ret, "oreRuby", 5);
        addDefault(ret, "oreSapphire", 5);
        addDefault(ret, "oreTopaz", 5);
        addDefault(ret, "oreQuartz", 5);
        addDefault(ret, "oreSulfur", 10);
        addDefault(ret, "oreNickel", 10);
        addDefault(ret, "orePlatinum", 25);
        addDefault(ret, "oreIridium", 25);
        addDefault(ret, "oreManaInfused", 50);
        addDefault(ret, "oreEmerald", 50);
        addDefault(ret, "oreDiamond", 50);
        addDefault(ret, "oreUranium", 50);
        addDefault(ret, "oreCobalt", 50);
        addDefault(ret, "oreArdite", 50);
        return ret;
    }

    private static void addDefault(Map<String, BlockInformation> ret, String name, int cost) {
        ret.put(name, new BlockInformation(name, cost));
    }

    public static class BlockInformation {

        public final String oreName;
        public final int cost;

        BlockInformation(String oreName, int cost) {
            this.oreName = oreName;
            this.cost = cost;
        }

        public static BlockInformation getFromStack(ItemStack stack) {

            for (BlockInformation information : registeredBlocks.values()) {

                for (ItemStack oreStack : OreDictionary.getOres(information.oreName)) {

                    if (ItemStack.areItemsEqual(oreStack, stack)) {
                        return information;
                    }
                }

                Item item = Item.getByNameOrId(information.oreName);

                if (item != null) {
                    return information;
                }
            }

            return null;
        }
    }
}