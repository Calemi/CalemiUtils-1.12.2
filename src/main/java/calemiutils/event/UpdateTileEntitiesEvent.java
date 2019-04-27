package calemiutils.event;

import calemiutils.tileentity.base.TileEntityBase;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class UpdateTileEntitiesEvent {

    @SubscribeEvent
    public void onPlayerOpenContainer(PlayerContainerEvent.Open event) {

        for (Object o : event.getEntityPlayer().world.loadedTileEntityList) {

            if (o instanceof TileEntityBase) {

                TileEntityBase tileEntity = (TileEntityBase) o;

                tileEntity.markForUpdate();
            }
        }
    }
}
