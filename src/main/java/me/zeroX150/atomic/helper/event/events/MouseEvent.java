package me.zeroX150.atomic.helper.event.events;

import me.zeroX150.atomic.helper.event.events.base.NonCancellableEvent;

public class MouseEvent extends NonCancellableEvent {
    int button;
    MouseEventType type;

    public MouseEvent(int button, int action) {
        this.button = button;
        type = action == 1 ? MouseEventType.MOUSE_CLICKED : MouseEventType.MOUSE_RELEASED;
    }

    public int getButton() {
        return button;
    }

    public MouseEventType getAction() {
        return type;
    }

    public enum MouseEventType {
        MOUSE_CLICKED, MOUSE_RELEASED
    }
}
