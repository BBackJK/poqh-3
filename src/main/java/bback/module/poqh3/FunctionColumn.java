package bback.module.poqh3;

public interface FunctionColumn extends Column {

    Class<?> getCommandHibernateReturnType();

    @Override
    default boolean isFunctional() {
        return true;
    }
}
