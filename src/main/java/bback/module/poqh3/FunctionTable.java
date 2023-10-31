package bback.module.poqh3;

public interface FunctionTable {

    Column currentDateTime();

    Column currentDate();

    Column currentTime();

    Column year(Column column);

    Column month(Column column);

    Column day(Column column);

    Column hour(Column column);

    Column minute(Column column);

    Column second(Column column);
}
