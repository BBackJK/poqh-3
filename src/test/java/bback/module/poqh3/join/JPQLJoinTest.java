package bback.module.poqh3.join;

import bback.module.poqh3.JPQL;
import bback.module.poqh3.SQLContextFactory;
import bback.module.poqh3.Table;
import bback.module.poqh3.target.entity.ArticleEntity;
import bback.module.poqh3.target.entity.MemberEntity;
import bback.module.provider.EntityManagerProvider;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

@DisplayName("JPQL_JOIN_테스트_그룹")
class JPQLJoinTest extends EntityManagerProvider {

    @Test
    @DisplayName("JPQL_JOIN_ON_없이_테스트")
    void JPQL_JOIN_ON_없이_테스트() {
        executeQuery(em -> {
            saveDummyMember(em);
            saveDummyArticle(em);

            Table<MemberEntity> MEMBER = JPQL.TABLE(MemberEntity.class);
            Table<ArticleEntity> ARTICLE = JPQL.TABLE(ArticleEntity.class);

            List<ArticleEntity> articles = SQLContextFactory.<ArticleEntity>getContext(em)
                    .select(ARTICLE.all())
                    .from(ARTICLE)
                    .join(MEMBER)
                    .toResultList(ArticleEntity.class);

            for (ArticleEntity ae: articles) {
                Assertions.assertNotNull(ae.getMember());
            }
        });
    }

    /**
     * join 할 시, 아래와 같이 field 명만 명시해줄 경우, JPQL 에서는 지원하지 않으므로 Exception 처리
     */
    @Test
    @DisplayName("JPQL_JOIN_ON_있는데_field명을_member로만_표기한경우_Exception_테스트")
    void JPQL_JOIN_ON_있는데_field명을_member로만_표기한경우_Exception_테스트() {
        executeQuery(em -> {
            saveDummyMember(em);
            saveDummyArticle(em);

            Table<MemberEntity> MEMBER = JPQL.TABLE(MemberEntity.class);
            Table<ArticleEntity> ARTICLE = JPQL.TABLE(ArticleEntity.class);

            Assertions.assertThrowsExactly(
                    PersistenceException.class
                    , () -> {
                        SQLContextFactory.<ArticleEntity>getContext(em)
                                .select(ARTICLE.all())
                                .from(ARTICLE)
                                .join(MEMBER)
                                .on(MEMBER.col("id").eq(ARTICLE.col("member")))
                                .toResultList(ArticleEntity.class);
                    }
            );
        });
    }


    @Test
    @DisplayName("JPQL_JOIN_ON_있는데_field명을_member_id_표기한경우_테스트")
    void JPQL_JOIN_ON_있는데_field명을_member_Id_표기한경우_테스트() {
        executeQuery(em -> {
            saveDummyMember(em);
            saveDummyArticle(em);

            Table<MemberEntity> MEMBER = JPQL.TABLE(MemberEntity.class);
            Table<ArticleEntity> ARTICLE = JPQL.TABLE(ArticleEntity.class);

            List<ArticleEntity> articleEntities = SQLContextFactory.<ArticleEntity>getContext(em)
                    .select(ARTICLE.all())
                    .from(ARTICLE)
                    .join(MEMBER)
                    .on(MEMBER.col("id").eq(ARTICLE.col("member_id")))
                    .toResultList(ArticleEntity.class);

            Assertions.assertEquals(6, articleEntities.size());
        });
    }

    @Test
    @DisplayName("JPQL_JOIN_ON_있는데_field명을_memberId_표기한경우_테스트")
    void JPQL_JOIN_ON_있는데_field명을_memberId_표기한경우_테스트() {
        executeQuery(em -> {
            saveDummyMember(em);
            saveDummyArticle(em);

            Table<MemberEntity> MEMBER = JPQL.TABLE(MemberEntity.class);
            Table<ArticleEntity> ARTICLE = JPQL.TABLE(ArticleEntity.class);

            List<ArticleEntity> articleEntities = SQLContextFactory.<ArticleEntity>getContext(em)
                    .select(ARTICLE.all())
                    .from(ARTICLE)
                    .join(MEMBER)
                    .on(MEMBER.col("id").eq(ARTICLE.col("memberId")))
                    .toResultList(ArticleEntity.class);

            Assertions.assertEquals(6, articleEntities.size());
        });
    }
}
