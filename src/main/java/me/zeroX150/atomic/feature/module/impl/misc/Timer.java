package me.zeroX150.atomic.feature.module.impl.misc;

import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import me.zeroX150.atomic.helper.Client;
import net.minecraft.client.util.math.MatrixStack;

public class Timer extends Module {
    SliderValue newTps = this.config.create("New TPS", 20, 0.1, 100, 1);

    public Timer() {
        super("Timer", "changes client side tps", ModuleType.MISC);
    }

    @Override
    public void tick() {
        Client.setClientTps((float) (newTps.getValue() + 0d));
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {
        Client.setClientTps(20f);
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

