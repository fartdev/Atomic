package me.zeroX150.atomic.feature.module.impl.render;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.BooleanValue;
import me.zeroX150.atomic.feature.module.config.DynamicValue;
import me.zeroX150.atomic.helper.Rotations;
import me.zeroX150.atomic.helper.keybind.Keybind;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.math.MatrixStack;

public class FreeLook extends Module {
    final BooleanValue hold = (BooleanValue) this.config.create("Hold", true).description("Whether or not to disable the module when the keybind is unpressed");
    Perspective before = Perspective.FIRST_PERSON;
    float ey, ep;
    Keybind kb;

    public FreeLook() {
        super("FreeLook", "looks around yourself without you looking", ModuleType.MISC);
    }

    @Override
    public void tick() {
        if (kb == null) return;
        if (!kb.isHeld() && hold.getValue()) this.setEnabled(false);
        Rotations.setClientPitch(ep);
        Rotations.setClientYaw(ey);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void enable() {
        kb = new Keybind(((DynamicValue<Integer>) this.config.get("Keybind")).getValue());
        before = Atomic.client.options.getPerspective();
        ey = Atomic.client.player.getYaw();
        ep = Atomic.client.player.getPitch();
    }

    @Override
    public void disable() {
        Atomic.client.options.setPerspective(before);
        Atomic.client.player.setYaw(ey);
        Atomic.client.player.setPitch(ep);
    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        Atomic.client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
    }

    @Override
    public void onHudRender() {

    }
}

