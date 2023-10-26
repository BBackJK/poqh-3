package bback.module.poqh3.join;

import bback.module.poqh3.JPQL;
import bback.module.poqh3.SQLContextFactory;
import bback.module.poqh3.Table;
import bback.module.poqh3.target.entity.ArticleEntity;
import bback.module.poqh3.target.entity.MemberEntity;
import bback.module.provider.EntityManagerProvider;
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

    @Test
    @DisplayName("JPQL_JOIN_ON_있이_테스트")
    void JPQL_JOIN_ON_있이_테스트() {
        executeQuery(em -> {
            saveDummyMember(em);
            saveDummyArticle(em);

            Table<MemberEntity> MEMBER = JPQL.TABLE(MemberEntity.class);
            Table<ArticleEntity> ARTICLE = JPQL.TABLE(ArticleEntity.class);

            List<ArticleEntity> articles = SQLContextFactory.<ArticleEntity>getContext(em)
                    .select(ARTICLE.all())
                    .from(ARTICLE)
                    .join(MEMBER)
                    .on(MEMBER.col("id").eq(ARTICLE.col("member_id")))
                    .toResultList(ArticleEntity.class);

            for (ArticleEntity ae: articles) {
                Assertions.assertNotNull(ae.getMember());
            }
        });
    }



}
