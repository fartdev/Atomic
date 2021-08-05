package me.zeroX150.atomic.feature.module.impl.world;

import me.zeroX150.atomic.Atomic;
import me.zeroX150.atomic.feature.module.Module;
import me.zeroX150.atomic.feature.module.ModuleType;
import me.zeroX150.atomic.feature.module.config.SliderValue;
import me.zeroX150.atomic.helper.Client;
import me.zeroX150.atomic.helper.Renderer;
import me.zeroX150.atomic.helper.event.EventType;
import me.zeroX150.atomic.helper.event.Events;
import me.zeroX150.atomic.helper.event.events.PacketEvent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class MassFillNuke extends Module {
    SliderValue delay = this.config.create("Delay", 50, 10, 1000, 0);
    Vec3d startPos;
    Vec3d last = null;
    volatile boolean run = false;

    public MassFillNuke() {
        super("MassFillNuke", "Erases your whole render distance one by one [REQUIRES OP]", ModuleType.WORLD);
        Events.registerEventHandler(EventType.PACKET_SEND, event -> {
            if (!this.isEnabled() || run) return;
            PacketEvent pe = (PacketEvent) event;
            if (pe.getPacket() instanceof ChatMessageC2SPacket packet) {
                if (packet.getChatMessage().equalsIgnoreCase("go")) {
                    Client.notifyUser("Alright then. No returning.");
                    run = true;
                    startThread();
                }
            }
        });
    }

    @Override
    public void tick() {
    }

    void execute() {
        for (int y = 245; y > 0; y -= 20) {
            for (int x = -255; x < 255; x += 20) {
                for (int z = -255; z < 255; z += 20) {
                    if (!run) return;
                    if (Atomic.client.player == null || Atomic.client.world == null) {
                        setEnabled(false);
                        return;
                    }
                    Client.sleep((long) (delay.getValue() + 0));
                    Vec3d o = startPos.add(x, 0, z);
                    BlockPos pos = new BlockPos(o.x, y, o.z);
                    last = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
                    String cmd = "/fill " + r(pos.getX() - 10) + " " + MathHelper.clamp(r(pos.getY() - 10), 0, 255) + " " + r(pos.getZ() - 10) + " " + r(pos.getX() + 10) + " " + r(pos.getY() + 10) + " " + r(pos.getZ() + 10) + " " + "minecraft:air";
                    Atomic.client.player.sendChatMessage(cmd);
                }
            }
        }
        setEnabled(false);

    }

    int r(double v) {
        return (int) Math.round(v);
    }

    void startThread() {
        new Thread(this::execute).start();
    }

    @Override
    public void enable() {
        startPos = Atomic.client.player.getPos();
        Client.notifyUser("THIS WILL ABSOLUTELY DESTROY EVERYTHING, AND YOU NEED OP");
        Client.notifyUser("SEND \"GO\" INTO CHAT TO CONTINUE. THIS WILL BE DANGEROUS");
        Client.notifyUser("MAKE SURE YOUR RENDER DISTANCE IS ALL THE WAY UP AND EVERYTHING IS LOADED");
    }

    @Override
    public void disable() {
        run = false;
    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {
        if (last != null) {
            Vec3d origin = last.subtract(0.5, 0.5, 0.5);
            Renderer.renderFilled(origin, new Vec3d(1, 1, 1), Client.getCurrentRGB(), matrices);
            Renderer.line(origin.add(0.5, 0.5, 0.5).subtract(10, 0, 0), origin.add(0.5, 0.5, 0.5).add(10, 0, 0), Color.RED, matrices);
            Renderer.line(origin.add(0.5, 0.5, 0.5).subtract(0, 0, 10), origin.add(0.5, 0.5, 0.5).add(0, 0, 10), Color.GREEN, matrices);
            Renderer.line(origin.add(0.5, 0.5, 0.5).subtract(0, 10, 0), origin.add(0.5, 0.5, 0.5).add(0, 10, 0), Color.BLUE, matrices);
        }
    }

    @Override
    public void onHudRender() {

    }
}