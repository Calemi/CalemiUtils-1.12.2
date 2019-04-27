package calemiutils.tileentity;

import calemiutils.config.CUConfig;
import calemiutils.gui.GuiDiggingUnit;
import calemiutils.inventory.ContainerDiggingUnit;
import calemiutils.tileentity.base.TileEntityDiggingUnitBase;
import calemiutils.util.EnumOreCost;
import calemiutils.util.Location;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class TileEntityMiningUnit extends TileEntityDiggingUnitBase {

    @Override
    public Container getTileContainer(EntityPlayer player) {

        return new ContainerDiggingUnit(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiContainer getTileGuiContainer(EntityPlayer player) {

        return new GuiDiggingUnit(player, this, "Mining Unit");
    }

    @Override
    public List<Location> getLocationsToMine() {

        ArrayList<Location> oreList = new ArrayList<>();

        for (Location location : getLocationsWithinRange()) {

            ItemStack stack = new ItemStack(location.getBlock(), 1, location.getBlockMeta());

            oreCost:
            for (EnumOreCost oreCost : EnumOreCost.values()) {

                for (ItemStack oreStack : OreDictionary.getOres(oreCost.oreDict)) {

                    if (ItemStack.areItemsEqual(oreStack, stack)) {
                        oreList.add(location);
                        break oreCost;
                    }
                }
            }
        }

        return oreList;
    }

    @Override
    public int getCurrentOreCost() {

        EnumOreCost oreCost = EnumOreCost.getFromStack(getCurrentLocationStack());

        if (oreCost != null) {
            return oreCost.cost;
        }

        return 0;
    }

    @Override
    public int getMaxCurrency() {

        return CUConfig.misc.miningUnitCurrencyCapacity;
    }

    @Override
    public int getScaledSpeedMin() {

        return 3;
    }

    @Override
    public int getScaledSpeedMax() {

        return 15;
    }

    @Override
    public int getScaledRangeMax() {

        return CUConfig.misc.miningUnitMaxRange;
    }

}
