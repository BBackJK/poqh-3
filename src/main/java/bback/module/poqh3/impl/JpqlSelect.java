package bback.module.poqh3.impl;

import bback.module.poqh3.Column;
import bback.module.poqh3.FunctionColumn;
import bback.module.poqh3.Select;
import bback.module.poqh3.exceptions.DMLValidationException;
import bback.module.poqh3.exceptions.NoConstructorException;
import bback.module.poqh3.utils.ClassUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class JpqlSelect implements Select {

    private final List<JpqlColumnParameterWrapper> constructorColumnWrapperList;
    private final Constructor<?> constructorTarget;


    public JpqlSelect(Class<?> resultType, List<Column> selectColumnList) {
        this.validationArguments(resultType);

        List<String> attrList = selectColumnList.stream().map(Column::getAttr).collect(Collectors.toList());
        Constructor<?>[] constructors = resultType.getDeclaredConstructors();
        Constructor<?> constructor = this.getTargetConstructor(constructors, attrList);
        if (constructor == null) {
            throw new NoConstructorException(" 조회하려는 컬럼과 일치하는 생성자가 존재하지 않습니다. ");
        }

        this.constructorTarget = constructor;
        this.constructorColumnWrapperList = this.getConstructorSelectColumnList(constructor, selectColumnList, attrList);
    }

    @Override
    public String toQuery() {
        StringBuilder sb = new StringBuilder(" ");
        sb.append("select");
        sb.append(" new ");
        sb.append(this.constructorTarget.getName());
        sb.append("( ");
        int selectColumnCount = this.constructorColumnWrapperList.size();
        int n=1;
        for (int i=0; i<selectColumnCount;i++, n++) {
            boolean isLast = n == selectColumnCount;
            JpqlColumnParameterWrapper wrapper = this.constructorColumnWrapperList.get(i);
            boolean isMismatch = wrapper.isMismatchType();

            if ( isMismatch ) {
                sb.append("cast(");
            }
            sb.append(wrapper.getColumnQuery());

            if ( isMismatch ) {
                sb.append(" as ");
                sb.append(wrapper.getParameterType());
                sb.append(")");
            }

            if ( wrapper.hasAlias() ) {
                sb.append(" as ");
                sb.append(wrapper.getAttr());
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

    private List<JpqlColumnParameterWrapper> getConstructorSelectColumnList(Constructor<?> constructor, List<Column> selectColumnList, List<String> attrList) {
        List<JpqlColumnParameterWrapper> result = new ArrayList<>();
        Parameter[] parameters = constructor.getParameters();
        int parameterCount = parameters.length;
        for (int i=0; i<parameterCount;i++) {
            Parameter parameter = parameters[i];
            int foundAttrIndex = attrList.indexOf(parameter.getName());
            if ( foundAttrIndex > -1 ) {
                Column foundColumn = selectColumnList.get(foundAttrIndex);
                result.add(new JpqlColumnParameterWrapper(parameter, foundColumn));
            } else {
                result.add(new JpqlColumnParameterWrapper(parameter, new NullColumn()));
            }
        }
        return result;
    }

    private void validationArguments(Class<?> resultType) {
        if ( resultType == null ) {
            throw new DMLValidationException(" resultType 을 지정해주세요. ");
        }

        if (resultType.getDeclaredConstructors().length < 1) {
            throw new NoConstructorException("JPQL 을 생성하려면 조회하고 싶은 객체의 Column 에 맞는 생성자를 생성해주세요.");
        }
    }

    class JpqlColumnParameterWrapper {

        private final Parameter parameter;

        private final Column column;

        public JpqlColumnParameterWrapper(Parameter parameter, Column column) {
            this.parameter = parameter;
            this.column = column;
        }

        public String getColumnQuery() {
            return this.column.toQuery();
        }

        public boolean isMismatchType() {
            if (this.column.isNullColumn()) {
                return true;
            }

            if (this.column.isJpqlColumn()) {
                Class<?> columnType = ((JpqlColumn<?>) this.column).getField().getType();
                Class<?> parameterType = this.parameter.getType();
                if ( columnType.equals(parameterType) ) {
                    return false;
                }
                Class<?> parameterWrapperType = ClassUtils.getWrapperClass(parameterType);
                return !columnType.equals(parameterWrapperType);
            }

            if (this.column.isFunctional()) {
                Class<?> columnType = ((FunctionColumn) this.column).getCommandHibernateReturnType();
                Class<?> parameterType = this.parameter.getType();
                if ( columnType.equals(parameterType) ) {
                    return false;
                }
                Class<?> parameterWrapperType = ClassUtils.getWrapperClass(parameterType);
                return !columnType.equals(parameterWrapperType);
            }

            return false;
        }

        public String getParameterType() {
            return this.parameter.getType().getName();
        }

        public boolean hasAlias() {
            return this.column.hasAlias();
        }

        public String getAttr() {
            return this.column.getAttr();
        }
    }
}
