package bback.module.poqh3.impl;

import bback.module.poqh3.Column;
import bback.module.poqh3.FunctionColumn;
import bback.module.poqh3.utils.Strings;

class JpqlFunctionalColumn extends AbstractPredictorColumn implements FunctionColumn {

    private final FunctionCommand command;
    private final Column input;
    private String alias;

    public JpqlFunctionalColumn(FunctionCommand command, Column input) {
        this(command, input, null);
    }

    public JpqlFunctionalColumn(FunctionCommand command, Column input, String alias) {
        this.command = command;
        this.input = input;
        this.alias = alias;
    }


    @Override
    public Column as(String alias) {
        if ( hasAlias() ) {
            return new JpqlFunctionalColumn(this.command, this.input, alias);
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
            case YEAR:
                StringBuilder year = new StringBuilder("year(");
                year.append(input.toQuery());
                year.append(")");
                yield year.toString();
            case MONTH:
                StringBuilder month = new StringBuilder("month(");
                month.append(input.toQuery());
                month.append(")");
                yield month.toString();
            case DAY:
                StringBuilder day = new StringBuilder("day(");
                day.append(input.toQuery());
                day.append(")");
                yield day.toString();
            case HOUR:
                StringBuilder hour = new StringBuilder("hour(");
                hour.append(input.toQuery());
                hour.append(")");
                yield hour.toString();
            case MINUTE:
                StringBuilder minute = new StringBuilder("minute(");
                minute.append(input.toQuery());
                minute.append(")");
                yield minute.toString();
            case SECOND:
                StringBuilder sec = new StringBuilder("sec(");
                sec.append(input.toQuery());
                sec.append(")");
                yield sec.toString();
            case SUM:
                StringBuilder sum = new StringBuilder("sum(");
                sum.append(input.toQuery());
                sum.append(")");
                yield sum.toString();
            case MIN:
                StringBuilder min = new StringBuilder("min(");
                min.append(input.toQuery());
                min.append(")");
                yield min.toString();
            case MAX:
                StringBuilder max = new StringBuilder("max(");
                max.append(input.toQuery());
                max.append(")");
                yield max.toString();
            case AVG:
                StringBuilder avg = new StringBuilder("avg(");
                avg.append(input.toQuery());
                avg.append(")");
                yield avg.toString();
            case COUNT:
                StringBuilder count = new StringBuilder("count(");
                count.append(input.toQuery());
                count.append(")");
                yield count.toString();
            case TRIM:
                StringBuilder trim = new StringBuilder("trim(");
                trim.append(input.toQuery());
                trim.append(")");
                yield trim.toString();
            case LOWER:
                StringBuilder lower = new StringBuilder("lower(");
                lower.append(input.toQuery());
                lower.append(")");
                yield lower.toString();
            case UPPER:
                StringBuilder upper = new StringBuilder("upper(");
                upper.append(input.toQuery());
                upper.append(")");
                yield upper.toString();
            case LENGTH:
                StringBuilder length = new StringBuilder("length(");
                length.append(input.toQuery());
                length.append(")");
                yield length.toString();
            default:
                yield null;
        };
    }

    @Override
    public Class<?> getCommandHibernateReturnType() {
        return command.getHibernateReturnType();
    }
}
