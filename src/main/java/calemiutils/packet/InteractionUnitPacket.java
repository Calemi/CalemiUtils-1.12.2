package calemiutils.packet;

import calemiutils.init.InitItems;
import calemiutils.item.ItemInteractionInterfaceFilter;
import calemiutils.tileentity.TileEntityInteractionInterface;
import calemiutils.util.Location;
import calemiutils.util.helper.ItemHelper;
import calemiutils.util.helper.PacketHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class InteractionUnitPacket extends ServerPacketHandler {

    public InteractionUnitPacket() {

    }

    public InteractionUnitPacket(String text) {
        this.text = text;
    }

    public static class Handler implements IMessageHandler<InteractionUnitPacket, IMessage> {

        @Override
        public IMessage onMessage(InteractionUnitPacket message, MessageContext ctx) {

            String[] data = message.text.split("%");
            EntityPlayer player = ctx.getServerHandler().player;
            World world = player.world;

            if (data[0].equalsIgnoreCase("interface-setfilter")) {

                Location location = PacketHelper.getLocation(world, data, 1);
                TileEntityInteractionInterface tileEntity = (TileEntityInteractionInterface) location.getTileEntity();

                if (tileEntity != null) {

                    String filterIconString = data[4];
                    String filterName = "";

                    if (data.length > 5) {
                        filterName = data[5];
                    }

                    ItemStack filterStack = new ItemStack(InitItems.INTERACTION_INTERFACE_FILTER);

                    ItemInteractionInterfaceFilter.setFilterIcon(filterStack, ItemHelper.getStackFromString(filterIconString));
                    ItemInteractionInterfaceFilter.setFilterName(filterStack, filterName);

                    tileEntity.tabIconStack = filterStack;
                }
            }

            else if (data[0].equalsIgnoreCase("interface-setblock")) {

                Location location = PacketHelper.getLocation(world, data, 1);
                TileEntityInteractionInterface tileEntity = (TileEntityInteractionInterface) location.getTileEntity();

                if (tileEntity != null) {

                    tileEntity.blockIconStack = ItemHelper.getStackFromString(data[4]);
                }
            }

            else if (data[0].equalsIgnoreCase("interface-setblockname")) {

                Location location = PacketHelper.getLocation(world, data, 1);
                TileEntityInteractionInterface tileEntity = (TileEntityInteractionInterface) location.getTileEntity();

                if (tileEntity != null) {

                    if (data.length > 4) {
                        tileEntity.blockName = data[4];
                    }

                    else tileEntity.blockName = "";
                }
            }

            else if (data[0].equalsIgnoreCase("interface-clearfilter")) {

                Location location = PacketHelper.getLocation(world, data, 1);
                TileEntityInteractionInterface tileEntity = (TileEntityInteractionInterface) location.getTileEntity();

                if (tileEntity != null) {

                    tileEntity.tabIconStack = ItemStack.EMPTY;
                }
            }

            else if (data[0].equalsIgnoreCase("interface-clearblock")) {

                Location location = PacketHelper.getLocation(world, data, 1);
                TileEntityInteractionInterface tileEntity = (TileEntityInteractionInterface) location.getTileEntity();

                if (tileEntity != null) {

                    tileEntity.blockIconStack = ItemStack.EMPTY;
                }
            }

            else if (data[0].equalsIgnoreCase("terminal-interact")) {

                Location location = PacketHelper.getLocation(world, data, 1);

                boolean b = Boolean.parseBoolean(data[4]);

                player.setSneaking(b);
                location.getBlock().onBlockActivated(world, location.getBlockPos(), location.getBlockState(), player, EnumHand.MAIN_HAND, EnumFacing.UP, 0, 0, 0);
            }

            return null;
        }
    }
}
