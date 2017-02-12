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

import static com.google.common.base.Preconditions.checkNotNull;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import uk.jamierocks.mc.unnamed.UnnamedMod;
import uk.jamierocks.mc.unnamed.util.BlockHelper;

import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

/**
 * An extension of {@link Block} used by all blocks in Unnamed.
 */
public class UnnamedBlock extends Block {

    protected final Supplier<Item> dropped;
    protected final ItemDropBehaviour itemDropBehaviour;
    protected final ExpDropBehaviour expDropBehaviour;

    public UnnamedBlock(String identifier, Material materialIn, Supplier<Item> dropped,
            ItemDropBehaviour itemDropBehaviour, ExpDropBehaviour expDropBehaviour) {
        super(materialIn);
        this.dropped = dropped;
        this.itemDropBehaviour = itemDropBehaviour;
        this.expDropBehaviour = expDropBehaviour;

        BlockHelper.setBlockNames(this, identifier);
        this.setCreativeTab(UnnamedMod.creativeTab);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        if (this.dropped != null) {
            return this.dropped.get();
        }
        return super.getItemDropped(state, rand, fortune);
    }

    @Override
    public int quantityDropped(Random random) {
        return this.itemDropBehaviour.getQuantityDropped(random);
    }

    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
        return this.expDropBehaviour.getQuantityDropped(state, world, pos, fortune);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String identifier;
        private Material material;
        private Supplier<Item> drop;
        private ItemDropBehaviour itemDropBehaviour = ItemDropBehaviour.DEFAULT;
        private ExpDropBehaviour expDropBehaviour = ExpDropBehaviour.DEFAULT;
        private Optional<Float> hardness = Optional.empty();
        private Optional<Float> resistance = Optional.empty();

        private Builder() {
        }

        public Builder identifier(String identifier) {
            this.identifier = identifier;
            return this;
        }

        public Builder material(Material material) {
            this.material = material;
            return this;
        }

        public Builder drop(Supplier<Item> drop) {
            this.drop = drop;
            return this;
        }

        public Builder drop(ItemDropBehaviour dropBehavior) {
            this.itemDropBehaviour = dropBehavior;
            return this;
        }

        public Builder drop(ExpDropBehaviour dropBehaviour) {
            this.expDropBehaviour = dropBehaviour;
            return this;
        }

        public Builder drop(Supplier<Item> drop, ItemDropBehaviour dropBehavior) {
            this.drop = drop;
            this.itemDropBehaviour = dropBehavior;
            return this;
        }

        public Builder hardness(float hardness) {
            this.hardness = Optional.of(hardness);
            return this;
        }

        public Builder resistance(float resistance) {
            this.resistance = Optional.of(resistance);
            return this;
        }

        public UnnamedBlock build(ConstructionBehaviour constructionBehaviour) {
            checkNotNull(this.identifier, "An identifier is required to build a block!");

            if (this.material == null) {
                this.material = constructionBehaviour.getDefaultMaterial();
            }

            final UnnamedBlock block =
                    constructionBehaviour.construct(this.identifier, this.material, this.drop, this.itemDropBehaviour, this.expDropBehaviour);
            this.hardness.ifPresent(block::setHardness);
            this.resistance.ifPresent(block::setResistance);

            return block;
        }

        public UnnamedBlock build() {
            return this.build(ConstructionBehaviour.DEFAULT);
        }

    }

}
