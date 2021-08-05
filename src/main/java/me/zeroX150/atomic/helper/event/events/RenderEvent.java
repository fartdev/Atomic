package me.zeroX150.atomic.helper.event.events;

import me.zeroX150.atomic.helper.event.events.base.Event;
import net.minecraft.client.util.math.MatrixStack;

public class RenderEvent extends Event {
    MatrixStack stack;

    public RenderEvent(MatrixStack stack) {
        this.stack = stack;
    }

    public MatrixStack getStack() {
        return stack;
    }
}
