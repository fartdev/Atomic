package me.zeroX150.atomic.feature.module.impl.render;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.BooleanValue;
import me.zeroX150.atomic.helper.Client;
import me.zeroX150.atomic.helper.Renderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class ESP extends Module {
    final BooleanValue entities = (BooleanValue) this.config.create("Show Entities", false).description("Whether or not to show entities");
    final BooleanValue players = (BooleanValue) this.config.create("Show Players", true).description("Whether or not to show players");

    public ESP() {
        super("ESP", "shows where shit is but its the walmart version", ModuleType.RENDER);
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        if (Atomic.client.world == null || Atomic.client.player == null) return;
        for (Entity entity : Atomic.client.world.getEntities()) {
            if (entity.squaredDistanceTo(Atomic.client.player) > 4096) continue;
            if (entity.getUuid().equals(Atomic.client.player.getUuid())) continue;
            if (((entity instanceof PlayerEntity && players.getValue()) || entities.getValue())) {
                Renderer.renderFilled(entity.getPos().subtract(new Vec3d(entity.getWidth(), 0, entity.getWidth()).multiply(0.5)), new Vec3d(entity.getWidth(), entity.getHeight(), entity.getWidth()), Renderer.modify(Client.getCurrentRGB(), -1, -1, -1, 130), matrices);
            }
        }
    }

    @Override
    public void onHudRender() {

    }
}

