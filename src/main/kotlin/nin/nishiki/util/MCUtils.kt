package nin.nishiki.util

import net.minecraft.core.BlockPos
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionHand.MAIN_HAND
import net.minecraft.world.InteractionHand.OFF_HAND
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseFireBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.level.material.PushReaction
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3
import net.minecraftforge.registries.RegistryObject

fun Level.getBlock(pos: BlockPos): Block = getBlockState(pos).block

val Player.blockHit
    get(): BlockHitResult? = ProjectileUtil.getHitResultOnViewVector(this, { true }, blockReach) as? BlockHitResult

val LivingEntity.hand: InteractionHand
    get() = usedItemHand

val Entity.viewVector: Vec3
    get() = getViewVector(0F)

val LivingEntity.slot
    get() = when (hand) {
        MAIN_HAND -> EquipmentSlot.MAINHAND
        OFF_HAND -> EquipmentSlot.OFFHAND
    }

val LivingEntity.arm: HumanoidArm
    get() = when (hand) {
        MAIN_HAND -> mainArm
        OFF_HAND -> mainArm.opposite
    }

fun LivingEntity.hurtUsingItem() = useItem.hurtAndBreak(1, this) { it.broadcastBreakEvent(slot) }

fun ServerLevel.destroyWithoutUpdate(pos: BlockPos, tool: ItemStack = ItemStack.EMPTY, dropItself: Boolean = false): Boolean {
    val blockstate = getBlockState(pos)
    val fluidstate = getFluidState(pos)

    if (blockstate.block !is BaseFireBlock) levelEvent(2001, pos, Block.getId(blockstate))

    if (dropItself) dropItself(pos) else dropBlock(pos, tool)

    gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(null, blockstate))
    return setBlock(pos, fluidstate.createLegacyBlock(), 2, 0)
}

fun ServerLevel.dropItself(pos: BlockPos) {
    val item = getBlockState(pos).asItemStack
    getBlockEntity(pos)?.storeData(item)
    dropItem(item, pos)
}

val BlockState.asItemStack: ItemStack
    get() = block.asItem().defaultInstance

fun ServerLevel.getDrops(pos: BlockPos, tool: ItemStack = ItemStack.EMPTY, entity: Entity? = null): MutableList<ItemStack> =
    Block.getDrops(getBlockState(pos), this, pos, getBlockEntity(pos), entity, tool)

fun List<ItemStack>.contains(blockState: BlockState) = contains(blockState.block)

fun List<ItemStack>.contains(item: ItemLike) = stream().anyMatch { it.`is`(item.asItem()) }

fun BlockEntity.storeData(item: ItemStack) =
    BlockItem.setBlockEntityData(item, type, saveWithoutMetadata())

fun ServerLevel.dropItem(item: ItemStack, pos: BlockPos) = Block.popResource(this, pos, item)

fun ServerLevel.dropBlock(pos: BlockPos, item: ItemStack = ItemStack.EMPTY, entity: Entity? = null) =
    Block.dropResources(getBlockState(pos), this, pos, getBlockEntity(pos), entity, item)

fun ServerLevel.isSusSandLike(pos: BlockPos) =
    !canDropItself(pos) && getBlockState(pos).pistonPushReaction == PushReaction.DESTROY

val silkTouchTool: ItemStack = Items.NETHERITE_PICKAXE.defaultInstance.withEnchant(Enchantments.SILK_TOUCH, 1)
fun ServerLevel.canDropItself(pos: BlockPos) = getDrops(pos, silkTouchTool).contains(getBlockState(pos))

fun ItemStack.isEnchanted(enchantment: Enchantment) = getEnchantmentLevel(enchantment) > 0

fun ItemStack.getParabolicRequiredTick(enchantment: Enchantment, base: Int) =
    (base * (1 - getEnchantmentLevel(enchantment).toFloat() / enchantment.maxLevel).sqr).toInt().coerceIn(1, base)

fun RecipeCategory.shaped(itemLike: RegistryObject<out ItemLike>): ShapedRecipeBuilder = ShapedRecipeBuilder.shaped(this, itemLike.get())

val ItemStack.silkTouch
    get() = isEnchanted(Enchantments.SILK_TOUCH)

val ItemStack.efficiency
    get() = getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY)

fun ItemStack.withEnchant(enchantment: Enchantment, i: Int): ItemStack {
    enchant(enchantment, i)
    return this
}