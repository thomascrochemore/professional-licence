package org.acrobatt.project.utils.struct;

import java.util.Objects;

/**
 * A generic object used for JSON mapping. This one contains the row and column headers
 * @param <R> the row header type
 * @param <C> the column header type
 */
public class TableKey<R, C> {

    private final R rowKey;
    private final C columnKey;

    public TableKey(R rowKey, C columnKey) {
        this.rowKey = rowKey;
        this.columnKey = columnKey;
    }

    public R getRowKey() { return rowKey; }
    public C getColumnKey() { return columnKey; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableKey<?, ?> tableKey = (TableKey<?, ?>) o;
        return Objects.equals(rowKey, tableKey.rowKey) &&
                Objects.equals(columnKey, tableKey.columnKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowKey, columnKey);
    }

    @Override
    public String toString() {
        return String.format("TableKey{%1s, %2s}", rowKey, columnKey);
    }
}
