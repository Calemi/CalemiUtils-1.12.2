package calemiutils.packet;

import calemiutils.item.ItemLinkBookLocation;
import calemiutils.util.Location;
import calemiutils.util.helper.PacketHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class LinkBookPacket extends ServerPacketHandler {

    public LinkBookPacket() {

    }

    public LinkBookPacket(String text) {

        this.text = text;
    }

    public static class Handler implements IMessageHandler<LinkBookPacket, IMessage> {

        @Override
        public IMessage onMessage(LinkBookPacket message, MessageContext ctx) {

            String[] data = message.text.split("%");
            EntityPlayer player = ctx.getServerHandler().player;
            World world = player.world;

            if (data[0].equalsIgnoreCase("name")) {

                String name = "";

                if (data.length > 1) {
                    name = data[1];
                }

                ItemLinkBookLocation.bindName(player.getHeldItemMainhand(), name);
            }

            else if (data[0].equalsIgnoreCase("reset")) {
                ItemLinkBookLocation.resetLocation(player.getHeldItemMainhand(), player);
            }

            else {

                Location location = PacketHelper.getLocation(world, data, 1);
                int dim = Integer.parseInt(data[4]);

                if (data[0].equalsIgnoreCase("bind")) {
                    ItemLinkBookLocation.bindLocation(player.getHeldItemMainhand(), player, location, true);
                }

                if (data[0].equalsIgnoreCase("teleport")) {
                    ItemLinkBookLocation.teleport(player.world, player, location, dim);
                }
            }

            return null;
        }
    }
}
