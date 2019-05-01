package calemiutils.tileentity;

import calemiutils.config.CUConfig;
import calemiutils.gui.GuiTorchPlacer;
import calemiutils.inventory.ContainerTorchPlacer;
import calemiutils.tileentity.base.TileEntityUpgradable;
import calemiutils.util.Location;
import calemiutils.util.helper.InventoryHelper;
import calemiutils.util.helper.MathHelper;
import calemiutils.util.helper.TorchHelper;
import calemiutils.util.helper.WorldEditHelper;
import net.minecraft.block.BlockTorch;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

public class TileEntityTorchPlacer extends TileEntityUpgradable {

    private static final BlockTorch TORCH = (BlockTorch) Blocks.TORCH;
    private static final ItemStack TORCH_STACK = new ItemStack(TORCH);

    public TileEntityTorchPlacer() {

        setInputSlots(MathHelper.getCountingArray(0, 28));
        setSideInputSlots(MathHelper.getCountingArray(0, 28));
        setExtractSlots(MathHelper.getCountingArray(2, 28));
        enable = false;
    }

    @Override
    public void update() {

        if (enable && hasTorches()) {

            Location darkSpot = findDarkSpot();

            if (darkSpot == null && currentRange < getScaledRange()) {
                currentRange++;
            }

            else if (darkSpot != null) {

                tickProgress();

                if (isDoneAndReset()) {
                    darkSpot.setBlock(TORCH);
                    InventoryHelper.consumeItem(this, 1, true, TORCH_STACK);
                }
            }
        }

        else {
            currentRange = 0;
            currentProgress = 0;
        }
    }

    private Location findDarkSpot() {

        ArrayList<Location> locations = WorldEditHelper.selectWallsFromRadius(getLocation(), currentRange, 8);

        ArrayList<Location> test = new ArrayList<>();

        for (Location nextLocation : locations) {

            if (TorchHelper.canPlaceTorchAt(nextLocation)) {

                return nextLocation;
            }
        }

        return null;
    }

    private boolean hasTorches() {

        for (int i = 0; i < getSizeInventory(); i++) {

            if (getStackInSlot(i) != null && getStackInSlot(i).getItem() == Item.getItemFromBlock(TORCH)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int getSizeInventory() {

        return 29;
    }

    @Override
    public Container getTileContainer(EntityPlayer player) {

        return new ContainerTorchPlacer(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiContainer getTileGuiContainer(EntityPlayer player) {

        return new GuiTorchPlacer(player, this);
    }

    @Override
    public int getSpeedSlot() {

        return 0;
    }

    @Override
    public int getRangeSlot() {

        return 1;
    }

    @Override
    public int getMaxProgress() {

        return 100;
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
    public int getScaledRangeMin() {

        return 10;
    }

    @Override
    public int getScaledRangeMax() {

        return CUConfig.misc.torchPlacerMaxRange;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {

        super.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

        return super.writeToNBT(nbt);
    }
}
