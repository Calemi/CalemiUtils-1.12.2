package calemiutils.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MiningUnitCostsFile {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().disableHtmlEscaping().create();
    public static Map<String, BlockInformation> registeredBlocks = new HashMap<>();

    public static void init(File jsonConfig) {

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
    }

    private static Map<String, BlockInformation> getDefaults() {
        Map<String, BlockInformation> ret = new HashMap<>();
        addDefault(ret, "Coal", 1);
        addDefault(ret, "Copper", 2);
        addDefault(ret, "Tin", 2);
        addDefault(ret, "Iron", 2);
        addDefault(ret, "Aluminum", 3);
        addDefault(ret, "Lead", 3);
        addDefault(ret, "Silver", 4);
        addDefault(ret, "Gold", 5);
        addDefault(ret, "Redstone", 5);
        addDefault(ret, "Lapis", 5);
        addDefault(ret, "Ruby", 5);
        addDefault(ret, "Sapphire", 5);
        addDefault(ret, "Topaz", 5);
        addDefault(ret, "Quartz", 5);
        addDefault(ret, "Sulfur", 10);
        addDefault(ret, "Nickel", 10);
        addDefault(ret, "Platinum", 25);
        addDefault(ret, "Iridium", 25);
        addDefault(ret, "ManaInfused", 50);
        addDefault(ret, "Emerald", 50);
        addDefault(ret, "Diamond", 50);
        addDefault(ret, "Uranium", 50);
        addDefault(ret, "Cobalt", 50);
        addDefault(ret, "Ardite", 50);
        return ret;
    }

    private static void addDefault(Map<String, BlockInformation> ret, String name, int cost) {
        ret.put(name, new BlockInformation("ore" + name, cost));
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
            }

            return null;
        }
    }
}