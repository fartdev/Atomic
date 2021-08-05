package me.zeroX150.atomic.helper.event.events.base;

public class NonCancellableEvent extends Event {
    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        throw new IllegalStateException("Event cannot be cancelled.");
    }
}
