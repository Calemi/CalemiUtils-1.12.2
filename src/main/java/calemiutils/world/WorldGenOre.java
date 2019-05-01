package calemiutils.world;

import calemiutils.config.CUConfig;
import calemiutils.init.InitBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class WorldGenOre implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {

        if (world.provider.getDimension() == 0) {
            generateSurface(world, random, chunkX * 16, chunkZ * 16);
        }
    }

    private void generateSurface(World world, Random rand, int chunkX, int chunkZ) {

        if (CUConfig.worldGen.raritaniumOreGen && CUConfig.blockUtils.raritaniumOre) {

            for (int i = 0; i < CUConfig.worldGen.raritaniumOreVeinsPerChunk; i++) {

                int randPosX = chunkX + rand.nextInt(16);
                int randPosY = rand.nextInt(CUConfig.worldGen.raritaniumOreGenMaxY - CUConfig.worldGen.raritaniumOreGenMinY) + CUConfig.worldGen.raritaniumOreGenMinY;
                int randPosZ = chunkZ + rand.nextInt(16);

                (new WorldGenMinable((InitBlocks.RARITANIUM_ORE.getBlockState().getBaseState()), CUConfig.worldGen.raritaniumVeinSize)).generate(world, rand, new BlockPos(randPosX, randPosY, randPosZ));
            }
        }
    }
}