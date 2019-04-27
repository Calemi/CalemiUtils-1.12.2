package calemiutils;

import calemiutils.command.WorldEditCommandBase;
import calemiutils.event.CurrencyEvent;
import calemiutils.event.SecurityEvent;
import calemiutils.event.UpdateTileEntitiesEvent;
import calemiutils.gui.base.GuiHandler;
import calemiutils.init.InitOreDictionaries;
import calemiutils.init.InitTileEntities;
import calemiutils.packet.ServerPacketHandler;
import calemiutils.proxy.IProxy;
import calemiutils.world.WorldGenOre;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
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

    public static final CreativeTabs TAB = new CUTab();
    @Instance(CUReference.MOD_ID)
    public static CalemiUtils instance;
    @SidedProxy(clientSide = CUReference.CLIENT_PROXY_CLASS, serverSide = CUReference.SERVER_PROXY_CLASS)
    public static IProxy proxy;
    public static SimpleNetworkWrapper network;
    public static final int guiIdWallet = 64;
    public static final int guiIdBuildersKit = 65;
    public static final int guiIdInteractionInterfaceFilter = 66;
    private static final WorldGenOre worldGenOre = new WorldGenOre();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        network = NetworkRegistry.INSTANCE.newSimpleChannel(CUReference.MOD_ID);
        network.registerMessage(ServerPacketHandler.Handler.class, ServerPacketHandler.class, 0, Side.SERVER);

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

        MinecraftForge.EVENT_BUS.register(new SecurityEvent());
        MinecraftForge.EVENT_BUS.register(new CurrencyEvent());
        MinecraftForge.EVENT_BUS.register(new UpdateTileEntitiesEvent());

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

        event.registerServerCommand(new WorldEditCommandBase());
    }
}