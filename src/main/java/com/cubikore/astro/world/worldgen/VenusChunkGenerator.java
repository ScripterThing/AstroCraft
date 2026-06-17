package com.cubikore.astro.world.worldgen;

import com.cubikore.astro.block.AstroCraftBlocks;
import com.cubikore.astro.block.fluid.AstroCraftFluids;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.util.math.noise.SimplexNoiseSampler;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.noise.NoiseConfig;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class VenusChunkGenerator extends ChunkGenerator {
    public static final MapCodec<VenusChunkGenerator> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            BiomeSource.CODEC.fieldOf("biome_source").forGetter(VenusChunkGenerator::getBiomeSource)
                    ).apply(instance, VenusChunkGenerator::new)
            );

    private Random random;

    private SimplexNoiseSampler simplexNoiseSampler;

    private OctavePerlinNoiseSampler perlinNoiseSampler;
    private OctavePerlinNoiseSampler mountainDetNoiseSampler;
    private OctavePerlinNoiseSampler mountainNoiseSampler;

    private OctavePerlinNoiseSampler basaltNoiseSampler;

    private long seed = 0l;

    public VenusChunkGenerator(BiomeSource biomeSource) {
        super(biomeSource);
        random = Random.create(seed);
        simplexNoiseSampler = new SimplexNoiseSampler(random);
        perlinNoiseSampler = OctavePerlinNoiseSampler.create(random, List.of(0, 1, 2, 3, 4));
        mountainDetNoiseSampler = OctavePerlinNoiseSampler.create(random, List.of(0, 1, 2, 3, 4, 5, 6, 7));
        mountainNoiseSampler = OctavePerlinNoiseSampler.create(random, List.of(0, 1, 2, 3, 4, 5, 6, 7));
        basaltNoiseSampler = OctavePerlinNoiseSampler.create(random, List.of(0, 1, 2, 3));
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    public void carve(ChunkRegion chunkRegion, long seed, NoiseConfig noiseConfig, BiomeAccess biomeAccess, StructureAccessor structureAccessor, Chunk chunk, GenerationStep.Carver carverStep) {

    }

    @Override
    public void buildSurface(ChunkRegion region, StructureAccessor structures, NoiseConfig noiseConfig, Chunk chunk) {

    }

    @Override
    public void populateEntities(ChunkRegion region) {

    }

    @Override
    public int getWorldHeight() {
        return 0;
    }

    @Override
    public CompletableFuture<Chunk> populateNoise(Blender blender, NoiseConfig noiseConfig, StructureAccessor structureAccessor, Chunk chunk) {
        BlockPos.Mutable pos = new BlockPos.Mutable();

        int startY = chunk.getBottomY();
        int endY = chunk.getTopY();
        int baseHeight = 64;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                pos.set(x, 0, z);
                generate(pos, chunk, startY, endY, baseHeight);
            }
        }

        return CompletableFuture.completedFuture(chunk);
    }

    private void generate(BlockPos pos, Chunk chunk, int startY, int endY, int baseHeight) {
        int height = generateSurface(pos, chunk, startY);

        generatePatches(pos, chunk, startY, height);

        generateCaves(pos, chunk, startY, height);

        int x = pos.getX();
        int z = pos.getZ();

        BlockPos.Mutable heightPos = new BlockPos.Mutable();

        for(int y = startY + 50; y <= 3; y++) {
            heightPos.set(x, y, z);

            if(chunk.getBlockState(heightPos).isAir()) {
                chunk.setBlockState(
                        heightPos,
                        AstroCraftFluids.SULFURIC_ACID.getDefaultState(),
                        false
                );
            }
        }
    }

    private void generateCaves(BlockPos pos, Chunk chunk, int startY, int height) {
        int x = pos.getX();
        int z = pos.getZ();

        BlockPos.Mutable heightPos = new BlockPos.Mutable();

        for (int y = startY + 50; y <= height; y++) {
            heightPos.set(x, y, z);

            float frequency = 0.03f;

            double caveNoise = simplexNoiseSampler.sample(
                    (chunk.getPos().x * 16 + x) * frequency,
                    y * 0.05,
                    (chunk.getPos().z * 16 + z) * frequency
            );
            caveNoise = caveNoise * 0.5 + 0.5;

            double caveNoise2 = perlinNoiseSampler.sample(
                    (chunk.getPos().x * 16 + x) * 0.03,
                    y * 0.03,
                    (chunk.getPos().z * 16 + z) * 0.03
            );

            if(caveNoise > 0.5 && caveNoise2 < 0.6) {
                chunk.setBlockState(
                        heightPos,
                        Blocks.CAVE_AIR.getDefaultState(),
                        false
                );
            }
        }
    }

    private void generatePatches(BlockPos pos, Chunk chunk, int startY, float height) {
        int x = pos.getX();
        int z = pos.getZ();

        BlockPos.Mutable heightPos = new BlockPos.Mutable();

        for (int y = startY; y <= height; y++) {
            heightPos.set(x, y, z);

            float frequency = 0.01f;

            double patchNoise = simplexNoiseSampler.sample(
                    (chunk.getPos().x * 16 + x) * frequency,
                    y * frequency,
                    (chunk.getPos().z * 16 + z) * frequency
            );

            if(patchNoise > 0.5) {
                chunk.setBlockState(
                        heightPos,
                        AstroCraftBlocks.IRON_OXIDE_BLOCK.getDefaultState(),
                        false
                );
            }
            else {
                float perlFrequency = 0.01f;

                double basaltPatchNoise = basaltNoiseSampler.sample(
                        (chunk.getPos().x * 16 + x) * perlFrequency,
                        y * perlFrequency,
                        (chunk.getPos().z * 16 + z) * perlFrequency
                );

                if(basaltPatchNoise > 0.2) {
                    chunk.setBlockState(
                            heightPos,
                            Blocks.BASALT.getDefaultState(),
                            false
                    );
                }
            }
        }
    }

    private int generateSurface(BlockPos pos, Chunk chunk, int startY) {
        int x = pos.getX();
        int z = pos.getZ();

        BlockPos.Mutable heightPos = new BlockPos.Mutable();

        float frequency1 = 0.01f;

        float mountainDetFrequency = 0.0001f;
        float mountainFrequency = 0.00001f;

        double noise1 = perlinNoiseSampler.sample(
                (chunk.getPos().x * 16 + x) * frequency1,
                (chunk.getPos().z * 16 + z) * frequency1,
                0
        );

        double mountainDet = mountainDetNoiseSampler.sample(
                (chunk.getPos().x * 16 + x) * mountainDetFrequency,
                (chunk.getPos().z * 16 + z) * mountainDetFrequency,
                0
        );

        double mountainNoise = mountainNoiseSampler.sample(
                (chunk.getPos().x * 16 + x) * mountainFrequency,
                (chunk.getPos().z * 16 + z) * mountainFrequency,
                0
        );

        mountainNoise = (mountainNoise - 0.5);

        int mountainHeight = (int)Math.round((((mountainNoise * 40) + 80) * 2) * mountainDet);

        int height2 = (int)Math.round(((noise1 * 40) + 80) / 2.0f);

        int height = height2 + mountainHeight + 64;

        heightPos.set(x, height, z);

        chunk.setBlockState(heightPos,
                AstroCraftBlocks.VENUSIAN_SAND_BLOCK.getDefaultState(),
                false);

        for(int y = startY; y <= height - 1; y++) {
            heightPos.set(x, y, z);
            chunk.setBlockState(
                    heightPos,
                    AstroCraftBlocks.VENUSIAN_BASALT_BLOCK.getDefaultState(),
                    false
            );
        }

        return height;
    }

    @Override
    public int getSeaLevel() {
        return 64;
    }

    @Override
    public int getMinimumY() {
        return -64;
    }

    @Override
    public int getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView world, NoiseConfig noiseConfig) {
        return 384;
    }

    @Override
    public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world, NoiseConfig noiseConfig) {
        return null;
    }

    @Override
    public void getDebugHudText(List<String> text, NoiseConfig noiseConfig, BlockPos pos) {

    }
}
