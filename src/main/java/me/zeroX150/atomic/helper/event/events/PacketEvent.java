package me.zeroX150.atomic.helper.event.events;

import me.zeroX150.atomic.helper.event.events.base.Event;
import net.minecraft.network.Packet;

public class PacketEvent extends Event {
    private final Packet<?> packet;

    public PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }
}
