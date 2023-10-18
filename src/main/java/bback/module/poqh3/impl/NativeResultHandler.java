package bback.module.poqh3.impl;

import bback.module.poqh3.Column;
import bback.module.poqh3.QueryResultHandler;
import bback.module.poqh3.logger.Log;
import bback.module.poqh3.logger.LogFactory;
import bback.module.poqh3.utils.PersistenceUtils;
import bback.module.poqh3.utils.Strings;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class NativeResultHandler<T> implements QueryResultHandler<T> {

    private static final Log LOGGER = LogFactory.getLog(QueryResultHandler.class);

    private final EntityManager entityManager;
    private final Class<T> resultType;
    private final ObjectMapper om;
    private final List<Column> selectColumnList;

    public NativeResultHandler(EntityManager em, Class<T> resultType, ObjectMapper om, List<Column> selectColumnList) {
        this.entityManager = em;
        this.resultType = resultType;
        this.om = om;
        this.selectColumnList = selectColumnList;
    }



    @Override
    public List<T> list(String query) {
        List<Object[]> resultObjectList = this.getNativeQuery(query).getResultList();
        List<Map<String, Object>> listMap = new ArrayList<>();
        for (Object[] dataObject : resultObjectList) {
            Map<String, Object> dataMap = new HashMap<>();
            int columnCount = dataObject.length;
            for (int i=0; i<columnCount; i++) {
                Object columnValue = dataObject[i];
                Column column = null;
                try {
                    column = this.selectColumnList.get(i);
                } catch (IndexOutOfBoundsException e) {
                    continue;
                }
                dataMap.put(column.getAttr(), columnValue);
            }
            listMap.add(dataMap);
        }
        try {
            return this.om.convertValue(listMap, List.class);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<T> detail(String query) {
        Object data = getNativeQuery(query).getSingleResult();
        if (data == null) {
            return (Optional<T>) Optional.ofNullable(data);
        }
        List<String> resultFieldNameList = PersistenceUtils.getColumnFields(resultType).stream().map(Field::getName).collect(Collectors.toList());
        if (data instanceof Object[]) {
            Object[] dataObject = (Object[]) data;
            Map<String, Object> dataMap = new HashMap<>();
            int columnCount = dataObject.length;
            for (int i=0; i<columnCount; i++) {
                Object columnValue = dataObject[i];
                Column column = null;
                try {
                    column = this.selectColumnList.get(i);
                } catch (IndexOutOfBoundsException e) {
                    continue;
                }
                String camelColumnName = Strings.toCamel(column.getAttr());
                if (resultFieldNameList.contains(camelColumnName)) {
                    dataMap.put(camelColumnName, columnValue);
                }
            }
            return Optional.of(this.om.convertValue(dataMap, resultType));
        }
        return Optional.of((T) data);
    }

    private Query getNativeQuery(String query) {
        return this.entityManager.createNativeQuery(query);
    }
}
