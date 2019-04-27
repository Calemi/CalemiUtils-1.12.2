package calemiutils.config;

import calemiutils.CUReference;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = CUReference.MOD_ID)
@Config.LangKey("config.title")
public class CUConfig {

    public static final CategoryTooltips tooltips = new CategoryTooltips();
    public static final CategoryWorldGen worldGen = new CategoryWorldGen();
    public static final CategoryBlockScans blockScans = new CategoryBlockScans();
    public static final CategoryMisc misc = new CategoryMisc();
    private static final String NEEDED_FOR_SERVERS = "(Only needed on Servers)";

    public static class CategoryTooltips {

        @Name("Show Information on Tooltips")
        public final boolean showInfoOnTooltips = true;

        @Name("Show Controls on Tooltips")
        public final boolean showControlsOnTooltips = true;
    }

    public static class CategoryWorldGen {

        @Name("Raritanium Ore Gen")
        @Config.Comment(NEEDED_FOR_SERVERS)
        public final boolean raritaniumOreGen = true;

        @Name("Raritanium Ore Veins Per Chunk")
        @Config.Comment(NEEDED_FOR_SERVERS)
        @RangeInt(min = 1, max = 100)
        public final int raritaniumOreVeinsPerChunk = 4;

        @Name("Raritanium Vein Size")
        @Config.Comment(NEEDED_FOR_SERVERS)
        @RangeInt(min = 2, max = 32)
        public final int raritaniumVeinSize = 8;

        @Name("Raritanium Ore Min Y")
        @Config.Comment(NEEDED_FOR_SERVERS)
        @RangeInt(min = 0, max = 256)
        public final int raritaniumOreGenMinY = 10;

        @Name("Raritanium Ore Max Y")
        @Config.Comment(NEEDED_FOR_SERVERS)
        @RangeInt(min = 0, max = 256)
        public final int raritaniumOreGenMaxY = 30;
    }

    public static class CategoryBlockScans {

        @Name("Vein Scan Max Size")
        @Config.Comment("The Vein Scan is a system used by Blueprints, Scaffolds and Networks. It scans for blocks in a chain. The max size is how many chains will occur. Lower values run faster on servers.")
        @RangeInt(min = 0, max = 1500)
        public final int veinScanMaxSize = 1500;

        @Name("Brush Max Size")
        @Config.Comment("0 to Disable. The max size of blocks the Brush can place. Lower values run faster on servers.")
        @RangeInt(min = 0, max = 5000)
        public final int worldEditMaxSize = 5000;
    }

    public static class CategoryMisc {

        @Name("Use Permission for Commands")
        @Config.Comment(NEEDED_FOR_SERVERS + " Enable this to restrict non-ops from using cu commands.")
        public final boolean usePermission = false;

        @Name("Use Security")
        @Config.Comment("Disable this to allow everyone access to anyone's blocks")
        public final boolean useSecurity = true;

        @Name("Bank Currency Capacity")
        @Config.Comment("The max amount of currency the Bank can store.")
        @RangeInt(min = 0)
        public final int bankCurrencyCapacity = 1000000;

        @Name("Trading Post Currency Capacity")
        @Config.Comment("The max amount of currency the Trading Post can store.")
        @RangeInt(min = 0)
        public final int postCurrencyCapacity = 1000000;

        @Name("Mining Unit Currency Capacity")
        @Config.Comment("The max amount of currency the Trading Post can store.")
        @RangeInt(min = 0, max = 10000)
        public final int miningUnitCurrencyCapacity = 1000;

        @Name("Scaffold Max Height Teleport")
        @Config.Comment("0 to Disable. The max height you can teleport to the top or bottom of a scaffold.")
        @RangeInt(min = 0, max = 256)
        public final int scaffoldMaxHeightTp = 256;

        @Name("Torch Place Max Range")
        @Config.Comment("The max range the Torch Place can place torches.")
        @RangeInt(min = 10, max = 48)
        public final int torchPlacerMaxRange = 48;

        @Name("Mining Unit Max Range")
        @Config.Comment("The max range the Mining Unit can mine blocks.")
        @RangeInt(min = 10, max = 64)
        public final int miningUnitMaxRange = 32;

        @Name("Building Unit Horizontal Max Range")
        @Config.Comment("The max range the Building Unit can scan for blueprints horizontally.")
        @RangeInt(min = 1, max = 128)
        public final int buildingUnitHorizontalMaxRange = 64;

        @Name("Building Unit Vertical Max Range")
        @Config.Comment("The max range the Building Unit can scan for blueprints vertically.")
        @RangeInt(min = 1, max = 256)
        public final int buildingUnitVerticalMaxRange = 64;

        @Name("Building Unit Render Box Size")
        @Config.Comment("The size of a box that determines whether to render the ghost Blueprints around the unit.")
        @RangeInt(min = 1, max = 64)
        public final int buildingRenderBoxSize = 16;

        @Name("Blender Max Juice")
        @Config.Comment("0 to Disable. The max height amount of juice the blender can store.")
        @RangeInt(min = 0, max = 1000000)
        public final int blenderMaxJuice = 1000;

        @Name("Wallet Currency Capacity")
        @Config.Comment("The max amount of currency the Wallet can store.")
        @RangeInt(min = 0, max = 99999)
        public final int walletCurrencyCapacity = 99999;

        @Name("Builders' Kit Block Capacity")
        @Config.Comment("The max amount of block the Builders' Kit can store.")
        @RangeInt(min = 0, max = 100000)
        public final int buildersKitCapacity = 100000;
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