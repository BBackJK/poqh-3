package bback.module.poqh3.impl;

public class Value extends AbstractPredictorColumn {

    private final Object data;
    private final String alias;

    public Value(Object data) {
        this(data, null);
    }

    public Value(Object data, String alias) {
        this.data = data;
        this.alias = alias;
    }

    @Override
    public String toQuery() {
        if (data == null) {
            return null;
        }
        if (data instanceof String) {
            return "'" + this.data + "'";
        }
        return String.format("%s", this.data);
    }

    @Override
    public String getAttr() {
        return hasAlias() ? this.alias : this.data.toString();
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public boolean hasAlias() {
        return this.alias != null && !this.alias.isEmpty();
    }
}
