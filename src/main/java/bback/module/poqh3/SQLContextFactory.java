package bback.module.poqh3;

import bback.module.poqh3.impl.SQLContextImpl;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import jakarta.persistence.EntityManager;

import java.lang.annotation.Annotation;


public final class SQLContextFactory {

    private static final ObjectMapper om;

    static {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setAnnotationIntrospector(new IgnoreAnnotationIntrospector());
        om = objectMapper;
    }

    private SQLContextFactory() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static <T> SQLContext<T> getContext(EntityManager entityManager) {
        return new SQLContextImpl<>(entityManager, om);
    }


    private static class IgnoreAnnotationIntrospector extends JacksonAnnotationIntrospector {
        @Override
        protected <A extends Annotation> A _findAnnotation(Annotated annotated, Class<A> annoClass) {
            if (annoClass == JsonProperty.class) {
                return null;
            }
            return super._findAnnotation(annotated, annoClass);
        }
    }
}
