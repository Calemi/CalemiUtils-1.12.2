package calemiutils.packet;

import calemiutils.config.MarketItemsFile;
import calemiutils.tileentity.TileEntityBank;
import calemiutils.tileentity.TileEntityMarket;
import calemiutils.util.Location;
import calemiutils.util.helper.CurrencyHelper;
import calemiutils.util.helper.InventoryHelper;
import calemiutils.util.helper.ItemHelper;
import calemiutils.util.helper.PacketHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MarketPacket extends ServerPacketHandler {

    public MarketPacket() {

    }

    public MarketPacket(String text) {
        this.text = text;
    }

    public static class Handler implements IMessageHandler<MarketPacket, IMessage> {

        @Override
        public IMessage onMessage(MarketPacket message, MessageContext ctx) {

            String[] data = message.text.split("%");
            EntityPlayer player = ctx.getServerHandler().player;
            World world = player.world;

            if (data[0].equalsIgnoreCase("syncamount")) {

                Location location = PacketHelper.getLocation(world, data, 1);
                TileEntityMarket tileEntity = (TileEntityMarket) location.getTileEntity();

                tileEntity.purchaseAmount = Integer.parseInt(data[4]);
            }

            else if (data[0].equalsIgnoreCase("setbuymode")) {

                Location location = PacketHelper.getLocation(world, data, 1);
                TileEntityMarket tileEntity = (TileEntityMarket) location.getTileEntity();

                tileEntity.buyMode = Boolean.parseBoolean(data[4]);
            }

            else if (data[0].equalsIgnoreCase("setoffer")) {

                Location location = PacketHelper.getLocation(world, data, 1);
                TileEntityMarket tileEntity = (TileEntityMarket) location.getTileEntity();

                tileEntity.selectedOffer = Integer.parseInt(data[4]);
            }

            else if (data[0].equalsIgnoreCase("setautomode")) {

                Location location = PacketHelper.getLocation(world, data, 1);
                TileEntityMarket tileEntity = (TileEntityMarket) location.getTileEntity();

                tileEntity.automationMode = Boolean.parseBoolean(data[4]);
            }

            else if (data[0].equalsIgnoreCase("buy")) {

                String itemObj = data[1];
                int meta = Integer.parseInt(data[2]);
                int amount = Integer.parseInt(data[3]);
                int value = Integer.parseInt(data[4]);
                int purchaseAmount = Integer.parseInt(data[5]);

                ItemStack stack = CurrencyHelper.getCurrentWalletStack(player);

                if (data.length > 6) {

                    Location location = PacketHelper.getLocation(world, data, 6);
                    TileEntityBank teBank = (TileEntityBank) location.getTileEntity();

                    if (teBank.getStoredCurrency() >= value * purchaseAmount) {

                        teBank.addCurrency(-(value * purchaseAmount));
                    }
                }

                else {
                    ItemHelper.getNBT(stack).setInteger("balance", ItemHelper.getNBT(stack).getInteger("balance") - (value * purchaseAmount));
                }

                ItemHelper.spawnItem(world, player, new ItemStack(Item.getByNameOrId(itemObj), amount * purchaseAmount, meta));
            }

            else if (data[0].equalsIgnoreCase("sell")) {

                String itemObj = data[1];
                int meta = Integer.parseInt(data[2]);
                int amount = Integer.parseInt(data[3]);
                int value = Integer.parseInt(data[4]);
                int purchaseAmount = Integer.parseInt(data[5]);

                ItemStack walletStack = CurrencyHelper.getCurrentWalletStack(player);

                if (data.length > 6) {

                    Location location = PacketHelper.getLocation(world, data, 6);
                    TileEntityBank teBank = (TileEntityBank) location.getTileEntity();

                    teBank.addCurrency(value * purchaseAmount);
                }

                else {
                    ItemHelper.getNBT(walletStack).setInteger("balance", ItemHelper.getNBT(walletStack).getInteger("balance") + (value * purchaseAmount));
                }

                ItemStack[] stacks;

                if (MarketItemsFile.MarketItem.doesOreNameExist(itemObj)) {

                    stacks = MarketItemsFile.MarketItem.getStacksFromOreDict(itemObj);
                }

                else {
                    stacks = new ItemStack[]{MarketItemsFile.MarketItem.getStack(itemObj, amount, meta)};
                }

                InventoryHelper.consumeItem(player.inventory, amount * purchaseAmount, false, stacks);
            }

            return null;
        }
    }
}
