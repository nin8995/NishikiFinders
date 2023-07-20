@file:Suppress("HasPlatformType", "unused")

package nin.nishiki

import nin.nishiki.item.CeremonialBrush
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraftforge.client.model.generators.ItemModelProvider
import net.minecraftforge.common.Tags.Items.FEATHERS
import net.minecraftforge.common.data.ExistingFileHelper
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import java.util.concurrent.CompletableFuture

object ItemRegistry {

    val ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ID)

    val CEREMONIAL_BRUSH = register("ceremonial_brush") { CeremonialBrush }
    val PARROT_FEATHER = register("parrot_feather") { Item(Item.Properties()) }

    init {
        ITEMS.register(MOD_BUS)
    }

    fun register(name: String, item: () -> Item) = ITEMS.register(name, item)

    class Model(output: PackOutput, modid: String, existingFileHelper: ExistingFileHelper) : ItemModelProvider(output, modid, existingFileHelper) {
        override fun registerModels() = ITEMS.entries.forEach { basicItem(it.get()) }
    }

    class Tag(output: PackOutput, lookupItem: CompletableFuture<HolderLookup.Provider>, lookupBlock: CompletableFuture<TagLookup<Block>>, modid: String, existingFileHelper: ExistingFileHelper) : ItemTagsProvider(output, lookupItem, lookupBlock, modid, existingFileHelper) {
        override fun addTags(nu: HolderLookup.Provider) {
            tag(FEATHERS).add(PARROT_FEATHER.get())
        }
    }
}