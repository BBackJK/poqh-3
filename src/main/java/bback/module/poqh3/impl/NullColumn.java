package bback.module.poqh3.impl;

class NullColumn extends AbstractPredictorColumn {

    private final Class<?> constructorParameterType;

    public NullColumn(Class<?> constructorParameterType) {
        this.constructorParameterType = constructorParameterType;
    }

    @Override
    public String toQuery() {
        return String.format("cast(null as %s)", constructorParameterType.getName());
    }

    @Override
    public String getAttr() {
        return constructorParameterType.getName();
    }

    @Override
    public String getAlias() {
        return constructorParameterType.getName();
    }

    @Override
    public boolean hasAlias() {
        return false;
    }
}
