package me.zeroX150.atomic.feature.module.config;

import net.minecraft.util.math.MathHelper;

public class SliderValue extends DynamicValue<Double> {
    final int prec;
    double min;
    double max;

    public SliderValue(String key, double value, double min, double max, int precision) {
        super(key, value);
        this.min = min;
        this.max = max;
        this.prec = MathHelper.clamp(precision, 0, 10);
    }

    @Override
    public void setValue(Object value) {
        if (!(value instanceof Double)) return;
        this.value = MathHelper.clamp((double) value, min, max);

        onValueChanged();
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public int getPrec() {
        return prec;
    }
}
