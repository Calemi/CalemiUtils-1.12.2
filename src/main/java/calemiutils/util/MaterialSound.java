package calemiutils.util;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public enum MaterialSound {

    STONE(Material.ROCK, SoundType.STONE),
    WOOD(Material.WOOD, SoundType.WOOD),
    IRON(Material.IRON, SoundType.METAL),
    GLASS(Material.GLASS, SoundType.GLASS);

    public final Material mat;
    public final SoundType sound;

    MaterialSound(Material mat, SoundType sound) {

        this.mat = mat;
        this.sound = sound;
    }
}
