package me.zeroX150.atomic.feature.module.impl.render;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.gui.ImGuiScreen;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import net.minecraft.client.util.math.MatrixStack;

public class ImGUI extends Module {
    public ImGUI() {
        super("ImGUI", "yo pog", ModuleType.RENDER);
    }

    @Override
    public void tick() {
        if (!(Atomic.client.currentScreen instanceof ImGuiScreen)) {
            if (ImGuiScreen.INSTANCE == null)
                new ImGuiScreen();
            Atomic.client.openScreen(ImGuiScreen.INSTANCE);
        } else setEnabled(false);
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

    }

    @Override
    public void onHudRender() {

    }
}
