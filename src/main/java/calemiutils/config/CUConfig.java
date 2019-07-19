package calemiutils.config;

import calemiutils.CUReference;
import calemiutils.util.CoinColor;
import com.google.gson.JsonObject;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.function.BooleanSupplier;

@SuppressWarnings("CanBeFinal")
@Config(modid = CUReference.MOD_ID, name = "CalemiUtils/CalemiUtils")
@Config.LangKey("config.title")
public class CUConfig implements IConditionFactory {

    public static final CategoryItemUtils itemUtils = new CategoryItemUtils();
    public static final CategoryBlockUtils blockUtils = new CategoryBlockUtils();
    public static final CategoryTooltips tooltips = new CategoryTooltips();
    public static final CategoryWorldGen worldGen = new CategoryWorldGen();
    public static final CategoryBlockScans blockScans = new CategoryBlockScans();
    public static final CategoryEconomy economy = new CategoryEconomy();
    public static final CategoryWallet wallet = new CategoryWallet();
    public static final CategoryBuildingUnit buildingUnit = new CategoryBuildingUnit();
    public static final CategoryMisc misc = new CategoryMisc();

    private static final String NEEDED_FOR_SERVERS = "(Only needed on Servers)";

    @Override
    public BooleanSupplier parse(JsonContext context, JsonObject json) {

        return () -> economy.currencyRecipes;
    }

    public static class CategoryItemUtils {

        @Name("Enable Wood Sledgehammer")
        @Config.Comment("Disable this to remove the Wood Sledgehammer.")
        public boolean wood_sledgehammer = true;

        @Name("Enable Stone Sledgehammer")
        @Config.Comment("Disable this to remove the Stone Sledgehammer.")
        public boolean stone_sledgehammer = true;

        @Name("Enable Iron Sledgehammer")
        @Config.Comment("Disable this to remove the Iron Sledgehammer.")
        public boolean iron_sledgehammer = true;

        @Name("Enable Gold Sledgehammer")
        @Config.Comment("Disable this to remove the Gold Sledgehammer.")
        public boolean gold_sledgehammer = true;

        @Name("Enable Diamond Sledgehammer")
        @Config.Comment("Disable this to remove the Diamond Sledgehammer.")
        public boolean diamond_sledgehammer = true;

        @Name("Enable Starlight Sledgehammer")
        @Config.Comment("Disable this to remove the Starlight Sledgehammer.")
        public boolean starlight_sledgehammer = true;

        @Name("Enable Wallet")
        @Config.Comment("Disable this to remove the Wallet.")
        public boolean wallet = true;

        @Name("Enable Security Wrench")
        @Config.Comment("Disable this to remove the Security Wrench.")
        public boolean securityWrench = true;

        @Name("Enable Pencil")
        @Config.Comment("Disable this to remove the Pencil.")
        public boolean pencil = true;

        @SuppressWarnings("CanBeFinal")
        @Name("Enable Brush")
        @Config.Comment("Disable this to remove the Brush")
        public boolean brush = true;

        @Name("Enable Eraser")
        @Config.Comment("Disable this to remove the Eraser.")
        public boolean eraser = true;

        @Name("Enable Builders' Kit")
        @Config.Comment("Disable this to remove the Builders' Kit.")
        public boolean buildersKit = true;

        @Name("Enable Blender")
        @Config.Comment("Disable this to remove the Blender.")
        public boolean blender = true;

        @Name("Enable Torch Belt")
        @Config.Comment("Disable this to remove the Torch Belt.")
        public boolean torchBelt = true;

        @Name("Enable Location Link Book")
        @Config.Comment("Disable this to remove the Location Link Book.")
        public boolean locationLinkBook = true;

        @Name("Enable Upgrades")
        @Config.Comment("Disable this to remove the Upgrades.")
        public boolean upgrades = true;
    }

    public static class CategoryBlockUtils {

        @Name("Enable Raritanium Ore")
        @Config.Comment("Disable this to remove Raritanium Ore.")
        public boolean raritaniumOre = true;

        @Name("Enable Blueprint")
        @Config.Comment("Disable this to remove Blueprint and its tools.")
        public boolean blueprint = true;

        @Name("Enable Iron Scaffold")
        @Config.Comment("Disable this to remove Iron Scaffold.")
        public boolean ironScaffold = true;

        @Name("Enable Torch Placer")
        @Config.Comment("Disable this to remove the Torch Placer.")
        public boolean torchPlacer = true;

        @Name("Enable Bank")
        @Config.Comment("Disable this to remove the Bank.")
        public boolean bank = true;

        @Name("Enable Network Cable & Gate")
        @Config.Comment("Disable this to remove Network Cable & Gate.")
        public boolean networkCable = true;

        @Name("Enable Interaction Interface & Terminal")
        @Config.Comment("Disable this to remove the Interaction Interface & Terminal.")
        public boolean interactionNetwork = true;

        @Name("Enable Mining Unit")
        @Config.Comment("Disable this to remove the Mining Unit.")
        public boolean miningUnit = true;

        @Name("Enable Quarry Unit")
        @Config.Comment("Disable this to remove the Quarry Unit.")
        public boolean quarryUnit = true;

        @Name("Enable Building Unit")
        @Config.Comment("Disable this to remove the Building Unit.")
        public boolean buildingUnit = true;

        @Name("Enable Trading Post")
        @Config.Comment("Disable this to remove the Trading Post.")
        public boolean tradingPost = true;

        @Name("Enable Market")
        @Config.Comment("Disable this to remove the Market.")
        public boolean market = true;

        @Name("Enable Book Stand")
        @Config.Comment("Disable this to remove the Book Stand.")
        public boolean bookStand = true;
    }

    public static class CategoryTooltips {

        @Name("Show Information on Tooltips")
        public boolean showInfoOnTooltips = true;

        @Name("Show Controls on Tooltips")
        public boolean showControlsOnTooltips = true;
    }

    public static class CategoryWorldGen {

        @Name("Raritanium Ore Gen")
        @Config.Comment(NEEDED_FOR_SERVERS)
        public boolean raritaniumOreGen = true;

        @Name("Raritanium Ore Veins Per Chunk")
        @Config.Comment(NEEDED_FOR_SERVERS)
        @RangeInt(min = 1, max = 100)
        public int raritaniumOreVeinsPerChunk = 4;

        @Name("Raritanium Vein Size")
        @Config.Comment(NEEDED_FOR_SERVERS)
        @RangeInt(min = 2, max = 32)
        public int raritaniumVeinSize = 8;

        @Name("Raritanium Ore Min Y")
        @Config.Comment(NEEDED_FOR_SERVERS)
        @RangeInt(min = 0, max = 256)
        public int raritaniumOreGenMinY = 10;

        @Name("Raritanium Ore Max Y")
        @Config.Comment(NEEDED_FOR_SERVERS)
        @RangeInt(min = 0, max = 256)
        public int raritaniumOreGenMaxY = 30;
    }

    public static class CategoryBlockScans {

        @Name("Vein Scan Max Size")
        @Config.Comment("The Vein Scan is a system used by Blueprints, Scaffolds and Networks. It scans for blocks in a chain. The max size is how many chains will occur. Lower values run faster on servers.")
        @RangeInt(min = 0, max = 1500)
        public int veinScanMaxSize = 1500;

        @Name("Brush Max Size")
        @Config.Comment("0 to Disable. The max size of blocks the Brush can place. Lower values run faster on servers.")
        @RangeInt(min = 0, max = 5000)
        public int worldEditMaxSize = 5000;
    }

    public static class CategoryEconomy {

        @Name("Enable Economy")
        @Config.Comment("Disable this to remove Economy and everything that uses it.")
        public boolean economy = true;

        @Name("Enable Coin Recipes")
        @Config.Comment("Disable this to remove the recipes for the coins.")
        boolean currencyRecipes = true;

        @Name("Currency Name")
        @Config.Comment("Edit this name to change the name of the currency for everything. Try to keep it small.")
        public String currencyName = "RC";

        @Name("Penny Name")
        @Config.Comment("Edit this name to change the name of the Penny.")
        public String pennyName = "Penny";

        @Name("Penny Color")
        @Config.Comment("Edit this name to change the color of the Penny.")
        public CoinColor pennyColor = CoinColor.BRONZE;

        @Name("Penny Value")
        @Config.Comment("Edit this name to change the value of the Penny.")
        @RangeInt(min = 0, max = 10000)
        public int pennyValue = 1;

        @Name("Nickel Name")
        @Config.Comment("Edit this name to change the name of the Nickel.")
        public String nickelName = "Nickel";

        @Name("Nickel Color")
        @Config.Comment("Edit this name to change the color of the Nickel.")
        public CoinColor nickelColor = CoinColor.GRAY;

        @Name("Nickel Value")
        @Config.Comment("Edit this name to change the value of the Nickel.")
        @RangeInt(min = 0, max = 10000)
        public int nickelValue = 5;

        @Name("Quarter Name")
        @Config.Comment("Edit this name to change the name of the Quarter.")
        public String quarterName = "Quarter";

        @Name("Quarter Color")
        @Config.Comment("Edit this name to change the color of the Quarter.")
        public CoinColor quarterColor = CoinColor.SILVER;

        @Name("Quarter Value")
        @Config.Comment("Edit this name to change the value of the Quarter.")
        @RangeInt(min = 0, max = 10000)
        public int quarterValue = 25;

        @Name("Dollar Name")
        @Config.Comment("Edit this name to change the name of the Dollar.")
        public String dollarName = "Dollar";

        @Name("Dollar Color")
        @Config.Comment("Edit this name to change the color of the Dollar.")
        public CoinColor dollarColor = CoinColor.GOLD;

        @Name("Dollar Value")
        @Config.Comment("Edit this name to change the value of the Dollar.")
        @RangeInt(min = 0, max = 10000)
        public int dollarValue = 100;
    }

    public static class CategoryWallet {

        @Name("Wallet Currency Capacity")
        @Config.Comment("The max amount of currency the Wallet can store.")
        @RangeInt(min = 0, max = 99999999)
        public int walletCurrencyCapacity = 99999999;

        @Name("Give Starting Wallet")
        @Config.Comment("Enable this to give players a wallet the first time they join the world.")
        public boolean startingWallet = false;

        @Name("Keep Wallets on Death")
        @Config.Comment("Enable this to spawn any wallets at the player spawnpoint when they die.")
        public boolean keepWallet = false;

        @Name("Render Wallet Currency Overlay")
        @Config.Comment("Enable this render an overlay on your game screen showing your wallet stats.")
        public boolean walletOverlay = true;
    }

    public static class CategoryBuildingUnit {

        @Name("Building Unit Horizontal Max Range")
        @Config.Comment("The max range the Building Unit can scan for blueprints horizontally.")
        @RangeInt(min = 1, max = 128)
        public int buildingUnitHorizontalMaxRange = 64;

        @Name("Building Unit Vertical Max Range")
        @Config.Comment("The max range the Building Unit can scan for blueprints vertically.")
        @RangeInt(min = 1, max = 256)
        public int buildingUnitVerticalMaxRange = 64;

        @Name("Building Unit Render Box Size")
        @Config.Comment("The size of a box that determines whether to render the ghost Blueprints around the unit.")
        @RangeInt(min = 1, max = 64)
        public int buildingUnitRenderBoxSize = 16;

        @Name("Building Unit Max Block Size")
        @Config.Comment("The size of a box that determines whether to render the ghost Blueprints around the unit.")
        @RangeInt(min = 1, max = 2000)
        public int buildingUnitBlockSize = 1500;
    }

    public static class CategoryMisc {

        @Name("Use Permission for Commands")
        @Config.Comment(NEEDED_FOR_SERVERS + " Enable this to restrict non-ops from using cu commands.")
        public boolean usePermission = false;

        @Name("Use Security")
        @Config.Comment("Disable this to allow everyone access to anyone's blocks.")
        public boolean useSecurity = true;

        @Name("Make Blueprint Passable")
        @Config.Comment("Enable this to allow players to pass through Blueprint.")
        public boolean blueprintPassable = false;

        @Name("Bank Currency Capacity")
        @Config.Comment("The max amount of currency the Bank can store.")
        @RangeInt(min = 0)
        public int bankCurrencyCapacity = 1000000;

        @Name("Trading Post Currency Capacity")
        @Config.Comment("The max amount of currency the Trading Post can store.")
        @RangeInt(min = 0)
        public int postCurrencyCapacity = 1000000;

        @Name("Scaffold Max Height Teleport")
        @Config.Comment("0 to Disable. The max height you can teleport to the top or bottom of a scaffold.")
        @RangeInt(min = 0, max = 256)
        public int scaffoldMaxHeightTp = 256;

        @Name("Torch Place Max Range")
        @Config.Comment("The max range the Torch Place can place torches.")
        @RangeInt(min = 10, max = 48)
        public int torchPlacerMaxRange = 48;

        @Name("Mining Unit Max Range")
        @Config.Comment("The max range the Mining Unit can mine blocks.")
        @RangeInt(min = 10, max = 64)
        public int miningUnitMaxRange = 32;

        @Name("Upgrade Stack Size")
        @Config.Comment("The max range the Torch Place can place torches.")
        @RangeInt(min = 1, max = 64)
        public int upgradeStackSize = 5;

        @Name("Speed Upgrade Cost Multiplier")
        @Config.Comment("This value is multiplied and added to the current cost of the Unit. This value increases based on how many Speed Upgrades are in the slot.")
        @Config.RangeDouble(min = 0)
        public double speedUpgradeCostMultiplier = 0;

        @Name("Blender Max Juice")
        @Config.Comment("0 to Disable. The max height amount of juice the blender can store.")
        @RangeInt(min = 0, max = 1000000)
        public int blenderMaxJuice = 1000;

        @Name("Quarry Unit RC Cost")
        @Config.Comment("The cost of every block the Quarry Unit mines.")
        @RangeInt(min = 0)
        public int quarryUnitCost = 1;

        @Name("Builders' Kit Block Capacity")
        @Config.Comment("The max amount of block the Builders' Kit can store.")
        @RangeInt(min = 0, max = 100000)
        public int buildersKitCapacity = 100000;
    }

    @Mod.EventBusSubscriber(modid = CUReference.MOD_ID)
    private static class EventHandler {

        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {

            if (event.getModID().equals(CUReference.MOD_ID)) {
                ConfigManager.sync(CUReference.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}