package calemiutils.init;

import calemiutils.block.*;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

public class InitBlocks {

    public static final List<Block> BLOCKS = new ArrayList<>();

    public static final Block RARITANIUM_ORE = new BlockRaritaniumOre();

    public static final Block BLUEPRINT = new BlockBlueprint();

    public static final Block MARKER = new BlockMarker();
    public static final Block IRON_SCAFFOLD = new BlockScaffold();
    public static final Block TORCH_PLACER = new BlockTorchPlacer();

    public static final Block BANK = new BlockBank();
    public static final Block NETWORK_CABLE = new BlockNetworkCable();
    public static final Block NETWORK_CABLE_OPAQUE = new BlockNetworkCableOpaque();
    public static final Block NETWORK_GATE = new BlockNetworkGate(true);
    public static final Block NETWORK_GATE_DISABLED = new BlockNetworkGate(false);
    public static final Block INTERACTION_INTERFACE = new BlockInteractionInterface();
    public static final Block INTERACTION_TERMINAL = new BlockInteractionTerminal();

    public static final Block MINING_UNIT = new BlockMiningUnit();
    public static final Block QUARRY_UNIT = new BlockQuarryUnit();
    public static final Block BUILDING_UNIT = new BlockBuildingUnit();

    public static final Block TRADING_POST = new BlockTradingPost();
    public static final Block BOOK_STAND = new BlockBookStand();
}