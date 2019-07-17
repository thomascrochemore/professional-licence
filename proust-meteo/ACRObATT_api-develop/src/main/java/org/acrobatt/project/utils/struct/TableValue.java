package org.acrobatt.project.utils.struct;

import java.util.Objects;

/**
 * A generic object used for JSON mapping. This one contains a value and its unit
 * @param <V> the value
 * @param <U> the unit
 */
public class TableValue<V, U> {
    private final V valueValue;
    private final U unitValue;

    public TableValue(V valueKey, U unitKey) {
        this.valueValue = valueKey;
        this.unitValue = unitKey;
    }

    public V getValueValue() { return valueValue; }
    public U getUnitValue() { return unitValue; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableValue<?, ?> tableValue = (TableValue<?, ?>) o;
        return Objects.equals(valueValue, tableValue.valueValue) &&
                Objects.equals(unitValue, tableValue.unitValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valueValue, unitValue);
    }

    @Override
    public String toString() {
        return String.format("TableValue{%1s, %2s}", valueValue, unitValue);
    }
}
