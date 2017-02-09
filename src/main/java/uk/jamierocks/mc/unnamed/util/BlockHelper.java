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

package uk.jamierocks.mc.unnamed.util;

import net.minecraft.block.Block;

/**
 * A utility class for methods pertaining to blocks.
 */
public final class BlockHelper {

    /**
     * Gets the unlocalized name of a {@link Block}, used for {@link Block#setUnlocalizedName(String)}.
     *
     * @param blockId The identifier for the item
     * @return The fully formatted unlocalized name for the item.
     */
    public static String getUnlocalizedName(String blockId) {
        return Constants.MOD_ID + "." + blockId;
    }

    /**
     * Sets all the necessary names for a {@link Block}.
     *
     * @param block The block to set the names on.
     * @param blockId The block of the item.
     */
    public static void setBlockNames(Block block, String blockId) {
        block.setUnlocalizedName(getUnlocalizedName(blockId));
        block.setRegistryName(blockId);
    }

    private BlockHelper() {
    }

}
