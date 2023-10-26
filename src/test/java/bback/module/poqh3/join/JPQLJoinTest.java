package bback.module.poqh3.join;

import bback.module.poqh3.JPQL;
import bback.module.poqh3.SQLContext;
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

            SQLContext<ArticleEntity> articleContext = SQLContextFactory.getContext(em);

            articleContext.SELECT(ARTICLE.ALL());
            articleContext.FROM(ARTICLE)
                    .JOIN(MEMBER);

            List<ArticleEntity> articles = articleContext.toResultList(ArticleEntity.class);
            Assertions.assertEquals(6, articles.size());
            for (ArticleEntity ae : articles) {
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

            SQLContext<ArticleEntity> articleContext = SQLContextFactory.getContext(em);

            articleContext.SELECT(ARTICLE.ALL());
            articleContext.FROM(ARTICLE)
                    .JOIN(MEMBER)
                    .ON(MEMBER.COLUMN("id").EQ(ARTICLE.COLUMN("member_id")));

            List<ArticleEntity> articles = articleContext.toResultList(ArticleEntity.class);
            Assertions.assertEquals(6, articles.size());
            for (ArticleEntity ae : articles) {
                Assertions.assertNotNull(ae.getMember());
            }
        });
    }



}
