package nin.nishiki

import nin.nishiki.ItemRegistry.CEREMONIAL_BRUSH
import nin.nishiki.ItemRegistry.PARROT_FEATHER
import nin.nishiki.util.shaped
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.world.item.Items.*
import java.util.function.Consumer

class Recipe(output: PackOutput) : RecipeProvider(output) {

    override fun buildRecipes(writer: Consumer<FinishedRecipe>) {
        RecipeCategory.TOOLS.shaped(CEREMONIAL_BRUSH)
            .pattern("#")
            .pattern(".")
            .pattern("|")
            .define('#', PARROT_FEATHER.get())
            .define('.', GOLD_INGOT)
            .define('|', STICK)
            .unlockedBy(getHasName(PARROT_FEATHER.get()), has(PARROT_FEATHER.get()))
            .save(writer)
    }
}