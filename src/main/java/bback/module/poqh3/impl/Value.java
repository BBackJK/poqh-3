package bback.module.poqh3.impl;

public class Value extends AbstractPredictorColumn {

    private final Object value;
    private String alias;

    public Value(Object value) {
        this(value, null);
    }

    public Value(Object value, String alias) {
        this.value = value;
        this.alias = alias;
    }

    @Override
    public String toQuery() {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return "'" + this.value + "'";
        }
        return String.format("%s", this.value);
    }

    @Override
    public String getAttr() {
        return hasAlias() ? this.alias : this.value.toString();
    }

    @Override
    public boolean hasAlias() {
        return this.alias != null && !this.alias.isEmpty();
    }
}
