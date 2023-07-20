package nin.nishiki.mixin;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrushableBlockEntity.class)
public class NoBrushCooldown {

    @Shadow
    private long coolDownEndsAtTick;

    @Inject(method = "brush", at = @At("TAIL"))
    public void noCooldown(long p_277786_, Player p_277520_, Direction p_277424_, CallbackInfoReturnable<Boolean> cir){
        coolDownEndsAtTick = 0;
    }
}
