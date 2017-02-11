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
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
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
    protected final Range<Integer> quantityDropped;
    protected final Range<Integer> expDrop;

    protected UnnamedBlock(String identifier, Material materialIn, Supplier<Item> dropped,
             Range<Integer> quantityDropped, Range<Integer> expDrop) {
        super(materialIn);
        this.dropped = dropped;
        this.quantityDropped = quantityDropped;
        this.expDrop = expDrop;

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
        if (this.quantityDropped != null) {
            return MathHelper.getInt(random, this.quantityDropped.lowerEndpoint(), this.quantityDropped.upperEndpoint());
        }
        return super.quantityDropped(random);
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
        private Type type = Type.NORMAL;
        private Material material;
        private Optional<Float> hardness = Optional.empty();
        private Optional<Float> resistance = Optional.empty();
        private Supplier<Item> drop;
        private Range<Integer> quantityDropped;
        private Range<Integer> expDrop;

        private Builder() {
        }

        public Builder identifier(String identifier) {
            this.identifier = identifier;
            return this;
        }

        public Builder type(Type type) {
            this.type = type;
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

        public Builder hardness(float hardness) {
            this.hardness = Optional.of(hardness);
            return this;
        }

        public Builder resistance(float resistance) {
            this.resistance = Optional.of(resistance);
            return this;
        }

        public Builder quantityDropped(int minimum, int maximum) {
            this.quantityDropped = Range.closed(minimum, maximum);
            return this;
        }

        public Builder quantityDropped(int quantityDropped) {
            return this.quantityDropped(quantityDropped, quantityDropped);
        }

        public Builder expDrop(int minimum, int maximum) {
            this.expDrop = Range.closed(minimum, maximum);
            return this;
        }

        public Builder expDrop(int expDrop) {
            return this.expDrop(expDrop, expDrop);
        }

        public UnnamedBlock build() {
            checkNotNull(this.identifier, "An identifier is required to build a block!");

            if (this.material == null) {
                this.material = this.type.getMaterial();
            }

            final UnnamedBlock block;
            switch (type) {
                case ORE:
                    block = new UnnamedBlockOre(this.identifier, this.material, this.drop, this.quantityDropped, this.expDrop);
                    break;
                case GLASS:
                    block = new UnnamedBlockGlass(this.identifier, this.material, this.drop, this.quantityDropped, this.expDrop);
                    break;
                default:
                    block = new UnnamedBlock(this.identifier, this.material, this.drop, this.quantityDropped, this.expDrop);
                    break;
            }

            this.hardness.ifPresent(block::setHardness);
            this.resistance.ifPresent(block::setResistance);

            return block;
        }

    }

    enum Type {

        NORMAL(Material.ROCK),
        ORE(Material.ROCK),
        GLASS(Material.GLASS),
        ;

        private final Material material;

        Type(Material material) {
            this.material = material;
        }

        public Material getMaterial() {
            return this.material;
        }

    }

}
