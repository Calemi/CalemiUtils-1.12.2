package calemiutils.blueprint;

import calemiutils.block.BlockBlueprint;
import calemiutils.init.InitBlocks;
import calemiutils.tileentity.TileEntityBuildingUnit;
import calemiutils.util.Location;
import calemiutils.util.helper.ItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

public class BlueprintTemplate {

    public final ArrayList<BlueprintPos> positions;

    public BlueprintTemplate(ArrayList<BlueprintPos> positions) {

        this.positions = positions;
    }

    public static BlueprintTemplate scan(TileEntityBuildingUnit te) {

        Location location = te.getLocation();

        int horRad = te.horRange;
        int verRad = te.verRange;

        ArrayList<BlueprintPos> list = new ArrayList<>();

        for (int x = -horRad; x < horRad; x++) {

            for (int y = 0; y < verRad; y++) {

                for (int z = -horRad; z < horRad; z++) {

                    Location nextLocation = new Location(location.world, location.x + x, location.y + y, location.z + z);

                    if (nextLocation.getBlock() instanceof BlockBlueprint) {

                        BlueprintPos pos = new BlueprintPos(x, y, z, nextLocation.getBlockMeta());
                        list.add(pos);
                    }
                }
            }
        }

        return new BlueprintTemplate(list);
    }

    public static BlueprintTemplate readFromItem(ItemStack stack) {

        NBTTagCompound nbt = ItemHelper.getNBT(stack);

        ArrayList<BlueprintPos> positions = new ArrayList<>();

        if (nbt.hasKey("Template")) {

            NBTTagCompound template = nbt.getCompoundTag("Template");

            for (int i = 0; i < template.getSize(); i++) {

                NBTTagCompound posNbt = template.getCompoundTag("P-" + i);

                positions.add(new BlueprintPos(posNbt.getInteger("X"), posNbt.getInteger("Y"), posNbt.getInteger("Z"), posNbt.getInteger("M")));
            }
        }

        return new BlueprintTemplate(positions);
    }

    public BlueprintTemplate add(int meta, Location origin, Location... locations) {

        for (Location location : locations) {
            positions.add(BlueprintPos.fromLocation(origin, location, meta));
        }

        return this;
    }

    public List<BlueprintPos> getPositions(int rotation) {

        ArrayList<BlueprintPos> rotatedPositions = new ArrayList<>();

        for (BlueprintPos pos : positions) {

            int rotX = pos.x;
            int rotZ = pos.z;

            if (rotation == 1) {
                rotX = -rotX;
            }

            else if (rotation == 2) {
                rotX = -rotX;
                rotZ = -rotZ;
            }

            else if (rotation == 3) {
                rotZ = -rotZ;
            }

            rotatedPositions.add(new BlueprintPos(rotX, pos.y, rotZ, pos.meta));
        }

        return rotatedPositions;
    }

    public void construct(TileEntityBuildingUnit te) {

        for (BlueprintPos pos : getPositions(te.currentRotation)) {

            Location location = pos.toLocation(te);

            if (location.isBlockValidForPlacing(InitBlocks.BLUEPRINT)) {
                location.setBlock(InitBlocks.BLUEPRINT.getStateFromMeta(pos.meta));
            }
        }
    }

    public ItemStack writeToItem(ItemStack stack) {

        NBTTagCompound posNbt = new NBTTagCompound();

        for (int i = 0; i < positions.size(); i++) {

            NBTTagCompound nbt = new NBTTagCompound();

            BlueprintPos pos = positions.get(i);

            nbt.setInteger("X", pos.x);
            nbt.setInteger("Y", pos.y);
            nbt.setInteger("Z", pos.z);
            nbt.setInteger("M", pos.meta);

            posNbt.setTag("P-" + i, nbt);
        }

        ItemHelper.getNBT(stack).setTag("Template", posNbt);

        return stack;
    }
}
