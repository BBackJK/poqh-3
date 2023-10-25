package bback.module.provider;

import bback.module.poqh3.target.entity.ArticleEntity;
import bback.module.poqh3.target.entity.MemberEntity;
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

    protected void saveDummyMember(EntityManager em) {
        MemberEntity dummy1 = new MemberEntity("test1", "테스터1");
        MemberEntity dummy2 = new MemberEntity("test2", "테스터2");
        MemberEntity dummy3 = new MemberEntity("test3", "테스터1");
        em.persist(dummy1);
        em.persist(dummy2);
        em.persist(dummy3);
        em.flush();
    }

    protected void saveDummyArticle(EntityManager em) {
        MemberEntity test1Member = em.find(MemberEntity.class, "test1");
        MemberEntity test2Member = em.find(MemberEntity.class, "test2");
        MemberEntity test3Member = em.find(MemberEntity.class, "test3");

        ArticleEntity dummy1 = new ArticleEntity(null, "제목1", "내용1234", test2Member);
        ArticleEntity dummy2 = new ArticleEntity(null, "제목2", "내용2", test2Member);

        ArticleEntity dummy3 = new ArticleEntity(null, "제목3", "내3", test1Member);
        ArticleEntity dummy4 = new ArticleEntity(null, "제목4", "내용45", test1Member);
        ArticleEntity dummy5 = new ArticleEntity(null, "제목5", "내용1235", test1Member);

        ArticleEntity dummy6 = new ArticleEntity(null, "제목6", null, test3Member);

        em.persist(dummy1);
        em.persist(dummy2);
        em.persist(dummy3);
        em.persist(dummy4);
        em.persist(dummy5);
        em.persist(dummy6);
        em.flush();
    }
}
