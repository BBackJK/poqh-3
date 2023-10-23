package bback.module.provider;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.function.Consumer;

public class EntityManagerProvider {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("test-persistence");


    protected void executeQuery(Consumer<EntityManager> consumer) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            consumer.accept(em);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
