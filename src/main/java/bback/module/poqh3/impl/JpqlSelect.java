package bback.module.poqh3.impl;

import bback.module.poqh3.Column;
import bback.module.poqh3.Select;
import bback.module.poqh3.exceptions.NoConstructorException;
import bback.module.poqh3.utils.ClassUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class JpqlSelect implements Select {

    private final List<Column> constructorColumnList;
    private final Constructor<?> constructorTarget;


    public JpqlSelect(Class<?> resultType, List<Column> selectColumnList) {
        if (resultType.getDeclaredConstructors().length < 1) {
            throw new NoConstructorException("JPQL 을 생성하려면 조회하고 싶은 객체의 Column 에 맞는 생성자를 생성해주세요.");
        }

        List<String> attrList = selectColumnList.stream().map(Column::getAttr).collect(Collectors.toList());
        Constructor<?>[] constructors = resultType.getDeclaredConstructors();
        Constructor<?> constructor = this.getTargetConstructor(constructors, attrList);
        if (constructor == null) {
            throw new NoConstructorException(" 조회하려는 컬럼과 일치하는 생성자가 존재하지 않습니다. ");
        }

        this.constructorTarget = constructor;
        this.constructorColumnList = this.getConstructorSelectColumnList(constructor, selectColumnList, attrList);
    }

    @Override
    public String toQuery() {
        StringBuilder sb = new StringBuilder(" select ");
        sb.append(" new ");
        sb.append(this.constructorTarget.getName());
        sb.append("( ");
        int selectColumnCount = this.constructorColumnList.size();
        int n=1;
        for (int i=0; i<selectColumnCount;i++, n++) {
            boolean isLast = n == selectColumnCount;
            Column column = this.constructorColumnList.get(i);
            sb.append(column.toQuery());
            if (column.hasAlias()) {
                sb.append(" as ");
                sb.append(column.getAttr());
            }
            if (!isLast) {
                sb.append(", ");
            }
        }
        sb.append(" ) ");
        return sb.toString();
    }


    private Constructor<?> getTargetConstructor(Constructor<?>[] constructors, List<String> attrList) {
        Constructor<?> target = null;
        int constructorCount = constructors.length;
        int maxConstructParameterMatchingCount = 0;
        master: for (int i=0;i<constructorCount;i++) {
            Constructor<?> candidate = constructors[i];
            Parameter[] parameters = candidate.getParameters();
            int constructorParameterCount = parameters.length;
            int matchingCount = 0;
            for (int n=0; n< constructorParameterCount; n++) {
                Parameter parameter = parameters[n];
                Class<?> parameterType = parameter.getType();
                if (ClassUtils.isListType(parameterType) || ClassUtils.isMapType(parameterType)) {
                    continue master;
                }
                boolean isExistAttrParameter = attrList.contains(parameter.getName());
                if ( isExistAttrParameter ) matchingCount++;
            }
            if (maxConstructParameterMatchingCount < matchingCount) {
                target = candidate;
                maxConstructParameterMatchingCount = matchingCount;
            }
        }
        return target;
    }

    private List<Column> getConstructorSelectColumnList(Constructor<?> constructor, List<Column> selectColumnList, List<String> attrList) {
        List<Column> result = new ArrayList<>();
        Parameter[] parameters = constructor.getParameters();
        int parameterCount = parameters.length;
        for (int i=0; i<parameterCount;i++) {
            Parameter parameter = parameters[i];
            int foundAttrIndex = attrList.indexOf(parameter.getName());
            if ( foundAttrIndex > -1 ) {
                result.add(selectColumnList.get(foundAttrIndex));
            } else {
                result.add(new NullColumn(parameter.getType()));
            }
        }
        return result;
    }
}
