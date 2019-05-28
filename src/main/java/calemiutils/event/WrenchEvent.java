package calemiutils.event;

import calemiutils.tileentity.TileEntityBuildingUnit;
import calemiutils.tileentity.base.ICurrencyNetwork;
import calemiutils.tileentity.base.TileEntityBase;
import calemiutils.util.Location;
import calemiutils.util.helper.ItemHelper;
import calemiutils.util.helper.LoreHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WrenchEvent {

    public static void onBlockWrenched(World world, Location location) {

        TileEntity tileEntity = location.getTileEntity();

        ItemStack stack = new ItemStack(Item.getItemFromBlock(location.getBlock()), 1, location.getBlockMeta());

        if (!world.isRemote) {
            ItemHelper.spawnItem(world, location, stack);
        }

        //Currency
        if (tileEntity instanceof ICurrencyNetwork) {

            ICurrencyNetwork currencyNetwork = (ICurrencyNetwork) tileEntity;

            if (currencyNetwork.getStoredCurrency() > 0) {
                ItemHelper.getNBT(stack).setInteger("currency", currencyNetwork.getStoredCurrency());
            }
        }

        //Building Unit
        if (tileEntity instanceof TileEntityBuildingUnit) {

            TileEntityBuildingUnit teBuildingUnit = (TileEntityBuildingUnit) tileEntity;

            if (!teBuildingUnit.isEmpty()) {
                ItemStackHelper.saveAllItems(ItemHelper.getNBT(stack), teBuildingUnit.slots);
            }
        }

        ItemHelper.getNBT(stack);

        location.setBlockToAir();
    }

    @SubscribeEvent
    public void onBlockPlace(PlaceEvent event) {

        TileEntity tileEntity = event.getWorld().getTileEntity(event.getPos());
        ItemStack stack = event.getPlayer().getHeldItem(event.getHand());

        if (!stack.isEmpty() && stack.getItem() instanceof ItemBlock) {

            //Currency
            if (tileEntity instanceof ICurrencyNetwork) {

                ICurrencyNetwork currencyNetwork = (ICurrencyNetwork) tileEntity;

                if (ItemHelper.getNBT(stack).getInteger("currency") != 0) {
                    currencyNetwork.setCurrency(ItemHelper.getNBT(stack).getInteger("currency"));
                    ((TileEntityBase) tileEntity).markForUpdate();
                }
            }

            //Building Unit
            if (tileEntity instanceof TileEntityBuildingUnit) {

                TileEntityBuildingUnit teBuildingUnit = (TileEntityBuildingUnit) tileEntity;

                ItemStackHelper.loadAllItems(ItemHelper.getNBT(stack), teBuildingUnit.slots);
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onLoreEvent(ItemTooltipEvent event) {

        if (event.getItemStack().getTagCompound() != null) {

            int currency = ItemHelper.getNBT(event.getItemStack()).getInteger("currency");

            if (currency != 0) {
                event.getToolTip().add("");
                LoreHelper.addCurrencyLore(event.getToolTip(), currency);
            }
        }
    }
}
