package nin.nishiki.item

import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BrushItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tiers
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BrushableBlock
import net.minecraft.world.level.block.FallingBlock
import net.minecraft.world.level.block.entity.BrushableBlockEntity
import net.minecraftforge.common.Tags
import nin.nishiki.util.*

object CeremonialBrush : BrushItem(Properties().durability(512)) {

    override fun onUseTick(level: Level, entity: LivingEntity, item: ItemStack, duration: Int) {
        if (duration < 0 || entity !is Player) {
            entity.releaseUsingItem()
            return
        }

        val requiredTick = (2 * (5 - item.efficiency)).coerceIn(1, 10)
        if (duration % requiredTick == requiredTick / 2) {
            val hit = entity.blockHit ?: return
            val pos = hit.blockPos
            val blockstate = level.getBlockState(pos)
            val block = blockstate.block
            val tile = level.getBlockEntity(pos)

            if (level.isClientSide) {
                spawnDustParticles(level, hit, blockstate, entity.viewVector, entity.arm)
                val sound = if (block is BrushableBlock) block.brushSound else SoundEvents.BRUSH_GENERIC
                level.playSound(entity, pos, sound, SoundSource.BLOCKS)
            }

            if (level is ServerLevel) {
                if (item.silkTouch && level.isSusSandLike(pos) && level.destroyWithoutUpdate(pos, item, true)
                    || (tile is BrushableBlockEntity && tile.brush(level.gameTime, entity, hit.direction))
                    || (block is FallingBlock && level.destroyWithoutUpdate(pos, item))
                )
                    entity.hurtUsingItem()
            }
        }
    }

    override fun isEnchantable(item: ItemStack) = true

    override fun getEnchantmentValue(item: ItemStack?) = Tiers.GOLD.enchantmentValue

    override fun canApplyAtEnchantingTable(stack: ItemStack?, enchantment: Enchantment?) = super.canApplyAtEnchantingTable(stack, enchantment)
            || enchantment == Enchantments.SILK_TOUCH
            || enchantment == Enchantments.BLOCK_EFFICIENCY

    override fun isValidRepairItem(self: ItemStack, ingredient: ItemStack) = ingredient.`is`(Tags.Items.FEATHERS)
}