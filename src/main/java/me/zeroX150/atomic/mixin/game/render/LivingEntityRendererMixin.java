package me.zeroX150.atomic.mixin.game.render;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.helper.Rotations;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @ModifyVariable(method = "render", ordinal = 2, at = @At(value = "STORE", ordinal = 0))
    public float changeYaw(float oldValue, LivingEntity le) {
        if (Rotations.isEnabled() && le.equals(Atomic.client.player)) return Rotations.getClientYaw();
        return oldValue;
    }

    @ModifyVariable(method = "render", ordinal = 3, at = @At(value = "STORE", ordinal = 0))
    public float changeHeadYaw(float oldValue, LivingEntity le) {
        if (le.equals(Atomic.client.player) && Rotations.isEnabled()) return Rotations.getClientYaw();
        return oldValue;
    }

    @ModifyVariable(method = "render", ordinal = 5, at = @At(value = "STORE", ordinal = 3))
    public float changePitch(float oldValue, LivingEntity le) {
        if (le.equals(Atomic.client.player) && Rotations.isEnabled()) return Rotations.getClientPitch();
        return oldValue;
    }

}
