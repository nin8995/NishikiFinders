package nin.nishiki

import net.minecraftforge.data.event.GatherDataEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.forge.MOD_BUS

const val ID = "nishiki"

@Mod(ID)
object NishikiMod {

    val LOGGER: Logger = LogManager.getLogger(ID)

    init {
        LOGGER.log(Level.INFO, "$ID has started!")

        ItemRegistry
        BlockRegistry
        CreativeTabRegistry

        MOD_BUS.register(this)
    }

    @SubscribeEvent
    fun onClientSetup(event: FMLClientSetupEvent) {
        LOGGER.log(Level.INFO, "Initializing client... with ExampleMod!")
    }

    @SubscribeEvent
    fun onDataGen(event: GatherDataEvent) {
        val data = event.generator
        val output = data.packOutput
        val lookup = event.lookupProvider

        data.addProvider(event.includeClient(), BlockRegistry.Model(output, ID, event.existingFileHelper))
        val b = BlockRegistry.Tag(output, lookup, ID, event.existingFileHelper);
        data.addProvider(event.includeClient(), b)

        data.addProvider(event.includeClient(), ItemRegistry.Model(output, ID, event.existingFileHelper))
        data.addProvider(event.includeClient(), ItemRegistry.Tag(output, lookup, b.contentsGetter(), ID, event.existingFileHelper))

        data.addProvider(event.includeClient(), Recipe(output))
        System.out.println(
            """
            aaaaaaaaaaaaaaaaa!



























            a!
        """.trimIndent()
        )
    }
}