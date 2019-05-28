package calemiutils.tileentity;

import calemiutils.config.CUConfig;
import calemiutils.gui.GuiDiggingUnit;
import calemiutils.inventory.ContainerDiggingUnit;
import calemiutils.tileentity.base.TileEntityDiggingUnitBase;
import calemiutils.util.Location;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class TileEntityQuarryUnit extends TileEntityDiggingUnitBase {

    @Override
    public Container getTileContainer(EntityPlayer player) {

        return new ContainerDiggingUnit(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiContainer getTileGuiContainer(EntityPlayer player) {

        return new GuiDiggingUnit(player, this, "Quarry Unit");
    }

    @Override
    public List<Location> getLocationsToMine() {

        ArrayList<Location> blockList = new ArrayList<>();

        for (Location location : getLocationsWithinRange()) {

            ItemStack stack = new ItemStack(location.getBlock(), 1, location.getBlockMeta());

            if ((location.getBlockState() != Blocks.STONE.getDefaultState() && location.getBlock() == Blocks.STONE) || location.getBlock() == Blocks.GRAVEL || location.getBlock() == Blocks.CLAY || location.getBlock() == Blocks.HARDENED_CLAY || location.getBlock() == Blocks.STAINED_HARDENED_CLAY) {

                List<String> names = new ArrayList<>();

                for (String name : OreDictionary.getOreNames()) {

                    if (name.startsWith("ore")) {
                        names.add(name);
                    }
                }

                boolean pass = true;

                for (String name : names) {

                    for (ItemStack oreStack : OreDictionary.getOres(name)) {

                        if (ItemStack.areItemsEqual(oreStack, stack)) {
                            pass = false;
                        }
                    }
                }

                if (pass) {
                    blockList.add(location);
                }
            }
        }

        return blockList;
    }

    @Override
    public int getCurrentOreCost() {

        return CUConfig.misc.quarryUnitCost;
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
