package bback.module.poqh3;

import bback.module.poqh3.target.entity.ArticleEntity;
import bback.module.poqh3.target.entity.MemberEntity;
import bback.module.provider.EntityManagerProvider;
import jakarta.persistence.criteria.JoinType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("NATIVE_일반_테스트_그룹")
class NativeCommonTest extends EntityManagerProvider {

    @Test
    @DisplayName("테스트중..")
    void IS_TEST() {
        executeQuery(entityManager -> {

            Table ARTICLE = Native.TABLE(ArticleEntity.class);
            SQLContext<?> ctx = SQLContextFactory.getContext(entityManager, null);
            ctx.SELECT(ARTICLE.ALL());
            ctx.FROM(ARTICLE);
            ctx.ORDER(ARTICLE.COLUMN("id")).DESC();

            SQLContext<ArticleEntity> wrapperCtx = SQLContextFactory.getContext(entityManager, ArticleEntity.class);
            Table ARTICLE_CONTEXT_TABLE = Native.TABLE(ctx, "a1");
            Table MEMBER = Native.TABLE(MemberEntity.class);

            wrapperCtx.SELECT(
                    ARTICLE_CONTEXT_TABLE.COLUMNS("title", "contents")
            );
            wrapperCtx.SELECT(
                    MEMBER.COLUMN("name", "member_name")
            );
            wrapperCtx.FROM(ARTICLE_CONTEXT_TABLE)
                    .JOIN(MEMBER, JoinType.LEFT)
                    .ON(
                            ARTICLE_CONTEXT_TABLE.COLUMN("member_id").EQ(MEMBER.COLUMN("id"))
                    );
            wrapperCtx.WHERE(ARTICLE_CONTEXT_TABLE.COLUMN("id").EQ(Column.VALUE(1)));


            /**
             *  select a1.title, a1.contents, t2.name as member_name
             *  from  (
             * 	    select t1.id, t1.title, t1.contents, t1.member_id
             * 	    from article t1
             * 	    order by t1.id desc
             *  ) a1
             *  left join member t2
             * 	on a1.member_id = t2.id
             *  where a1.id = 1
             */
            System.out.println(wrapperCtx.toQuery());
        });
    }
}
