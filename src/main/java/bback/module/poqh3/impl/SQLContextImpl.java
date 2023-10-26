package bback.module.poqh3.impl;

import bback.module.poqh3.*;
import bback.module.poqh3.Column;
import bback.module.poqh3.Table;
import bback.module.poqh3.logger.Log;
import bback.module.poqh3.logger.LogFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SQLContextImpl<T> implements SQLContext<T> {

    private static final Log LOGGER = LogFactory.getLog(SQLContext.class);

    private final EntityManager entityManager;
    private final ObjectMapper om;
    private final List<Column> selectColumnList = new ArrayList<>();
    private final List<Predictor> whereList = new ArrayList<>();
    private final List<Order> orderList = new ArrayList<>();
    private final List<Column> groupByList = new ArrayList<>();
    private From<T> from;
    private boolean isJpql;

    public SQLContextImpl(EntityManager entityManager, ObjectMapper om) {
        this.entityManager = entityManager;
        this.om = om;
    }

    @Override
    public String toQuery() {
        StringBuilder sb = new StringBuilder();

        if (hasTable()) {
            sb.append(from.toQuery());
            sb.append("\n");
        }

        if (hasWhere()) {
            sb.append(" where ");
            this.whereList.forEach(w -> {
                sb.append(w.toQuery());
                sb.append("\n");
            });
        }

        if (hasOrder()) {
            sb.append(" order by ");
            int orderCount = this.orderList.size();
            int n = 1;
            for (int i=0; i<orderCount;i++,n++) {
                boolean isLast = n == orderCount;
                Order order = this.orderList.get(i);
                sb.append(order.toQuery());
                if ( !isLast ) sb.append(", ");
            }
            sb.append("\n");
        }

        if ( hasGroupBy() ) {
            sb.append(" group by ");
            int groupCount = this.groupByList.size();
            int n = 1;
            for (int i=0; i<groupCount;i++,n++) {
                boolean isLast = n == groupCount;
                Column column = this.groupByList.get(i);
                sb.append(column.toQuery());
                if ( !isLast ) sb.append(", ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }



    @Override
    public From<T> FROM(Table<T> table) {
        if (!table.hasAlias()) {
            table.AS(1);
        }
        this.isJpql = table.isJpql();
        this.from = new FromImpl<>(table);
        return this.from;
    }

    @Override
    public void SELECT(Column... columns) {
        this.selectColumnList.addAll(Arrays.stream(columns).collect(Collectors.toList()));
    }

    @Override
    public void WHERE(Predictor... predictors) {
        this.whereList.addAll(Arrays.asList(predictors));
    }

    @Override
    public Order ORDER(Column column) {
        OrderImpl order = new OrderImpl(column);
        this.orderList.add(order);
        return order;
    }

    @Override
    public void ORDER(Column... columns) {
        for (Column c : columns) {
            ORDER(c);
        }
    }

    @Override
    public void GROUP(Column... columns) {
        this.groupByList.addAll(Arrays.stream(columns).collect(Collectors.toList()));
    }

//    @Override
//    public SQLContext select(Column... columns) {
//        this.selectColumnList.addAll(Arrays.stream(columns).collect(Collectors.toList()));
//        return this;
//    }
//
//    @Override
//    public SQLContext from(Table<?> table) {
//        return null;
//    }

    @Override
    public <R> List<R> toResultList(Class<R> resultType) {
        String query = this.getResultQuery(resultType);
        QueryResultHandler<R> resultHandler = this.isJpql
                ? new JpqlResultHandler<>(this.entityManager, resultType)
                : new NativeResultHandler<>(this.entityManager, resultType, this.om, this.selectColumnList);

        try {
            return resultHandler.list(query);
        } catch (IllegalStateException | PersistenceException e) {
            LOGGER.error("error query :: \n" + query);
            LOGGER.error(e.getMessage());
            throw new PersistenceException(e);
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public <R> Optional<R> toResult(Class<R> resultType) {
        String query = this.toQuery();

        QueryResultHandler<R> resultHandler = this.isJpql
                ? new JpqlResultHandler<>(this.entityManager, resultType)
                : new NativeResultHandler<>(this.entityManager, resultType, this.om, this.selectColumnList);

        try {
            return resultHandler.detail(query);
        } catch (IllegalStateException | PersistenceException e) {
            LOGGER.error("error query :: \n" + query);
            LOGGER.error(e.getMessage());
            throw new PersistenceException(e);
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Class<T> getRootEntityType() {
        return this.from.getRoot().getEntityType();
    }

    @Override
    public List<Column> getSelectColumnList() {
        return this.selectColumnList;
    }

    @Override
    public boolean isJpql() {
        return this.isJpql;
    }

    private String getResultQuery(Class<?> resultType) {
        StringBuilder sb = new StringBuilder();
        Select select = this.isJpql() ? new JpqlSelect(resultType, this.selectColumnList) : new NativeSelect(this.selectColumnList);
        sb.append(select.toQuery());
        sb.append("\n");
        sb.append(this.toQuery());
        return sb.toString();
    }

    private boolean hasTable() {
        return this.from != null;
    }

    private boolean hasWhere() {
        return !this.whereList.isEmpty();
    }

    private boolean hasOrder() {
        return !this.orderList.isEmpty();
    }

    private boolean hasGroupBy() {
        return !this.groupByList.isEmpty();
    }
}
