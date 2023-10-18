package bback.module.poqh3;

import bback.module.poqh3.impl.SQLContextImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;


public final class SQLContextFactory {

    private static final ObjectMapper om = new ObjectMapper();

    private SQLContextFactory() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static <T> SQLContext<T> getContext(EntityManager entityManager, Class<T> resultType) {
        return new SQLContextImpl<>(entityManager, resultType, om);
    }
}
