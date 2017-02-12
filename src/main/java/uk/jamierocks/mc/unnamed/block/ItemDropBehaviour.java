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

import net.minecraft.util.math.MathHelper;

import java.util.Random;

/**
 * An interface used to describe the behaviour for item drops for an {@link UnnamedBlock}.
 */
public interface ItemDropBehaviour {

    /**
     * A drop behaviour for dropping no items.
     */
    ItemDropBehaviour DROP_NONE = of(0);

    /**
     * The default drop behaviour.
     */
    ItemDropBehaviour DEFAULT = of(1);

    /**
     * Creates a drop behaviour that drops the given item quantity.
     *
     * @param quantity The quantity of items to drop
     * @return The drop behaviour
     */
    static ItemDropBehaviour of(int quantity) {
        return (random) -> quantity;
    }

    /**
     * Creates a drop behaviour that drops based on given item quantity range.
     *
     * @param minimum The minimum quantity of items to drop
     * @param maximum The maximum quantity of items to drop
     * @return The drop behaviour
     */
    static ItemDropBehaviour of(int minimum, int maximum) {
        return (random) -> MathHelper.getInt(random, minimum, maximum);
    }

    /**
     * Gets the quantity of items to be dropped.
     *
     * @param random The random
     * @return The quantity dropped
     */
    int getQuantityDropped(Random random);

    /**
     * Gets the quantity of items to be dropped, with bonus applied.
     *
     * @param fortune The fortune
     * @param random The random
     * @return The quantity dropped
     */
    default int getQuantityDroppedWithBonus(int fortune, Random random) {
        return this.getQuantityDropped(random);
    }

    interface Ore extends ItemDropBehaviour {

        /**
         * A drop behaviour for dropping no items.
         */
        Ore DROP_NONE = of(0);

        /**
         * The default drop behaviour.
         */
        Ore DEFAULT = of(1);

        /**
         * Creates a drop behaviour that drops the given item quantity for an ore.
         *
         * @param quantity The quantity of items to drop
         * @return The drop behaviour
         */
        static Ore of(int quantity) {
            return (random) -> quantity;
        }

        /**
         * Creates a drop behaviour that drops based on given item quantity range for an ore.
         *
         * @param minimum The minimum quantity of items to drop
         * @param maximum The maximum quantity of items to drop
         * @return The drop behaviour
         */
        static Ore of(int minimum, int maximum) {
            return (random) -> MathHelper.getInt(random, minimum, maximum);
        }

        // based on code from BlockOre#quantityDroppedWithBonus(int, Random)
        @Override
        default int getQuantityDroppedWithBonus(int fortune, Random random) {
            if (fortune > 0) {
                int i = random.nextInt(fortune + 2) - 1;

                if (i < 0) {
                    i = 0;
                }

                return this.getQuantityDropped(random) * (i + 1);
            } else {
                return this.getQuantityDropped(random);
            }
        }

    }

}
