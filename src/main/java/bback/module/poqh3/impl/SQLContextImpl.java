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
    private final DatabaseVendor databaseVendor;
    private final ObjectMapper om;
    private final List<Column> selectColumnList = new ArrayList<>();
    private final List<Predictor> whereList = new ArrayList<>();
    private final List<Order> orderList = new ArrayList<>();
    private final List<Column> groupByList = new ArrayList<>();
    private From<T> from;
    private Class<?> resultType;
    private boolean isJpql;
    private Pager pager = null;

    public SQLContextImpl(EntityManager entityManager, ObjectMapper om) {
        this.entityManager = entityManager;
        this.databaseVendor = NativeDatabaseVendorFactory.getVendor(entityManager);
        this.om = om;
    }

    @Override
    public String toQuery() {
        StringBuilder sb = new StringBuilder();

        Select select = this.isJpql()
                ? new JpqlSelect(this.resultType == null ? this.getRootEntityType() : this.resultType, this.selectColumnList)
                : new NativeSelect(this.selectColumnList);
        sb.append(select.toQuery());
        sb.append("\n");

        if ( hasTable() ) {
            sb.append(from.toQuery());
        }

        if ( hasWhere() ) {
            sb.append(" where ");
            this.whereList.forEach(w -> {
                sb.append(w.toQuery());
                sb.append("\n");
            });
        }

        if ( hasOrder() ) {
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

        if ( hasPagination() && !isJpql ) {
            sb.append(this.pager.toQuery());
            sb.append("\n");
        }

        return sb.toString();
    }

    @Override
    public SQLContext<T> select(Column... columns) {
        this.selectColumnList.addAll(Arrays.stream(columns).collect(Collectors.toList()));
        return this;
    }

    @Override
    public From<T> from(Table<T> table) {
        if (!table.hasAlias()) {
            table.as(1);
        }
        this.isJpql = table.isJpql();
        this.from = new FromImpl<>(this, table);
        return this.from;
    }

    @Override
    public SQLContext<T> limit(int limit) {
        if ( !this.hasPagination() ) {
            this.pager = isJpql ? new JpqlPager() : NativeDatabaseVendorFactory.getPager(this.databaseVendor);
        }
        this.pager.setLimit(limit);
        return this;
    }

    @Override
    public SQLContext<T> offset(int offset) {
        if ( !this.hasPagination() ) {
            this.pager = isJpql ? new JpqlPager() : NativeDatabaseVendorFactory.getPager(this.databaseVendor);
        }
        this.pager.setOffset(offset);
        return this;
    }

    @Override
    public SQLContext<T> where(Predictor... predictors) {
        this.whereList.addAll(Arrays.asList(predictors));
        return this;
    }

    @Override
    public SQLContext<T> order(Column... columns) {
        for (Column c : columns) {
            order(c, Order.OrderBy.ASC);
        }
        return this;
    }

    @Override
    public SQLContext<T> order(Column column, Order.OrderBy orderBy) {
        OrderImpl order = new OrderImpl(column, orderBy);
        this.orderList.add(order);
        return this;
    }

    @Override
    public SQLContext<T> group(Column... columns) {
        this.groupByList.addAll(Arrays.stream(columns).collect(Collectors.toList()));
        return this;
    }

    @Override
    public <R> List<R> toResultList(Class<R> resultType) {
        this.resultType = resultType;
        String query = toQuery();
        System.out.println(query);

        QueryResultHandler<R> resultHandler = this.isJpql
                ? new JpqlResultHandler<>(this.entityManager, resultType, this.pager)
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
        this.resultType = resultType;
        String query = this.toQuery();
        System.out.println(query);

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

    private boolean hasPagination() {
        return this.pager != null;
    }

}
