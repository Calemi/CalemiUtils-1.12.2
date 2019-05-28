package calemiutils.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MarketItemsFile {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().disableHtmlEscaping().create();
    public static Map<String, MarketItem> registeredBlocks = new HashMap<>();

    public static void init(File jsonConfig) {

        try {
            // Create the config if it doesn't already exist.
            if (!jsonConfig.exists() && jsonConfig.createNewFile()) {
                // Get a default map of blocks. You could just use a blank map, however.
                Map<String, MarketItem> defaultMap = getDefaults();
                // Convert the map to JSON format. There is a built in (de)serializer for it already.
                String json = gson.toJson(defaultMap, new TypeToken<Map<String, MarketItem>>(){}.getType());
                FileWriter writer = new FileWriter(jsonConfig);
                // Write to the file you passed
                writer.write(json);
                // Always close when done.
                writer.close();
            }

            // If the file exists (or we just made one exist), convert it from JSON format to a populated Map object
            registeredBlocks = gson.fromJson(new FileReader(jsonConfig), new TypeToken<Map<String, MarketItem>>(){}.getType());
        }

        catch (IOException e) {
            // Print an error if something fails. Please use a real logger, not System.out.
            System.out.println("Error creating default configuration.");
        }
    }

    private static Map<String, MarketItem> getDefaults() {
        Map<String, MarketItem> ret = new HashMap<>();

        addDefault(ret, "BreadBuy", "minecraft:bread", 0, 4, 2, true);
        addDefault(ret, "SteakBuy", "minecraft:cooked_beef", 0, 4, 6, true);
        addDefault(ret, "GoldenAppleBuy", "minecraft:golden_apple", 1, 1, 128, true);

        addDefault(ret, "CobbleBuy", "minecraft:cobblestone", 0, 128, 32, true);
        addDefault(ret, "OakPlanksBuy", "minecraft:planks", 0, 16, 16, true);

        addDefault(ret, "CobbleSell", "minecraft:cobblestone", 0, 64, 1, false);
        addDefault(ret, "DiamondSell", "minecraft:diamond", 0, 1, 64, false);
        addDefault(ret, "EmeraldSell", "minecraft:emerald", 0, 1, 16, false);

        return ret;
    }

    private static void addDefault(Map<String, MarketItem> ret, String stackName, String stackObj, int meta, int amount, int value, boolean isBuy) {

        ret.put(stackName, new MarketItem(stackObj, meta, amount, value, isBuy));
    }

    public static class MarketItem {

        public final String stackObj;
        public final int meta;
        public final int amount;
        public final int value;
        public final boolean isBuy;

        MarketItem(String stackObj, int meta, int amount, int value, boolean isBuy) {
            this.stackObj = stackObj;
            this.meta = meta;
            this.amount = amount;
            this.value = value;
            this.isBuy = isBuy;
        }

        public ItemStack getStack() {

            Item item = Item.getByNameOrId(stackObj);

            if (item != null) {
                return new ItemStack(item, amount, meta);
            }

            return ItemStack.EMPTY;
        }

        public static MarketItem readFromNBT(NBTTagCompound nbt) {
            return new MarketItem(nbt.getString("stackObj"), nbt.getInteger("meta"), nbt.getInteger("amount"), nbt.getInteger("value"), nbt.getBoolean("isBuy"));
        }

        public NBTTagCompound writeToNBT() {

            NBTTagCompound nbt = new NBTTagCompound();

            nbt.setString("stackObj", stackObj);
            nbt.setInteger("meta", meta);
            nbt.setInteger("amount", amount);
            nbt.setInteger("value", value);
            nbt.setBoolean("isBuy", isBuy);

            return nbt;
        }
    }
}