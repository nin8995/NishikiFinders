package nin.nishiki.mixin;

import nin.nishiki.ItemRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Parrot.class)
public abstract class DropParrotFeather extends Animal {

    @Shadow
    public float flapSpeed;

    protected DropParrotFeather(EntityType<? extends Animal> p_27557_, Level p_27558_) {
        super(p_27557_, p_27558_);
    }

    public int featherTime = calcFeatherTime();

    public void dropFeather() {
        playSound(SoundEvents.PARROT_FLY, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
        spawnAtLocation(ItemRegistry.INSTANCE.getPARROT_FEATHER().get());
        gameEvent(GameEvent.ENTITY_PLACE);
        featherTime = calcFeatherTime();
    }

    public int calcFeatherTime() {
        return (int) (random.nextGaussian() * 6000) + 6000;
    }

    @Inject(method = "aiStep", at = @At("TAIL"))
    public void featherLoss(CallbackInfo ci) {
        if (level().isClientSide) return;

        if (--featherTime <= 0 || random.nextFloat() < flapSpeed / 500)
            dropFeather();
    }

    @Inject(method = "hurt", at = @At("TAIL"))
    public void tearAway(DamageSource source, float p_29379_, CallbackInfoReturnable<Boolean> cir){
        if(!level().isClientSide && !isDeadOrDying() && source.getDirectEntity() != null && !source.isIndirect() && random.nextFloat() < 0.2)
            dropFeather();
    }
}
