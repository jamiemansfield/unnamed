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

import com.google.common.collect.Range;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;
import java.util.function.Supplier;

/**
 * An extension of {@link UnnamedBlock} used for all ores in Unnamed.
 */
public class UnnamedBlockOre extends UnnamedBlock {

    private final Supplier<Item> dropped;
    private final Range<Integer> quantityDropped;
    private final Range<Integer> expDrop;

    private UnnamedBlockOre(String identifier, Supplier<Item> dropped, Range<Integer> quantityDropped, Range<Integer> expDrop) {
        super(identifier, Material.ROCK);
        this.dropped = dropped;
        this.quantityDropped = quantityDropped;
        this.expDrop = expDrop;
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
        if (this.quantityDropped != null) {
            return MathHelper.getInt(random, this.quantityDropped.lowerEndpoint(), this.quantityDropped.upperEndpoint());
        }
        return super.quantityDropped(random);
    }

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

    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
        final Random random = world instanceof World ? ((World) world).rand : new Random();
        if (this.expDrop != null) {
            return MathHelper.getInt(random, this.expDrop.lowerEndpoint(), this.expDrop.upperEndpoint());
        }
        return super.getExpDrop(state, world, pos, fortune);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String identifier;
        private Supplier<Item> drop;
        private Range<Integer> quantityDropped;
        private Range<Integer> expDrop;

        private Builder() {
        }

        public Builder identifier(String identifier) {
            this.identifier = identifier;
            return this;
        }

        public Builder drop(Supplier<Item> drop) {
            this.drop = drop;
            return this;
        }

        public Builder quantityDropped(int minimum, int maximum) {
            this.quantityDropped = Range.closed(minimum, maximum);
            return this;
        }

        public Builder expDrop(int minimum, int maximum) {
            this.expDrop = Range.closed(minimum, maximum);
            return this;
        }

        public UnnamedBlockOre build() {
            checkNotNull(this.identifier, "An identifier is required to build an ore!");

            return new UnnamedBlockOre(this.identifier, this.drop, this.quantityDropped, this.expDrop);
        }

    }
}
