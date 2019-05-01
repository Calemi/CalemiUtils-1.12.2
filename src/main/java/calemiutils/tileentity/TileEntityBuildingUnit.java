package calemiutils.tileentity;

import calemiutils.block.BlockMarker;
import calemiutils.blueprint.BlueprintBuild;
import calemiutils.blueprint.BlueprintPos;
import calemiutils.blueprint.BlueprintTemplate;
import calemiutils.config.CUConfig;
import calemiutils.gui.GuiBuildingUnitInventory;
import calemiutils.init.InitItems;
import calemiutils.inventory.ContainerBuildingUnit;
import calemiutils.item.ItemBuildingUnitTemplate;
import calemiutils.tileentity.base.ITileEntityGuiHandler;
import calemiutils.tileentity.base.TileEntityInventoryBase;
import calemiutils.util.BlueprintTemplates;
import calemiutils.util.helper.ItemHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class TileEntityBuildingUnit extends TileEntityInventoryBase implements ITileEntityGuiHandler {

    private static final float pixel = 1F / 16F;
    private static final AxisAlignedBB FULL_AABB = new AxisAlignedBB(pixel * 0, pixel * 0, pixel * 0, pixel * 16, pixel * 16, pixel * 16);

    public final List<BlueprintBuild> buildBlueprints;
    public int currentBuildBlueprint = 0;
    public int currentRotation = 0;

    public int horRange = CUConfig.misc.buildingUnitHorizontalMaxRange;
    public int verRange = CUConfig.misc.buildingUnitVerticalMaxRange;

    public TileEntityBuildingUnit() {

        buildBlueprints = new ArrayList<>();
    }

    @Override
    public void update() {

        System.out.println(slots.get(0));

        currentBuildBlueprint = MathHelper.clamp(currentBuildBlueprint, 0, buildBlueprints.size() - 1);

        if (world.getWorldTime() % 20 == 0) {

            readInvForBuildBlueprints();
            searchForMarkers();
        }
    }

    @Override
    public void onLoad() {

        super.onLoad();

        buildBlueprints.add(new BlueprintBuild("None", new BlueprintTemplate(new ArrayList<>())));
        buildBlueprints.add(new BlueprintBuild("9x9", new BlueprintTemplate(BlueprintTemplates.getNineByNine())));
    }

    public void addRotation() {

        currentRotation++;
        currentRotation = currentRotation % 4;
    }

    private BlueprintBuild getCurrentBlueprintBuild() {

        if (currentBuildBlueprint < buildBlueprints.size()) {
            return buildBlueprints.get(currentBuildBlueprint);
        }

        return buildBlueprints.get(0);
    }

    public List<BlueprintPos> getCurrentPositions() {

        return getCurrentBlueprintBuild().template.getPositions(currentRotation);
    }

    private void searchForMarkers() {

        int tempHorRange = CUConfig.misc.buildingUnitHorizontalMaxRange;
        int tempVerRange = CUConfig.misc.buildingUnitVerticalMaxRange;

        for (EnumFacing dir : EnumFacing.HORIZONTALS) {

            for (int i = 0; i < CUConfig.misc.buildingUnitHorizontalMaxRange; i++) {

                if (getLocation().translate(dir, i).getBlock() instanceof BlockMarker && i < tempHorRange) {
                    tempHorRange = i;
                    break;
                }
            }
        }

        for (int i = 0; i < CUConfig.misc.buildingUnitVerticalMaxRange; i++) {

            if (getLocation().translate(EnumFacing.UP, i).getBlock() instanceof BlockMarker) {
                tempVerRange = i;
                break;
            }
        }

        horRange = tempHorRange;
        verRange = tempVerRange;
    }

    private void readInvForBuildBlueprints() {

        List<BlueprintBuild> list = new ArrayList<>();

        for (int i = 0; i < getSizeInventory(); i++) {

            ItemStack stack = getStackInSlot(i);
            NBTTagCompound nbt = ItemHelper.getNBT(stack);

            if (!stack.isEmpty() && stack.getItem() instanceof ItemBuildingUnitTemplate) {

                list.add(new BlueprintBuild(nbt.hasKey("buildName") ? nbt.getString("buildName") : "Unnamed", BlueprintTemplate.readFromItem(stack)));
            }
        }

        int size = buildBlueprints.size();

        if (list.size() + 2 != size) {

            if (size > 2) {
                buildBlueprints.subList(2, size).clear();
            }

            buildBlueprints.addAll(list);

            markForUpdate();
        }
    }

    public void readBlueprintsInRange(String name) {

        BlueprintTemplate template = BlueprintTemplate.scan(this);

        if (template.positions.size() > 0) {

            ItemStack stack = new ItemStack(InitItems.BUILDING_UNIT_TEMPLATE);

            EntityItem entity = ItemHelper.spawnItem(getWorld(), getLocation().translate(EnumFacing.UP, 1), stack);
            entity.setItem(template.writeToItem(stack));
            if (!name.isEmpty()) ItemHelper.getNBT(stack).setString("buildName", name);
        }
    }

    public void placeBlueprints() {

        getCurrentBlueprintBuild().template.construct(this);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {

        return FULL_AABB.grow(CUConfig.misc.buildingRenderBoxSize).offset(getPos());
    }

    @Override
    public Container getTileContainer(EntityPlayer player) {

        return new ContainerBuildingUnit(player, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiContainer getTileGuiContainer(EntityPlayer player) {

        return new GuiBuildingUnitInventory(player, this);
    }

    @Override
    public int getSizeInventory() {

        return 27;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {

        super.readFromNBT(nbt);
        currentBuildBlueprint = nbt.getInteger("buildBlueprint");
        currentRotation = nbt.getInteger("currentRotation");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

        super.writeToNBT(nbt);
        nbt.setInteger("buildBlueprint", currentBuildBlueprint);
        nbt.setInteger("currentRotation", currentRotation);
        return nbt;
    }
}
