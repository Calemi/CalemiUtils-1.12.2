package calemiutils.init;

import calemiutils.config.CUConfig;
import calemiutils.item.*;
import calemiutils.item.base.ItemBase;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

import java.util.ArrayList;
import java.util.List;

public class InitItems {

    private static Item.ToolMaterial HAMMER_WOOD = EnumHelper.addToolMaterial("hammerWood",0, 20*11, 2.0F, 3.0F, 15);
    private static Item.ToolMaterial HAMMER_STONE = EnumHelper.addToolMaterial("hammerStone",1, 44*11, 4.0F, 4.0F, 5);
    private static Item.ToolMaterial HAMMER_IRON = EnumHelper.addToolMaterial("hammerIron",2, 83*11, 6.0F, 5.0F, 14);
    private static Item.ToolMaterial HAMMER_GOLD = EnumHelper.addToolMaterial("hammerGold",0, 11*11, 12.0F, 3.0F, 22);
    private static Item.ToolMaterial HAMMER_DIAMOND = EnumHelper.addToolMaterial("hammerDiamond",3, 520*11, 8.0F, 6.0F, 10);
    private static Item.ToolMaterial HAMMER_STARLIGHT = EnumHelper.addToolMaterial("hammerStarlight",5, 0, 20.0F, 13.0F, 25);

    public static final List<Item> ITEMS = new ArrayList<>();

    public static final Item RARITANIUM = new ItemBase("raritanium").addItem();

    public static final ItemCurrency COIN_PENNY = new ItemCurrency("penny", CUConfig.economy.pennyName, CUConfig.economy.pennyColor, CUConfig.economy.pennyValue);
    public static final ItemCurrency COIN_NICKEL = new ItemCurrency("nickel", CUConfig.economy.nickelName, CUConfig.economy.nickelColor, CUConfig.economy.nickelValue);
    public static final ItemCurrency COIN_QUARTER = new ItemCurrency("quarter", CUConfig.economy.quarterName, CUConfig.economy.quarterColor, CUConfig.economy.quarterValue);
    public static final ItemCurrency COIN_DOLLAR = new ItemCurrency("dollar", CUConfig.economy.dollarName, CUConfig.economy.dollarColor, CUConfig.economy.dollarValue);

    public static final Item GOLD_CHIP = new ItemBase("gold_chip").addItem();
    public static final Item MOTOR = new ItemBase("motor").addItem();

    public static final Item KNOB_WOOD = new ItemBase("knob_wood", CUConfig.itemUtils.wood_sledgehammer);
    public static final Item KNOB_STONE = new ItemBase("knob_stone", CUConfig.itemUtils.stone_sledgehammer);
    public static final Item KNOB_IRON = new ItemBase("knob_iron",CUConfig.itemUtils.iron_sledgehammer);
    public static final Item KNOB_GOLD = new ItemBase("knob_gold", CUConfig.itemUtils.gold_sledgehammer);
    public static final Item KNOB_DIAMOND = new ItemBase("knob_diamond", CUConfig.itemUtils.diamond_sledgehammer);
    public static final Item KNOB_STARLIGHT = new ItemBase("knob_starlight", CUConfig.itemUtils.starlight_sledgehammer).setEffect();

    public static final Item SLEDGEHAMMER_WOOD = new ItemSledgehammer("wood", HAMMER_WOOD, 1.4, CUConfig.itemUtils.wood_sledgehammer);
    public static final Item SLEDGEHAMMER_STONE = new ItemSledgehammer("stone", HAMMER_STONE, 1.4, CUConfig.itemUtils.stone_sledgehammer);
    public static final Item SLEDGEHAMMER_IRON = new ItemSledgehammer("iron", HAMMER_IRON, 1.4, CUConfig.itemUtils.iron_sledgehammer);
    public static final Item SLEDGEHAMMER_GOLD = new ItemSledgehammer("gold", HAMMER_GOLD, 1.4, CUConfig.itemUtils.gold_sledgehammer);
    public static final Item SLEDGEHAMMER_DIAMOND = new ItemSledgehammer("diamond", HAMMER_DIAMOND, 1.4, CUConfig.itemUtils.diamond_sledgehammer);
    public static final Item SLEDGEHAMMER_STARLIGHT = new ItemSledgehammer("starlight", HAMMER_STARLIGHT, 2, CUConfig.itemUtils.starlight_sledgehammer);

    public static final Item SECURITY_WRENCH = new ItemSecurityWrench();

    public static final Item PENCIL = new ItemPencil();
    public static final Item BRUSH = new ItemBrush();
    public static final Item ERASER = new ItemEraser();

    public static final Item WALLET = new ItemWallet();

    public static final Item BUILDERS_KIT = new ItemBuildersKit();

    public static final Item BLENDER = new ItemBlender();
    public static final Item TORCH_BELT = new ItemTorchBelt();

    public static final Item LINK_BOOK_LOCATION = new ItemLinkBookLocation();

    public static final Item SPEED_UPGRADE = new ItemUpgrade("speed");
    public static final Item RANGE_UPGRADE = new ItemUpgrade("range");

    public static final Item BUILDING_UNIT_TEMPLATE = new ItemBuildingUnitTemplate();
    public static final Item INTERACTION_INTERFACE_FILTER = new ItemInteractionInterfaceFilter();
}
