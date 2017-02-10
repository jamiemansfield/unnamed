/*
 * This file is part of unnamed, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017, Jamie Mansfield <https://www.jamierocks.uk/>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package uk.jamierocks.mc.unnamed;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import uk.jamierocks.mc.unnamed.init.UnnamedBlocks;

import java.util.Random;

/**
 * The {@link IWorldGenerator} for Unnamed.
 */
public class UnnamedWorldGenerator implements IWorldGenerator {

    private final WorldGenMinable tungstenGen;

    public UnnamedWorldGenerator() {
        this.tungstenGen = new WorldGenMinable(UnnamedBlocks.tungsten_ore.getDefaultState(), 9);
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        switch (world.provider.getDimension()) {
            case -1: // nether
                this.generateNether(random, new BlockPos(chunkX, 0,  chunkZ), world, chunkGenerator, chunkProvider);
                break;
            case 0: // overworld
                this.generateOverworld(random, new BlockPos(chunkX, 0,  chunkZ), world, chunkGenerator, chunkProvider);
                break;
            case 1: // end
                this.generateEnd(random, new BlockPos(chunkX, 0,  chunkZ), world, chunkGenerator, chunkProvider);
                break;
        }
    }

    private void generateNether(Random random, BlockPos chunkPos, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
    }

    private void generateOverworld(Random random, BlockPos chunkPos, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        this.genStandardOre(chunkPos, world, random, 2, this.tungstenGen, 0, 32);
    }

    private void generateEnd(Random random, BlockPos chunkPos, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
    }

    // adapted from BiomeDecorator#genStandardOre1(World, Random, int, WorldGenerator, int, int)
    private void genStandardOre(BlockPos chunkPos, World worldIn, Random random, int blockCount, WorldGenerator generator, int minHeight,
            int maxHeight) {
        if (maxHeight < minHeight) {
            int i = minHeight;
            minHeight = maxHeight;
            maxHeight = i;
        } else if (maxHeight == minHeight) {
            if (minHeight < 255) {
                ++maxHeight;
            } else {
                --minHeight;
            }
        }

        for (int j = 0; j < blockCount; ++j) {
            BlockPos blockpos = chunkPos.add(random.nextInt(16), random.nextInt(maxHeight - minHeight) + minHeight, random.nextInt(16));
            generator.generate(worldIn, random, blockpos);
        }
    }

}
