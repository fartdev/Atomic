package me.zeroX150.atomic.helper.event.events;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.util.math.MatrixStack;

public class BlockEntityRenderEvent extends RenderEvent {
    BlockEntity entity;

    public BlockEntityRenderEvent(MatrixStack stack, BlockEntity entity) {
        super(stack);
        this.entity = entity;
    }

    public BlockEntity getBlockEntity() {
        return entity;
    }
}
