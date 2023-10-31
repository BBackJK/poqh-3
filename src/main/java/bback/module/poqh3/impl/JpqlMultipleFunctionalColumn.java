package bback.module.poqh3.impl;

import bback.module.poqh3.Column;
import bback.module.poqh3.FunctionColumn;
import bback.module.poqh3.utils.Strings;

import java.util.Arrays;
import java.util.List;

class JpqlMultipleFunctionalColumn extends AbstractPredictorColumn implements FunctionColumn {

    private final FunctionCommand command;
    private final List<Column> inputs;
    private String alias;

    public JpqlMultipleFunctionalColumn(FunctionCommand command, Column... inputs) {
        this(command, Arrays.asList(inputs), null);
    }

    public JpqlMultipleFunctionalColumn(FunctionCommand command, List<Column> inputList, String alias) {
        this.command = command;
        this.inputs = inputList;
        this.alias = alias;
    }


    @Override
    public Column as(String alias) {
        if ( hasAlias() ) {
            return new JpqlMultipleFunctionalColumn(this.command, this.inputs, alias);
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
        int inputCount = this.inputs.size();
        if (inputCount < 1) {
            return Strings.EMPTY;
        }
        int n=1;
        return switch (command) {
            case CONCAT:
                StringBuilder concat = new StringBuilder("concat(");
                for (int i=0;i<inputCount;i++, n++) {
                    boolean isLast = n == inputCount;
                    Column input = this.inputs.get(i);
                    concat.append(input.toQuery());

                    if ( !isLast ) concat.append(", ");
                }
                concat.append(")");
                yield concat.toString();
            case SUBSTRING:
                StringBuilder substr = new StringBuilder("substring(");
                for (int i=0;i<inputCount;i++, n++) {
                    boolean isLast = n == inputCount;
                    Column input = this.inputs.get(i);
                    substr.append(input.toQuery());

                    if ( !isLast ) substr.append(", ");
                }
                substr.append(")");
                yield substr.toString();
            default:
                yield null;
        };
    }

    @Override
    public Class<?> getCommandHibernateReturnType() {
        return command.getHibernateReturnType();
    }
}
