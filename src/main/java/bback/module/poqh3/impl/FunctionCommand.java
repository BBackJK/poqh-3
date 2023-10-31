package bback.module.poqh3.impl;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

enum FunctionCommand {

    // DateTime
    CURRENT_DATE(Date.class)
    , CURRENT_TIME(Time.class)
    , CURRENT_TIMESTAMP(Timestamp.class)
    , YEAR(Integer.class), MONTH(Integer.class), DAY(Integer.class), HOUR(Integer.class), MINUTE(Integer.class), SECOND(Integer.class)

    // aggregation
    , SUM(Integer.class), MIN(Integer.class), MAX(Integer.class), AVG(Double.class), COUNT(Long.class)

    // string
    , CONCAT(String.class), SUBSTRING(String.class), TRIM(String.class), LOWER(String.class), UPPER(String.class), LENGTH(String.class)

    // math
    , ABS(Double.class), SQRT(Double.class), MOD(Double.class)
    ;


    private final Class<?> hibernateReturnType;

    FunctionCommand(Class<?> hibernateReturnType) {
        this.hibernateReturnType = hibernateReturnType;
    }

    public Class<?> getHibernateReturnType() {
        return this.hibernateReturnType;
    }

}
