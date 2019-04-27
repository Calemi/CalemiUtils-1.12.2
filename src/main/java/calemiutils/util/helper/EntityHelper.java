package calemiutils.util.helper;

import calemiutils.util.CUTeleporter;
import calemiutils.util.Location;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.WorldServer;

public class EntityHelper {

    public static void teleportPlayer(EntityPlayerMP entity, Location location, int dim) {

        if (entity.dimension != dim) {

            MinecraftServer server = entity.getEntityWorld().getMinecraftServer();

            if (server != null) {

                WorldServer worldServer = server.getWorld(dim);

                entity.addExperienceLevel(0);

                server.getPlayerList().transferPlayerToDimension(entity, dim, new CUTeleporter(worldServer, location.x + 0.5F, location.y, location.z + 0.5F));
                entity.setPositionAndUpdate(location.x + 0.5F, location.y, location.z + 0.5F);

                entity.changeDimension(dim);
                teleport(entity, location);
            }
        }

        else {
            teleport(entity, location);
        }
    }

    public static boolean canTeleportAt(EntityPlayerMP entity, Location location) {

        Location ground = new Location(location, EnumFacing.DOWN);

        return location.isAirBlock() && new Location(location, EnumFacing.UP).isAirBlock() && ground.getBlock().getBlockFaceShape(entity.world, ground.getBlockState(), ground.getBlockPos(), EnumFacing.UP) == BlockFaceShape.SOLID;

    }

    private static void teleport(EntityPlayerMP entity, Location location) {

        entity.attemptTeleport(location.x + 0.5F, location.y, location.z + 0.5F);

        entity.fallDistance = 0F;
    }
}
