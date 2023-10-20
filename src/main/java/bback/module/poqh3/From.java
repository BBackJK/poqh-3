package bback.module.poqh3;


import jakarta.persistence.criteria.JoinType;

public interface From extends Native, JPQL {

    Join JOIN(Table joinTable, JoinType joinType);

    default Join JOIN(Table joinTable) {
        return JOIN(joinTable, JoinType.INNER);
    }

    Table getRoot();

    boolean isJpql();
}
