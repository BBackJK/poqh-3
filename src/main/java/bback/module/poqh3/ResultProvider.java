package bback.module.poqh3;

import jakarta.persistence.PersistenceException;

import java.util.List;
import java.util.Optional;

public interface ResultProvider {

    <R> List<R> toResultList(Class<R> resultType) throws PersistenceException;

    <R> Optional<R> toResult(Class<R> resultType) throws PersistenceException;
}
