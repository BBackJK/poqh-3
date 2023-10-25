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
    private final Class<T> resultType;
    private final List<Column> selectColumnList = new ArrayList<>();
    private final List<Predictor> whereList = new ArrayList<>();
    private final List<Order> orderList = new ArrayList<>();
    private final List<Column> groupByList = new ArrayList<>();
    private From from;
    private Select select;
    private boolean isJpql;

    public SQLContextImpl(EntityManager entityManager, Class<T> resultType, ObjectMapper om) {
        this.entityManager = entityManager;
        this.resultType = resultType;
        this.om = om;
    }

    @Override
    public String toQuery() {
        StringBuilder sb = new StringBuilder();
        this.select.setSelectColumnList(this.selectColumnList);
        sb.append(this.select.toQuery());
        sb.append("\n");

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
    public From FROM(Table table) {
        if (!table.hasAlias()) {
            table.AS(1);
        }
        this.isJpql = table.isJpql();
        this.select = table.isJpql() ? new JpqlSelect(this.resultType) : new NativeSelect();
        this.from = new FromImpl(table);
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

    @Override
    public List<T> toResultList() throws PersistenceException {
        String query = this.toQuery();

        QueryResultHandler<T> resultHandler = this.isJpql
                ? new JpqlResultHandler<>(this.entityManager, this.resultType)
                : new NativeResultHandler<>(this.entityManager, this.resultType, this.om, this.selectColumnList);

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
    public Optional<T> toResult() throws PersistenceException {
        String query = this.toQuery();

        QueryResultHandler<T> resultHandler = this.isJpql
                ? new JpqlResultHandler<>(this.entityManager, this.resultType)
                : new NativeResultHandler<>(this.entityManager, this.resultType, this.om, this.selectColumnList);

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
    public Class<?> getRootEntityType() {
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
