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

package uk.jamierocks.mc.unnamed.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;
import java.util.function.Supplier;

/**
 * An interface used to describe the behaviour for constructing a {@link UnnamedBlock}.
 */
public interface ConstructionBehaviour {

    /**
     * The default construction behaviour.
     */
    ConstructionBehaviour DEFAULT = new ConstructionBehaviour() {
        @Override
        public Material getDefaultMaterial() {
            return Material.ROCK;
        }

        @Override
        public UnnamedBlock construct(String identifier, Material materialIn, Supplier<Item> dropped, ItemDropBehaviour itemDropBehaviour,
                ExpDropBehaviour expDropBehaviour) {
            return new UnnamedBlock(identifier, materialIn, dropped, itemDropBehaviour, expDropBehaviour);
        }
    };

    /**
     * The construction behaviour for ores.
     */
    ConstructionBehaviour ORE = new ConstructionBehaviour() {
        @Override
        public Material getDefaultMaterial() {
            return Material.ROCK;
        }

        @Override
        public UnnamedBlock construct(String identifier, Material materialIn, Supplier<Item> dropped, ItemDropBehaviour itemDropBehaviour,
                ExpDropBehaviour expDropBehaviour) {
            return new UnnamedBlock(identifier, materialIn, dropped, itemDropBehaviour, expDropBehaviour) {
                // based on code from BlockOre#quantityDroppedWithBonus(int, Random)
                @Override
                public int quantityDroppedWithBonus(int fortune, Random random) {
                    if (fortune > 0 && this.dropped != null) {
                        int i = random.nextInt(fortune + 2) - 1;

                        if (i < 0) {
                            i = 0;
                        }

                        return this.quantityDropped(random) * (i + 1);
                    } else {
                        return this.quantityDropped(random);
                    }
                }
            };
        }
    };

    /**
     * The construction behaviour for ores.
     */
    ConstructionBehaviour GLASS = new ConstructionBehaviour() {
        @Override
        public Material getDefaultMaterial() {
            return Material.GLASS;
        }

        @Override
        public UnnamedBlock construct(String identifier, Material materialIn, Supplier<Item> dropped, ItemDropBehaviour itemDropBehaviour,
                ExpDropBehaviour expDropBehaviour) {
            return new UnnamedBlock(identifier, materialIn, dropped, itemDropBehaviour, expDropBehaviour) {
                @SideOnly(Side.CLIENT)
                public BlockRenderLayer getBlockLayer() {
                    return BlockRenderLayer.TRANSLUCENT;
                }

                @Override
                public boolean isFullCube(IBlockState state) {
                    return false;
                }

                @Override
                protected boolean canSilkHarvest() {
                    return true;
                }

                @Override
                public boolean isOpaqueCube(IBlockState state) {
                    return false;
                }

                // adapted from BlockGlass#shouldSideBeRendered(IBlockState, IBlockAccess, BlockPos, EnumFacing)
                @SideOnly(Side.CLIENT)
                public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
                    final IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
                    final Block block = iblockstate.getBlock();

                    return blockState != iblockstate || block != this && super.shouldSideBeRendered(blockState, blockAccess, pos, side);
                }
            };
        }
    };

    /**
     * Gets the default {@link Material}, to be used if none is set.
     *
     * @return The default material
     */
    Material getDefaultMaterial();

    /**
     * Gets a constructed {@link UnnamedBlock} with the given parameters.
     *
     * @param identifier The block identifier
     * @param materialIn The block material
     * @param dropped The item dropped by the block
     * @param itemDropBehaviour The item drop behaviour
     * @param expDropBehaviour The exp drop behaviour
     * @return The constructed block
     */
    UnnamedBlock construct(String identifier, Material materialIn, Supplier<Item> dropped,
            ItemDropBehaviour itemDropBehaviour, ExpDropBehaviour expDropBehaviour);

}
