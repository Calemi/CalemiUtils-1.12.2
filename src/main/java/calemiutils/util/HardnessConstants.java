package calemiutils.util;

public enum HardnessConstants {

    SECURED(1000, 5, 10, false),
    UNIT(3, 2, 10, false);

    public final int hardness;
    public final int harvestLevel;
    public final int resistance;
    public final boolean breakable;

    HardnessConstants(int hardness, int harvestLevel, int resistance, boolean breakable) {

        this.hardness = hardness;
        this.harvestLevel = harvestLevel;
        this.resistance = resistance;
        this.breakable = breakable;
    }
}
