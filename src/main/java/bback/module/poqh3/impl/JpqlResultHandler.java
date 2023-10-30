package bback.module.poqh3.impl;

import bback.module.poqh3.Pager;
import bback.module.poqh3.QueryResultHandler;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class JpqlResultHandler<R> implements QueryResultHandler<R> {

    private final EntityManager entityManager;
    private final Class<R> resultType;
    private final int limit;
    private final int offset;

    public JpqlResultHandler(EntityManager em, Class<R> resultType) {
        this(em, resultType, null);
    }

    public JpqlResultHandler(EntityManager em, Class<R> resultType, Pager jpqlPager) {
        this.entityManager = em;
        this.resultType = resultType;
        if (jpqlPager != null) {
            this.limit = jpqlPager.getLimit();
            this.offset = jpqlPager.getOffset();
        } else {
            this.limit = 0;
            this.offset = 0;
        }
    }

    @Override
    public List<R> list(String query) {
        TypedQuery<R> typedQuery = this.getTypedQuery(query);
        if ( isPagination() ) {
            typedQuery.setFirstResult(offset);
            typedQuery.setMaxResults(limit);
        }
        return typedQuery.getResultList();
    }

    @Override
    public Optional<R> detail(String query) {
        try {
            return Optional.of(this.getTypedQuery(query).getSingleResult());
        } catch (NoResultException e) {
            // jakarta.persistence.NoResultException: No result found for query
            return Optional.empty();
        }
    }

    private TypedQuery<R> getTypedQuery(String query) {
        return this.entityManager.createQuery(query, this.resultType);
    }

    private boolean isPagination() {
        return this.limit > 0;
    }
}
