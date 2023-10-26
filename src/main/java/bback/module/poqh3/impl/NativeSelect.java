package bback.module.poqh3.impl;

import bback.module.poqh3.Column;
import bback.module.poqh3.Select;

import java.util.List;


public class NativeSelect implements Select {

    private final List<Column> selectColumnList;

    public NativeSelect(List<Column> selectColumnList) {
        this.selectColumnList = selectColumnList;
    }

    @Override
    public String toQuery() {
        StringBuilder sb = new StringBuilder(" select ");
        int columnCount = selectColumnList.size();
        int n=1;
        for (int i=0; i<columnCount; i++, n++) {
            boolean isLast = n == columnCount;
            Column column = selectColumnList.get(i);
            sb.append(column.toQuery());
            if (column.hasAlias()) {
                sb.append(" as ");
                sb.append(column.getAttr());
            }
            if (!isLast) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
