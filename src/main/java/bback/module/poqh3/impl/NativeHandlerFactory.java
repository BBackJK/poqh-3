package bback.module.poqh3.impl;

import bback.module.poqh3.Pager;
import jakarta.persistence.EntityManager;

final class NativeHandlerFactory {

    private NativeHandlerFactory() throws IllegalAccessException {
        throw new IllegalAccessException("is utility class.");
    }

    private static final String PERSISTENCE_DRIVER_KEY = "jakarta.persistence.jdbc.url";

    public static DatabaseVendor getVendor(EntityManager entityManager) {
        Object jdbcUrlValue = entityManager.getEntityManagerFactory().getProperties().get(PERSISTENCE_DRIVER_KEY);
        if ( jdbcUrlValue != null && String.class.equals(jdbcUrlValue.getClass()) ) {
            String jdbcUrl = (String) jdbcUrlValue;

            if ( DatabaseVendor.SQLSERVER.is(jdbcUrl) ) {
                return DatabaseVendor.SQLSERVER;
            } else if ( DatabaseVendor.MYSQL.is(jdbcUrl) ) {
                return DatabaseVendor.MYSQL;
            } else if ( DatabaseVendor.MARIA.is(jdbcUrl) ) {
                return DatabaseVendor.MARIA;
            } else if ( DatabaseVendor.ORACLE.is(jdbcUrl) ) {
                return DatabaseVendor.ORACLE;
            } else if ( DatabaseVendor.POSTGRE.is(jdbcUrl) ) {
                return DatabaseVendor.POSTGRE;
            } else {
                return DatabaseVendor.H2;
            }
        }

        throw new IllegalArgumentException(" Empty JDBC Driver. ");
    }

    public static Pager getPager(DatabaseVendor vendor) {

        if (vendor == null) return null; // garbage in, garbage out

        switch ( vendor ) {
            case MYSQL:
                return new NativeMysqlPager();
            case MARIA:
                return new NativeMariaPager();
            case ORACLE:
                return new NativeOraclePager();
            case POSTGRE:
                return new NativePostgrePager();
            case SQLSERVER:
                return new NativeMssqlPager();
        }

        return new NativeH2Pager();
    }
}
