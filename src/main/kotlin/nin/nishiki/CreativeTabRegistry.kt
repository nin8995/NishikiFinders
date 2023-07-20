@file:Suppress("HasPlatformType")

package nin.nishiki

import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraftforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object CreativeTabRegistry {

    val TABS = DeferredRegister.create<CreativeModeTab>(Registries.CREATIVE_MODE_TAB.location(), ID)
    val MOD_TAB = TABS.register(ID){
        CreativeModeTab.builder()
            .icon { ItemRegistry.CEREMONIAL_BRUSH.get().defaultInstance }
            .title(Component.translatable("itemGroup.$ID"))
            .displayItems { _, output ->
                BlockRegistry.BLOCKS.entries.forEach { output.accept(it.get()) }
                ItemRegistry.ITEMS.entries.forEach { output.accept(it.get()) }
            }
            .build()
    }

    init{
        TABS.register(MOD_BUS)
    }
}