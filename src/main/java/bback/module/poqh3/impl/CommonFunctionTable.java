package bback.module.poqh3.impl;

import bback.module.poqh3.Column;
import bback.module.poqh3.FunctionTable;
import bback.module.poqh3.exceptions.DeveloperMistakeException;
import jakarta.persistence.EntityManager;

public class CommonFunctionTable implements FunctionTable {

    private final EntityManager entityManager;
    private final DatabaseVendor databaseVendor;
    private final boolean isJpql;

    public CommonFunctionTable(EntityManager entityManager, boolean isJpql) {
        this.entityManager = entityManager;
        this.databaseVendor = NativeDatabaseVendorFactory.getVendor(entityManager);
        this.isJpql = isJpql;
    }

    @Override
    public Column currentDateTime() {
        if ( isJpql ) {
            return new JpqlSupplierColumn(FunctionCommand.CURRENT_TIMESTAMP);
        }

        throw new DeveloperMistakeException();
    }

    @Override
    public Column currentDate() {
        if ( isJpql ) {
            return new JpqlSupplierColumn(FunctionCommand.CURRENT_DATE);
        }

        throw new DeveloperMistakeException();
    }

    @Override
    public Column currentTime() {
        if ( isJpql ) {
            return new JpqlSupplierColumn(FunctionCommand.CURRENT_TIME);
        }

        throw new DeveloperMistakeException();
    }

    @Override
    public Column year(Column column) {
        if ( isJpql ) {
            return new JpqlFunctionalColumn(FunctionCommand.YEAR, column);
        }

        throw new DeveloperMistakeException();
    }

    @Override
    public Column month(Column column) {
        if ( isJpql ) {
            return new JpqlFunctionalColumn(FunctionCommand.MONTH, column);
        }

        throw new DeveloperMistakeException();
    }

    @Override
    public Column day(Column column) {
        if ( isJpql ) {
            return new JpqlFunctionalColumn(FunctionCommand.DAY, column);
        }

        throw new DeveloperMistakeException();
    }

    @Override
    public Column hour(Column column) {
        if ( isJpql ) {
            return new JpqlFunctionalColumn(FunctionCommand.HOUR, column);
        }

        throw new DeveloperMistakeException();
    }

    @Override
    public Column minute(Column column) {
        if ( isJpql ) {
            return new JpqlFunctionalColumn(FunctionCommand.MINUTE, column);
        }

        throw new DeveloperMistakeException();
    }

    @Override
    public Column second(Column column) {
        if ( isJpql ) {
            return new JpqlFunctionalColumn(FunctionCommand.SECOND, column);
        }

        throw new DeveloperMistakeException();
    }
}
