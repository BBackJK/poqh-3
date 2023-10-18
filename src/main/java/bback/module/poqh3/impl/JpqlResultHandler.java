package bback.module.poqh3.impl;

import bback.module.poqh3.QueryResultHandler;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;


import java.util.List;
import java.util.Optional;

public class JpqlResultHandler<T> implements QueryResultHandler<T> {

    private final EntityManager entityManager;
    private final Class<T> resultType;

    public JpqlResultHandler(EntityManager em, Class<T> resultType) {
        this.entityManager = em;
        this.resultType = resultType;
    }



    @Override
    public List<T> list(String query) {
        return this.getTypedQuery(query).getResultList();
    }

    @Override
    public Optional<T> detail(String query) {
        return Optional.ofNullable(
                this.getTypedQuery(query).getSingleResult()
        );
    }

    private TypedQuery<T> getTypedQuery(String query) {
        return this.entityManager.createQuery(query, this.resultType);
    }
}
