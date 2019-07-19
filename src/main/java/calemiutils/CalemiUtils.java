package calemiutils;

import calemiutils.command.CUCommandBase;
import calemiutils.config.MarketItemsFile;
import calemiutils.config.MiningUnitCostsFile;
import calemiutils.event.*;
import calemiutils.gui.base.GuiHandler;
import calemiutils.init.InitEnchantments;
import calemiutils.init.InitOreDictionaries;
import calemiutils.init.InitTileEntities;
import calemiutils.packet.*;
import calemiutils.proxy.IProxy;
import calemiutils.world.WorldGenOre;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = CUReference.MOD_ID, name = CUReference.MOD_NAME, version = CUReference.MOD_VERSION, acceptedMinecraftVersions = CUReference.MC_VERSION)
public class CalemiUtils {

    @Instance(CUReference.MOD_ID)
    public static CalemiUtils instance;

    @SidedProxy(clientSide = CUReference.CLIENT_PROXY_CLASS, serverSide = CUReference.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    public static final CreativeTabs TAB = new CUTab();

    public static SimpleNetworkWrapper network;
    private static final WorldGenOre worldGenOre = new WorldGenOre();

    public static final int guiIdWallet = 64;
    public static final int guiIdBuildersKit = 65;
    public static final int guiIdInteractionInterfaceFilter = 66;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        MiningUnitCostsFile.init();
        MarketItemsFile.init();

        network = NetworkRegistry.INSTANCE.newSimpleChannel(CUReference.MOD_ID);
        network.registerMessage(ServerPacketHandler.Handler.class, ServerPacketHandler.class, 0, Side.SERVER);
        network.registerMessage(TradingPostPacket.Handler.class, TradingPostPacket.class, 1, Side.SERVER);
        network.registerMessage(BuildersKitPacket.Handler.class, BuildersKitPacket.class, 2, Side.SERVER);
        network.registerMessage(BuildingUnitPacket.Handler.class, BuildingUnitPacket.class, 3, Side.SERVER);
        network.registerMessage(InteractionUnitPacket.Handler.class, InteractionUnitPacket.class, 4, Side.SERVER);
        network.registerMessage(LinkBookPacket.Handler.class, LinkBookPacket.class, 5, Side.SERVER);
        network.registerMessage(MarketPacket.Handler.class, MarketPacket.class, 6, Side.SERVER);
        network.registerMessage(WalletPacket.Handler.class, WalletPacket.class, 7, Side.SERVER);

        FMLCommonHandler.instance().bus().register(new JoinEvent());
        FMLCommonHandler.instance().bus().register(new KeepWalletEvent());

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

        MinecraftForge.EVENT_BUS.register(new SecurityEvent());
        MinecraftForge.EVENT_BUS.register(new WrenchEvent());
        MinecraftForge.EVENT_BUS.register(new InitEnchantments());

        InitTileEntities.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

        proxy.registerRenders();
        InitOreDictionaries.init();
        GameRegistry.registerWorldGenerator(worldGenOre, 1);
    }

    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {

        event.registerServerCommand(new CUCommandBase());
    }
}