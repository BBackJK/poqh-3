package bback.module.poqh3;

import bback.module.poqh3.target.domain.EmptyConstructorArticle;
import bback.module.poqh3.target.entity.ArticleEntity;
import bback.module.poqh3.target.entity.MemberEntity;
import bback.module.provider.EntityManagerProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

@DisplayName("NATIVE_일반_테스트_그룹")
class NativeCommonTest extends EntityManagerProvider {

    @Test
    @DisplayName("목록_조회_테스트")
    void 목록_조회_테스트() {
        executeQuery(em -> {
            saveDummyMember(em);

            Table<MemberEntity> member = Native.TABLE(MemberEntity.class);

            List<MemberEntity> memberList = SQLContextFactory.<MemberEntity>getContext(em)
                    .select(member.all())
                    .from(member)
                    .where(member.col("name").like(Column.VALUE("테스터"), LikeType.START))
                    .toResultList(MemberEntity.class);

            Assertions.assertEquals(3, memberList.size());
        });
    }

    @Test
    @DisplayName("From_SubQuery_테스트")
    void IS_TEST() {
        executeQuery(entityManager -> {
            saveDummyMember(entityManager);
            saveDummyArticle(entityManager);

            Table<ArticleEntity> ARTICLE = Native.TABLE(ArticleEntity.class);
            Table<MemberEntity> MEMBER = Native.TABLE(MemberEntity.class);

            SQLContext<ArticleEntity> articleContext = SQLContextFactory.<ArticleEntity>getContext(entityManager)
                    .select(ARTICLE.all())
                    .from(ARTICLE)
                    .order(ARTICLE.col("id"), Order.OrderBy.DESC);

            Table<SQLContext<ArticleEntity>> ARTICLE_WRAPPER = Native.TABLE(articleContext);

            EmptyConstructorArticle article = SQLContextFactory.<SQLContext<ArticleEntity>>getContext(entityManager)
                    .select(ARTICLE_WRAPPER.cols("title", "contents"))
                    .select(MEMBER.col("name", "member_name"))
                    .from(ARTICLE_WRAPPER)
                    .join(MEMBER)
                    .on(ARTICLE_WRAPPER.col("member_id").eq(MEMBER.col("id")))
                    .where(ARTICLE_WRAPPER.col("id").eq(Column.VALUE(1)))
                    .toResult(EmptyConstructorArticle.class).orElseThrow(() ->  new RuntimeException("없습니다."))
                    ;

            Assertions.assertEquals(0, article.getId());
            Assertions.assertEquals("제목1", article.getTitle());
            Assertions.assertEquals("테스터2", article.getMemberName());
        });
    }
}
