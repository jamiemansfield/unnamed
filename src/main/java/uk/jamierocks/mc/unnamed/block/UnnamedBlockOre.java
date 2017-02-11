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

import com.google.common.collect.Range;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

import java.util.Random;
import java.util.function.Supplier;

/**
 * An extension of {@link UnnamedBlock} used for all ores in Unnamed.
 */
public class UnnamedBlockOre extends UnnamedBlock {

    protected UnnamedBlockOre(String identifier, Material materialIn, Supplier<Item> dropped,
            Range<Integer> quantityDropped, Range<Integer> expDrop) {
        super(identifier, materialIn, dropped, quantityDropped, expDrop);
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

}
