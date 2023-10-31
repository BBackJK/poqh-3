package bback.module.poqh3.impl;

import bback.module.poqh3.Column;
import bback.module.poqh3.FunctionColumn;
import bback.module.poqh3.utils.Strings;

class JpqlSupplierColumn extends AbstractPredictorColumn implements FunctionColumn {

    private final FunctionCommand command;

    private String alias;

    public JpqlSupplierColumn(FunctionCommand command) {
        this(command, null);
    }

    public JpqlSupplierColumn(FunctionCommand command, String alias) {
        this.command = command;
        this.alias = alias;
    }

    @Override
    public Column as(String alias) {
        if ( hasAlias() ) {
            return new JpqlSupplierColumn(this.command, alias);
        }
        this.alias = alias;
        return this;
    }

    @Override
    public String getAttr() {
        return hasAlias() ? this.alias : Strings.EMPTY;
    }

    @Override
    public String getAlias() {
        return this.alias;
    }

    @Override
    public boolean hasAlias() {
        return this.alias != null && !this.alias.isEmpty();
    }

    @Override
    public String toQuery() {
        return switch (command) {
            case CURRENT_TIMESTAMP -> "current_timestamp";
            case CURRENT_DATE -> "current_date";
            case CURRENT_TIME -> "current_time";
            default -> null;
        };
    }

    @Override
    public Class<?> getCommandHibernateReturnType() {
        return command.getHibernateReturnType();
    }
}
