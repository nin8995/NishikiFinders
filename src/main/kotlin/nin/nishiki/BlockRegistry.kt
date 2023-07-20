@file:Suppress("HasPlatformType", "unused")

package nin.nishiki

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraftforge.client.model.generators.BlockStateProvider
import net.minecraftforge.common.data.BlockTagsProvider
import net.minecraftforge.common.data.ExistingFileHelper
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import java.util.concurrent.CompletableFuture

object BlockRegistry {

    val BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ID)
    val BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ID)

    init{
        BLOCKS.register(MOD_BUS)
        BLOCK_ITEMS.register(MOD_BUS)
    }

    fun register(name: String, block: () -> Block): RegistryObject<Block> {
        val roBlock = BLOCKS.register(name, block)
        BLOCK_ITEMS.register(name) { BlockItem(roBlock.get(), Item.Properties()) }
        return roBlock
    }

    class Model(output: PackOutput, modid: String, existingFileHelper: ExistingFileHelper) : BlockStateProvider(output, modid, existingFileHelper) {
        override fun registerStatesAndModels() {
            //simpleBlockItem(EXAMPLE_BLOCK.get(), cubeAll(EXAMPLE_BLOCK.get()))
        }
    }

    class Tag(output: PackOutput, lookupItem: CompletableFuture<HolderLookup.Provider>, modid: String, existingFileHelper: ExistingFileHelper) : BlockTagsProvider(output, lookupItem, modid, existingFileHelper) {
        override fun addTags(nu: HolderLookup.Provider) {

        }
    }
}