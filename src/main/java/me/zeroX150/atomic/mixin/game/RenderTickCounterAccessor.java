package me.zeroX150.atomic.mixin.game;

import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderTickCounter.class)
public interface RenderTickCounterAccessor {
    @Accessor("tickTime")
    float getTickTime();

    @Mutable
    @Accessor("tickTime")
    void setTickTime(float v);
}
