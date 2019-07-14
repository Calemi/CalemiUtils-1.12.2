package calemiutils.packet;

import calemiutils.tileentity.TileEntityBuildingUnit;
import calemiutils.util.Location;
import calemiutils.util.helper.PacketHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BuildingUnitPacket extends ServerPacketHandler {

    public BuildingUnitPacket() {

    }

    public BuildingUnitPacket(String text) {
        this.text = text;
    }

    public static class Handler implements IMessageHandler<BuildingUnitPacket, IMessage> {

        @Override
        public IMessage onMessage(BuildingUnitPacket message, MessageContext ctx) {

            String[] data = message.text.split("%");
            EntityPlayer player = ctx.getServerHandler().player;
            World world = player.world;

            if (data[0].equalsIgnoreCase("synccurrentbuildblueprint")) {

                Location location = PacketHelper.getLocation(world, data, 1);
                TileEntityBuildingUnit tileEntity = (TileEntityBuildingUnit) location.getTileEntity();

                if (tileEntity != null) {
                    tileEntity.currentBuildBlueprint = Integer.parseInt(data[4]);
                    tileEntity.markForUpdate();
                }
            }

            else if (data[0].equalsIgnoreCase("build")) {

                Location location = PacketHelper.getLocation(world, data, 1);
                TileEntityBuildingUnit tileEntity = (TileEntityBuildingUnit) location.getTileEntity();

                if (tileEntity != null) tileEntity.placeBlueprints();
            }

            else if (data[0].equalsIgnoreCase("readblueprints")) {

                Location location = PacketHelper.getLocation(world, data, 1);
                TileEntityBuildingUnit tileEntity = (TileEntityBuildingUnit) location.getTileEntity();

                String name = "";
                if (data.length > 4) name = data[4];

                if (tileEntity != null) {
                    tileEntity.readBlueprintsInRange(player, name);
                }
            }

            else if (data[0].equalsIgnoreCase("readblocks")) {

                Location location = PacketHelper.getLocation(world, data, 1);
                TileEntityBuildingUnit tileEntity = (TileEntityBuildingUnit) location.getTileEntity();

                String name = "";
                if (data.length > 4) name = data[4];

                if (tileEntity != null) {
                    tileEntity.readBlocksInRange(player, name);
                }
            }

            else if (data[0].equalsIgnoreCase("rotate")) {

                Location location = PacketHelper.getLocation(world, data, 1);
                TileEntityBuildingUnit tileEntity = (TileEntityBuildingUnit) location.getTileEntity();

                if (tileEntity != null) {

                    tileEntity.addRotation();
                    tileEntity.markForUpdate();
                }
            }

            return null;
        }
    }
}
