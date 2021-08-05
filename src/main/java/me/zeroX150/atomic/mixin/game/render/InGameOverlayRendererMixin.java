package me.zeroX150.atomic.mixin.game.render;

import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleRegistry;
import me.zeroX150.atomic.feature.module.impl.external.NoRender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {
    private static final Module noRender = ModuleRegistry.getByClass(NoRender.class);

    @Inject(method = "renderUnderwaterOverlay", at = @At("HEAD"), cancellable = true)
    private static void cancelRenderWater(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        if (noRender.isEnabled() && NoRender.waterOverlay.getValue()) ci.cancel();
    }

    @Inject(method = "renderFireOverlay", at = @At("HEAD"), cancellable = true)
    private static void cancelRenderFireOverlay(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        if (noRender.isEnabled() && NoRender.fire.getValue()) ci.cancel();
    }
}
