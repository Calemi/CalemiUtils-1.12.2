package calemiutils.event;

import calemiutils.security.ISecurity;
import calemiutils.tileentity.base.TileEntityBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SecurityEvent {

    @SubscribeEvent
    public void onBlockPlace(PlaceEvent event) {

        TileEntity tileEntity = event.getWorld().getTileEntity(event.getPos());

        if (tileEntity instanceof TileEntityBase && tileEntity instanceof ISecurity) {

            ISecurity security = (ISecurity) tileEntity;

            security.getSecurityProfile().setOwner(event.getPlayer());
            ((TileEntityBase) tileEntity).markForUpdate();
        }
    }
}
