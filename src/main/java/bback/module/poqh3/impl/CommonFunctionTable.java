package bback.module.poqh3.impl;

import bback.module.poqh3.Column;
import bback.module.poqh3.Functions;
import bback.module.poqh3.exceptions.DeveloperMistakeException;
import jakarta.persistence.EntityManager;

public class CommonFunctionTable implements Functions {

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

    @Override
    public Column sum(Column column) {
        if ( isJpql ) {
            return new JpqlFunctionalColumn(FunctionCommand.SUM, column);
        }

        throw new DeveloperMistakeException();
    }

    @Override
    public Column min(Column column) {
        if ( isJpql ) {
            return new JpqlFunctionalColumn(FunctionCommand.MIN, column);
        }

        throw new DeveloperMistakeException();
    }

    @Override
    public Column max(Column column) {
        if ( isJpql ) {
            return new JpqlFunctionalColumn(FunctionCommand.MAX, column);
        }

        throw new DeveloperMistakeException();
    }

    @Override
    public Column avg(Column column) {
        if ( isJpql ) {
            return new JpqlFunctionalColumn(FunctionCommand.AVG, column);
        }

        throw new DeveloperMistakeException();
    }

    @Override
    public Column count(Column column) {
        if ( isJpql ) {
            return new JpqlFunctionalColumn(FunctionCommand.COUNT, column);
        }

        throw new DeveloperMistakeException();
    }

    @Override
    public Column concat(Column... inputs) {
        if ( isJpql ) {
            return new JpqlMultipleFunctionalColumn(FunctionCommand.CONCAT, inputs);
        }

        throw new DeveloperMistakeException();
    }

    @Override
    public Column substring(Column input, int startPosition, int takeLength) {
        if ( isJpql ) {
            if ( takeLength > 0 ) {
                return new JpqlMultipleFunctionalColumn(FunctionCommand.SUBSTRING, input, Column.VALUE(startPosition), Column.VALUE(takeLength));
            } else {
                return new JpqlMultipleFunctionalColumn(FunctionCommand.SUBSTRING, input, Column.VALUE(startPosition));
            }
        }

        throw new DeveloperMistakeException();
    }

    @Override
    public Column trim(Column input) {
        if ( isJpql ) {
            return new JpqlFunctionalColumn(FunctionCommand.TRIM, input);
        }

        throw new DeveloperMistakeException();
    }

    @Override
    public Column lower(Column input) {
        if ( isJpql ) {
            return new JpqlFunctionalColumn(FunctionCommand.LOWER, input);
        }

        throw new DeveloperMistakeException();
    }

    @Override
    public Column upper(Column input) {
        if ( isJpql ) {
            return new JpqlFunctionalColumn(FunctionCommand.UPPER, input);
        }

        throw new DeveloperMistakeException();
    }

    @Override
    public Column length(Column input) {
        if ( isJpql ) {
            return new JpqlFunctionalColumn(FunctionCommand.LENGTH, input);
        }

        throw new DeveloperMistakeException();
    }
}
