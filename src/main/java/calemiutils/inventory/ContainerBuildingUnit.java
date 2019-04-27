package calemiutils.inventory;

import calemiutils.inventory.base.ContainerBase;
import calemiutils.tileentity.TileEntityBuildingUnit;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerBuildingUnit extends ContainerBase {

    public ContainerBuildingUnit(EntityPlayer player, TileEntityBuildingUnit buildingUnit) {

        super(player, buildingUnit);

        addPlayerInv(8, 76);
        addPlayerHotbar(8, 134);

        addTileEntityStorageInv(buildingUnit, 0, 8, 18, 3);
    }
}
