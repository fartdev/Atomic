package me.zeroX150.atomic.feature.module.impl.world;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.helper.Transitions;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class Fullbright extends Module {
    double og;

    public Fullbright() {
        super("Fullbright", "shine bright like a diamond", ModuleType.RENDER);
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {
        og = MathHelper.clamp(Atomic.client.options.gamma, 0, 1);
    }

    @Override
    public void disable() {
        Atomic.client.options.gamma = og;
    }

    @Override
    public void onFastTick() {
        Atomic.client.options.gamma = Transitions.transition(Atomic.client.options.gamma, 10, 300);
    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }
}

