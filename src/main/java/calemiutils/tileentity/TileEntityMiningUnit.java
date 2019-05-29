package calemiutils.tileentity;

import calemiutils.config.CUConfig;
import calemiutils.config.MiningUnitCostsFile;
import calemiutils.gui.GuiDiggingUnit;
import calemiutils.inventory.ContainerDiggingUnit;
import calemiutils.tileentity.base.TileEntityDiggingUnitBase;
import calemiutils.util.Location;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
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
            for (MiningUnitCostsFile.BlockInformation information : MiningUnitCostsFile.registeredBlocks.values()) {

                for (ItemStack oreStack : OreDictionary.getOres(information.oreName)) {

                    if (ItemStack.areItemsEqual(oreStack, stack)) {
                        oreList.add(location);
                        break oreCost;
                    }
                }

                for (Item oreBlock : MiningUnitCostsFile.oreBlocks) {

                    if (oreBlock == stack.getItem()) {
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

        MiningUnitCostsFile.BlockInformation information = MiningUnitCostsFile.BlockInformation.getFromStack(getCurrentLocationStack());

        if (information != null) {
            return information.cost;
        }

        return 0;
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
