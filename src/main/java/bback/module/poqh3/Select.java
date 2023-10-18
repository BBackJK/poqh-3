package bback.module.poqh3;

import java.util.List;

public interface Select extends Native, JPQL {

    void setSelectColumnList(List<Column> columnList);
}
