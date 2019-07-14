package calemiutils.packet;

import calemiutils.CalemiUtils;
import calemiutils.item.*;
import calemiutils.tileentity.*;
import calemiutils.tileentity.base.TileEntityBase;
import calemiutils.util.Location;
import calemiutils.util.helper.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerPacketHandler implements IMessage {

    String text;

    public ServerPacketHandler() {

    }

    public ServerPacketHandler(String text) {

        this.text = text;
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        text = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {

        ByteBufUtils.writeUTF8String(buf, text);
    }

    public static class Handler implements IMessageHandler<ServerPacketHandler, IMessage> {

        @Override
        public IMessage onMessage(ServerPacketHandler message, MessageContext ctx) {

            String[] data = message.text.split("%");
            EntityPlayer player = ctx.getServerHandler().player;
            World world = player.world;

            if (data[0].equalsIgnoreCase("te-enable")) {

                Location location = PacketHelper.getLocation(world, data, 1);
                boolean value = Boolean.valueOf(data[4]);

                TileEntity te = location.getTileEntity();

                if (te instanceof TileEntityBase) {
                    ((TileEntityBase) te).enable = value;
                    ((TileEntityBase) te).markForUpdate();
                }
            }

            else if (data[0].equalsIgnoreCase("gui-open")) {

                int id = Integer.parseInt(data[1]);

                player.openGui(CalemiUtils.instance, id, player.world, (int) player.posX, (int) player.posY, (int) player.posZ);
            }

            else if (data[0].equalsIgnoreCase("pencil-setcolor")) {

                int color = Integer.parseInt(data[1]);

                if (player.getHeldItemMainhand().getItem() instanceof ItemPencil) {

                    ItemPencil pencil = (ItemPencil) player.getHeldItemMainhand().getItem();
                    pencil.setColorByMeta(player.getHeldItemMainhand(), color);
                }
            }

            else if (data[0].equalsIgnoreCase("bank-sync")) {

                Location location = PacketHelper.getLocation(world, data, 3);

                TileEntityBank tileEntity = (TileEntityBank) location.getTileEntity();

                if (tileEntity != null) {
                    NBTTagCompound nbt = ItemHelper.getNBT(tileEntity.getStackInSlot(1));

                    tileEntity.storedCurrency = Integer.parseInt(data[1]);
                    nbt.setInteger("balance", Integer.parseInt(data[2]));
                }
            }

            else if (data[0].equalsIgnoreCase("iifilter-seticon")) {

                ItemStack stackToSet = ItemHelper.getStackFromString(data[1]);
                ItemStack stack = player.getHeldItemMainhand();
                ItemInteractionInterfaceFilter.setFilterIcon(stack, stackToSet);
            }

            else if (data[0].equalsIgnoreCase("iifilter-setname")) {

                String name = "";
                String tooltip = "";

                if (data.length > 1) {
                    name = data[1];

                    if (data.length > 2) {
                        tooltip = data[2];
                    }
                }

                ItemStack stack = player.getHeldItemMainhand();
                ItemInteractionInterfaceFilter.setFilterName(stack, name);
                ItemInteractionInterfaceFilter.setFilterTooltip(stack, tooltip);
            }

            return null;
        }
    }
}