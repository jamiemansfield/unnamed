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

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

/**
 * A helper class for creative tabs.
 */
public final class CreativeTabHelper {

    /**
     * Creates a {@link CreativeTabs} with the given label and item.
     *
     * @param label The label
     * @param item The item to use to represent the tab
     * @return The creative tab
     */
    public static CreativeTabs of(final String label, final Supplier<Item> item) {
        return new CreativeTabs(label) {
            @Override
            public ItemStack getTabIconItem() {
                return new ItemStack(item.get());
            }
        };
    }

    private CreativeTabHelper() {
    }

}
