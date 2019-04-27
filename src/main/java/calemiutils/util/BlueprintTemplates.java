package calemiutils.util;

import calemiutils.blueprint.BlueprintPos;

import java.util.ArrayList;

public class BlueprintTemplates {

    public static ArrayList<BlueprintPos> getNineByNine() {

        ArrayList<BlueprintPos> list = new ArrayList<>();

        int meta = 8;

        int horRad = 4;
        int height = 5;

        //Walls 1
        for (int i = -horRad; i <= horRad; i++) {

            for (int y = 0; y < height; y++) {

                list.add(new BlueprintPos(horRad, y, i, meta));
                list.add(new BlueprintPos(-horRad, y, i, meta));
            }
        }

        //Walls 2
        for (int i = -horRad + 1; i <= horRad - 1; i++) {

            for (int y = 0; y < height; y++) {

                list.add(new BlueprintPos(i, y, horRad, meta));
                list.add(new BlueprintPos(i, y, -horRad, meta));
            }
        }

        //Roof
        for (int x = -horRad; x <= horRad; x++) {

            for (int z = -horRad; z <= horRad; z++) {
                list.add(new BlueprintPos(x, height, z, meta));
            }
        }

        return list;
    }
}
