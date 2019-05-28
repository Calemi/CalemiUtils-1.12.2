package calemiutils.init;

import calemiutils.config.CUConfig;
import calemiutils.item.*;
import calemiutils.item.base.ItemBase;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class InitItems {

    public static final List<Item> ITEMS = new ArrayList<>();

    public static final Item RARITANIUM = new ItemBase("raritanium").addItem();

    public static final ItemCurrency COIN_PENNY = new ItemCurrency("penny", CUConfig.economy.pennyName, CUConfig.economy.pennyColor, CUConfig.economy.pennyValue);
    public static final ItemCurrency COIN_NICKEL = new ItemCurrency("nickel", CUConfig.economy.nickelName, CUConfig.economy.nickelColor, CUConfig.economy.nickelValue);
    public static final ItemCurrency COIN_QUARTER = new ItemCurrency("quarter", CUConfig.economy.quarterName, CUConfig.economy.quarterColor, CUConfig.economy.quarterValue);
    public static final ItemCurrency COIN_DOLLAR = new ItemCurrency("dollar", CUConfig.economy.dollarName, CUConfig.economy.dollarColor, CUConfig.economy.dollarValue);

    public static final Item GOLD_CHIP = new ItemBase("gold_chip").addItem();
    public static final Item MOTOR = new ItemBase("motor").addItem();

    public static final Item WALLET = new ItemWallet();

    public static final Item SECURITY_WRENCH = new ItemSecurityWrench();

    public static final Item PENCIL = new ItemPencil();
    public static final Item BRUSH = new ItemBrush();
    public static final Item ERASER = new ItemEraser();

    public static final Item BUILDERS_KIT = new ItemBuildersKit();

    public static final Item BLENDER = new ItemBlender();
    public static final Item TORCH_BELT = new ItemTorchBelt();

    public static final Item LINK_BOOK_LOCATION = new ItemLinkBookLocation();

    public static final Item SPEED_UPGRADE = new ItemUpgrade("speed");
    public static final Item RANGE_UPGRADE = new ItemUpgrade("range");

    public static final Item BUILDING_UNIT_TEMPLATE = new ItemBuildingUnitTemplate();
    public static final Item INTERACTION_INTERFACE_FILTER = new ItemInteractionInterfaceFilter();
}
