package calemiutils.inventory;

import calemiutils.inventory.base.ContainerBase;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerInteractionInterfaceFilter extends ContainerBase {

    public ContainerInteractionInterfaceFilter(EntityPlayer player) {

        super(player, 0);

        isItemContainer = true;

        addPlayerInv(8, 62);
        addPlayerHotbar(8, 120);
    }
}
