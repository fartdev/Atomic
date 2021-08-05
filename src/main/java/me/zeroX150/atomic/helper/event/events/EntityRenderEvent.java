package me.zeroX150.atomic.helper.event.events;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class EntityRenderEvent extends RenderEvent {
    Entity target;

    public EntityRenderEvent(MatrixStack stack, Entity e) {
        super(stack);
        this.target = e;
    }

    public Entity getEntity() {
        return target;
    }
}
