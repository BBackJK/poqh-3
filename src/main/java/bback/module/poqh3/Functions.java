package bback.module.poqh3;

public interface Functions {

    // Date Time
    Column currentDateTime();

    Column currentDate();

    Column currentTime();

    Column year(Column input);

    Column month(Column input);

    Column day(Column input);

    Column hour(Column input);

    Column minute(Column input);

    Column second(Column input);

    // aggregation

    Column sum(Column input);

    Column min(Column input);

    Column max(Column input);

    Column avg(Column input);

    Column count(Column input);

    // string
    Column concat(Column... inputs);

    Column substring(Column input, int startPosition, int takeLength);

    Column trim(Column input);

    Column lower(Column input);

    Column upper(Column input);

    Column length(Column input);

    default Column substring(Column input, int startPosition) {
        return substring(input, startPosition, 0);
    }
}
