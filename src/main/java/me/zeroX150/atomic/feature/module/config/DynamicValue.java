package me.zeroX150.atomic.feature.module.config;

import java.util.ArrayList;
import java.util.List;

public class DynamicValue<T> {
    public final List<SelectorRunnable> selectors = new ArrayList<>();
    private final List<Runnable> onChangedListeners = new ArrayList<>();
    private final String key;
    protected T value;
    private String description = "";

    public DynamicValue(String key, T value) {
        this.key = key;
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @SuppressWarnings("unchecked") // fucking monkey shut up
    public void setValue(Object value) {
        if (getType() != value.getClass()) return;
        this.value = (T) value;
        onValueChanged();
    }

    protected void onValueChanged() {
        for (Runnable onChangedListener : onChangedListeners) {
            onChangedListener.run();
        }
    }

    public void addChangeListener(Runnable r) {
        onChangedListeners.add(r);
    }

    public String getKey() {
        return key;
    }

    public Class<?> getType() {
        return value.getClass();
    }

    public boolean shouldShow() {
        for (SelectorRunnable selector : selectors) {
            if (!selector.shouldShow()) return false;
        }
        return true;
    }

    public DynamicValue<T> description(String description) {
        this.description = description;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void showOnlyIf(SelectorRunnable runnable) {
        selectors.add(runnable);
    }

    public interface SelectorRunnable {
        boolean shouldShow();
    }

}
