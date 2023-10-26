package bback.module.poqh3.impl;

import bback.module.poqh3.Column;
import bback.module.poqh3.SQLContext;
import bback.module.poqh3.Table;
import bback.module.poqh3.exceptions.DMLValidationException;
import bback.module.poqh3.utils.Objects;

import java.util.List;

public class ContextTable<T extends SQLContext<?>> implements Table<T> {

    private final T context;
    private String alias;

    public ContextTable(T context, String alias) {
        if ( Objects.isEmpty( alias ) ) {
            throw new DMLValidationException(" Context Table Is Required Alias. ");
        }
        if ( context.isJpql() ) {
            throw new DMLValidationException(" Context Table Is Only Supported Native. ");
        }
        this.context = context;
        this.alias = alias;
    }

    @Override
    public String toQuery() {
        String query = context.toQuery();
        String[] queries = query.split("\n");
        StringBuilder sb = new StringBuilder(" ");
        sb.append("(");
        sb.append("\n");
        for (String q : queries) {
            sb.append("\t");
            sb.append(q);
            sb.append("\n");
        }
        sb.append(" ");
        sb.append(")");
        return sb.toString();
    }

    @Override
    public void AS(String alias) {
        this.alias = alias;
    }

    @Override
    public void AS(int tableIndex) {
        this.alias = String.format("c%d", tableIndex);
    }

    @Override
    public Column COLUMN(String field, String alias) {
        return new NativeColumn<>(this, field, alias);
    }

    @Override
    public Column[] COLUMNS(String... fields) {
        int attrCount = fields.length;
        Column[] result = new Column[attrCount];
        for (int i=0; i<attrCount; i++) {
            result[i] = COLUMN(fields[i]);
        }
        return result;
    }

    @Override
    public Column[] ALL() {
        List<Column> columnList = this.context.getSelectColumnList();
        int columnCount = columnList.size();
        Column[] columns = new Column[columnCount];
        for (int i = 0; i < columnCount; i++) {
            Column column = columnList.get(i);
            columns[i] = new NativeColumn<>(this, column.getAttr(), column.getAlias());
        }

        return columns;
    }

    @Override
    public boolean hasAlias() {
        return this.alias != null && !this.alias.isEmpty();
    }

    @Override
    public String getAlias() {
        return this.alias;
    }

    @Override
    public Class<T> getEntityType() {
        return (Class<T>) this.context.getClass();
    }

    @Override
    public boolean isJpql() {
        return false;
    }
}
