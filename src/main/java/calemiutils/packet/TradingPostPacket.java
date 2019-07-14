package calemiutils.packet;

import calemiutils.tileentity.TileEntityTradingPost;
import calemiutils.util.Location;
import calemiutils.util.helper.ItemHelper;
import calemiutils.util.helper.PacketHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class TradingPostPacket extends ServerPacketHandler {

    public TradingPostPacket() {

    }

    public TradingPostPacket(String text) {

        this.text = text;
    }

    public static class Handler implements IMessageHandler<TradingPostPacket, IMessage> {

        @Override
        public IMessage onMessage(TradingPostPacket message, MessageContext ctx) {

            String[] data = message.text.split("%");
            EntityPlayer player = ctx.getServerHandler().player;
            World world = player.world;

            if (data[0].equalsIgnoreCase("setoptions")) {

                Location location = PacketHelper.getLocation(world, data, 3);

                TileEntityTradingPost tileEntity = (TileEntityTradingPost) location.getTileEntity();

                if (tileEntity != null) {

                    if (data[1].equalsIgnoreCase("amount")) {
                        tileEntity.amountForSale = Integer.parseInt(data[2]);
                    }

                    if (data[1].equalsIgnoreCase("price")) {
                        tileEntity.salePrice = Integer.parseInt(data[2]);
                    }

                    tileEntity.amountForSale = MathHelper.clamp(tileEntity.amountForSale, 1, 1000);
                    tileEntity.salePrice = MathHelper.clamp(tileEntity.salePrice, 0, 10000);

                    tileEntity.markForUpdate();
                }
            }

            else if (data[0].equalsIgnoreCase("setstackforsale")) {

                Location location = PacketHelper.getLocation(world, data, 1);
                TileEntityTradingPost tileEntity = (TileEntityTradingPost) location.getTileEntity();

                String string = data[4];

                if (tileEntity != null) {

                    ItemStack stack = ItemHelper.getStackFromString(string);

                    if (data.length > 5) {
                        ItemHelper.attachNBTFromString(stack, data[5]);
                    }

                    tileEntity.setStackForSale(stack);
                    tileEntity.markForUpdate();
                }
            }

            else if (data[0].equalsIgnoreCase("togglesellmode")) {

                Location location = PacketHelper.getLocation(world, data, 1);
                TileEntityTradingPost tileEntity = (TileEntityTradingPost) location.getTileEntity();

                Boolean mode = Boolean.parseBoolean(data[4]);

                if (tileEntity != null) tileEntity.buyMode = mode;
                tileEntity.markForUpdate();
            }

            return null;
        }
    }
}
