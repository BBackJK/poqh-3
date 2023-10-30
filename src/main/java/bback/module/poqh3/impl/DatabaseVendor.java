package bback.module.poqh3.impl;

enum DatabaseVendor {

    MYSQL("mysql")
    , MARIA("maria")
    , H2("h2")
    , ORACLE("oracle")
    , POSTGRE("postgre")
    , SQLSERVER("sqlserver")
    ;

    private final String nm;

    DatabaseVendor(String nm) {
        this.nm = nm;
    }

    public boolean is(String driverClassName) {
        return driverClassName != null && driverClassName.contains(this.nm);
    }

}
