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

import static uk.jamierocks.mc.unnamed.init.UnnamedBlocks.hardened_glass;
import static uk.jamierocks.mc.unnamed.init.UnnamedBlocks.tungsten_block;
import static uk.jamierocks.mc.unnamed.init.UnnamedBlocks.tungsten_ore;
import static uk.jamierocks.mc.unnamed.init.UnnamedItems.tungsten;
import static uk.jamierocks.mc.unnamed.init.UnnamedItems.tungsten_carbide;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.oredict.OreDictionary;
import uk.jamierocks.mc.unnamed.block.ExpDropBehaviour;
import uk.jamierocks.mc.unnamed.block.ItemDropBehaviour;
import uk.jamierocks.mc.unnamed.block.Presets;
import uk.jamierocks.mc.unnamed.block.UnnamedBlock;
import uk.jamierocks.mc.unnamed.item.UnnamedItem;
import uk.jamierocks.mc.unnamed.proxy.IProxy;
import uk.jamierocks.mc.unnamed.util.Constants;
import uk.jamierocks.mc.unnamed.util.CreativeTabHelper;

@Mod(modid = Constants.MOD_ID, name = "Unnamed")
@Mod.EventBusSubscriber
public final class UnnamedMod {

    @SidedProxy(
            clientSide = "uk.jamierocks.mc.unnamed.client.proxy.ClientProxy",
            serverSide = "uk.jamierocks.md.unnamed.proxy.ServerProxy")
    public static IProxy proxy;

    public static CreativeTabs creativeTab = CreativeTabHelper.of(Constants.MOD_ID, () -> tungsten);

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        // tungsten
        registerBlock(event.getRegistry(), UnnamedBlock.builder()
                .preset(Presets.ORE)
                .identifier("tungsten_ore")
                .drop(ItemDropBehaviour.Fortune.of(() -> tungsten, 1, 3))
                .drop(ExpDropBehaviour.of(2, 5))
                .build());
        registerBlock(event.getRegistry(), UnnamedBlock.builder()
                .identifier("tungsten_block")
                .build());

        // hardened stuff
        registerBlock(event.getRegistry(), UnnamedBlock.builder()
                .preset(Presets.GLASS)
                .identifier("hardened_glass")
                .hardness(0.75f) // glass hardness is 0.3, this is 1.5x
                .resistance(10f) // same as stone
                .build());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        // tungsten
        registerItem(event.getRegistry(), new UnnamedItem("tungsten"), 0);
        registerItem(event.getRegistry(), new UnnamedItem("tungsten_carbide"), 0);
        registerItemBlock(event.getRegistry(), tungsten_ore, 0);
        registerItemBlock(event.getRegistry(), tungsten_block, 0);

        // hardened stuff
        registerItemBlock(event.getRegistry(), hardened_glass, 0);
    }

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        // Register items and blocks to the ore dictionary
        ////

        // tungsten
        OreDictionary.registerOre("tungsten", tungsten);
        OreDictionary.registerOre("oreTungsten", tungsten_ore);
        OreDictionary.registerOre("blockTungsten", tungsten_block);

        // World generation
        ////
        GameRegistry.registerWorldGenerator(new UnnamedWorldGenerator(), 0);
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        // Register recipes to the game registry
        ////

        // tungsten
        GameRegistry.addShapedRecipe(new ItemStack(tungsten_block), new String[] { "tt", "tt" }, 't', tungsten);
        GameRegistry.addShapedRecipe(new ItemStack(tungsten, 4), new String[] { "t" }, 't', tungsten_block);
        GameRegistry.addSmelting(tungsten, new ItemStack(tungsten_carbide, 3), 2);

        // hardened stuff
        GameRegistry.addShapedRecipe(new ItemStack(hardened_glass),
                new String[] { "ccc", "cgc", "ccc" }, 'c', tungsten_carbide, 'g', Blocks.GLASS);
    }

    private static void registerBlock(IForgeRegistry<Block> registry, Block block) {
        registry.register(block);
    }

    private static void registerItem(IForgeRegistry<Item> registry, Item item, int itemVariant) {
        registry.register(item);
        proxy.registerItemModel(item, itemVariant);
    }

    private static void registerItemBlock(IForgeRegistry<Item> registry, Block block, int itemVariant) {
        registerItem(registry, new ItemBlock(block).setRegistryName(block.getRegistryName()), itemVariant);
    }

}
