package bback.module.poqh3;

import bback.module.poqh3.exceptions.DMLValidationException;
import bback.module.poqh3.exceptions.TableIsOnlyAcceptEntityException;
import bback.module.poqh3.target.domain.Member;
import bback.module.poqh3.target.entity.ArticleEntity;
import bback.module.poqh3.target.entity.MemberEntity;
import bback.module.provider.EntityFactoryProvider;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;


@DisplayName("JPQL_일반_테스트_그룹")
class JPQLCommonTest extends EntityFactoryProvider {

    private EntityManagerFactory emf;

    @BeforeEach
    void before() {
        emf = loadH2();
    }

    @Test
    @DisplayName("엔티티가_아닌_Class_로_테이블을_추출_Exception_테스트")
    void 엔티티가_아닌_Class_로_테이블을_추출_Exception_테스트() {
        Assertions.assertThrowsExactly(
                TableIsOnlyAcceptEntityException.class
                , () -> {
                    Table<Member> memberTable = JPQL.TABLE(Member.class);
                }
        );
    }

    @Test
    @DisplayName("엔티티인_Class_로_테이블을_추출_정상_테스트")
    void 엔티티인_Class_로_테이블을_추출_정상_테스트() {
        Table<MemberEntity> memberTable = JPQL.TABLE(MemberEntity.class);
        Assertions.assertTrue(memberTable.isJpql());
    }

    @Test
    @DisplayName("JPQL_로_생성한_테이블을_Native_로_생성한_테이블을_조인_시_Exception_테스트")
    void JPQL_로_생성한_테이블을_Native_로_생성한_테이블을_조인_시_Exception_테스트() {
        Assertions.assertThrowsExactly(
                DMLValidationException.class
                , () -> {
                    executeQuery(emf, em -> {
                        Table<MemberEntity> MEMBER = JPQL.TABLE(MemberEntity.class);
                        Table<ArticleEntity> ARTICLE = Native.TABLE(ArticleEntity.class);

                        SQLContextFactory.<ArticleEntity>getContext(em)
                                .select(ARTICLE.all())
                                .from(ARTICLE)
                                .join(MEMBER)
                                .on(ARTICLE.col("member_id").eq(MEMBER.col("id")));
                    });
                }
        );
    }

    @Test
    @DisplayName("조회_테스트")
    void 조회_테스트() {
        executeQuery(emf, em -> {
            saveDummyMember(em);

            Table<MemberEntity> MEMBER = JPQL.TABLE(MemberEntity.class);

            List<MemberEntity> memberList = SQLContextFactory.<MemberEntity>getContext(em)
                    .select(MEMBER.all())
                    .from(MEMBER)
                    .toResultList(MemberEntity.class);

            Assertions.assertEquals(3, memberList.size());
        });
    }

    @Test
    @DisplayName("조회_페이지네이션_테스트")
    void 조회_페이지네이션_테스트() {
        executeQuery(emf, em -> {
            saveDummyMember(em);

            Table<MemberEntity> MEMBER = JPQL.TABLE(MemberEntity.class);

            List<MemberEntity> memberList = SQLContextFactory.<MemberEntity>getContext(em)
                    .select(MEMBER.all())
                    .from(MEMBER)
                    .offset(0)
                    .limit(2)
                    .toResultList(MemberEntity.class);

            Assertions.assertEquals(2, memberList.size());
        });
    }

    @Test
    @DisplayName("Order_Id_Desc_테스트")
    void Order_Id_Desc_테스트() {
        executeQuery(emf, em -> {
            saveDummyMember(em);

            Table<MemberEntity> MEMBER = JPQL.TABLE(MemberEntity.class);

            List<MemberEntity> memberList = SQLContextFactory.<MemberEntity>getContext(em)
                    .select(MEMBER.all())
                    .from(MEMBER)
                    .order(MEMBER.col("id"), Order.OrderBy.DESC)
                    .toResultList(MemberEntity.class);

            Assertions.assertEquals(3, memberList.size());
            Assertions.assertEquals("test3", memberList.get(0).getId());
        });
    }

    @Test
    @DisplayName("Group_By_테스트")
    void Group_By_테스트() {
        executeQuery(emf, em -> {
            saveDummyMember(em);

            Table<MemberEntity> MEMBER = JPQL.TABLE(MemberEntity.class);

            List<MemberEntity> memberList = SQLContextFactory.<MemberEntity>getContext(em)
                    .select(MEMBER.col("name"))
                    .from(MEMBER)
                    .group(MEMBER.col("name"))
                    .toResultList(MemberEntity.class);

            Assertions.assertEquals(2, memberList.size());
            Assertions.assertNull(memberList.get(0).getId());
            Assertions.assertNull(memberList.get(1).getId());
            Assertions.assertEquals("테스터1", memberList.get(0).getName());
            Assertions.assertEquals("테스터2", memberList.get(1).getName());
        });
    }

    @Test
    @DisplayName("Where_Predictor_Equal_테스트")
    void Predictor_Equal_테스트() {
        executeQuery(emf, em -> {
            saveDummyMember(em);

            Table<MemberEntity> MEMBER = JPQL.TABLE(MemberEntity.class);

            MemberEntity member = SQLContextFactory.<MemberEntity>getContext(em)
                    .select(MEMBER.all())
                    .from(MEMBER)
                    .where(MEMBER.col("id").eq(Column.VALUE("test1")))
                    .toResult(MemberEntity.class)
                    .get();

            Assertions.assertEquals("test1", member.getId());
        });
    }

    @Test
    @DisplayName("Where_Predictor_Not_Equal_테스트")
    void Where_Predictor_Not_Equal_테스트() {
        executeQuery(emf, em -> {
            saveDummyMember(em);

            Table<MemberEntity> MEMBER = JPQL.TABLE(MemberEntity.class);

            List<MemberEntity> members = SQLContextFactory.<MemberEntity>getContext(em)
                    .select(MEMBER.all())
                    .from(MEMBER)
                    .where(MEMBER.col("id").neq(Column.VALUE("test1")))
                    .toResultList(MemberEntity.class)
                    ;

            Assertions.assertEquals(2, members.size());
        });
    }

    @Test
    @DisplayName("Where_Predictor_Equal_Result_Null_테스트")
    void Where_Predictor_Equal_Result_Null_테스트() {
        executeQuery(emf, em -> {
            saveDummyMember(em);

            Table<MemberEntity> MEMBER = JPQL.TABLE(MemberEntity.class);

            Assertions.assertThrowsExactly(
                    RuntimeException.class
                    , () -> {
                        SQLContext<MemberEntity> memberContext = SQLContextFactory.<MemberEntity>getContext(em)
                                .select(MEMBER.all())
                                .from(MEMBER)
                                .where(MEMBER.col("id").eq(Column.VALUE("test5")));

                        memberContext.toResult(MemberEntity.class).orElseThrow(() -> new RuntimeException("없습니다."));
                    }
            );
        });
    }

    @Test
    @DisplayName("Where_Predictor_Greater_Than_테스트")
    void Where_Predictor_Greater_Than_테스트() {
        executeQuery(emf, em -> {
            saveDummyMember(em);
            saveDummyArticle(em);

            Table<ArticleEntity> ARTICLE = JPQL.TABLE(ArticleEntity.class);

            List<ArticleEntity> articleEntities = SQLContextFactory.<ArticleEntity>getContext(em)
                    .select(ARTICLE.all())
                    .from(ARTICLE)
                    .where(ARTICLE.col("id").gt(Column.VALUE(2)))
                    .toResultList(ArticleEntity.class);

            Assertions.assertEquals(4, articleEntities.size());
        });
    }

    @Test
    @DisplayName("Where_Predictor_Greater_Than_And_Equal_테스트")
    void Where_Predictor_Greater_Than_And_Equal_테스트() {
        executeQuery(emf, em -> {
            saveDummyMember(em);
            saveDummyArticle(em);

            Table<ArticleEntity> ARTICLE = JPQL.TABLE(ArticleEntity.class);

            List<ArticleEntity> articleEntities = SQLContextFactory.<ArticleEntity>getContext(em)
                    .select(ARTICLE.all())
                    .from(ARTICLE)
                    .where(ARTICLE.col("id").ge(Column.VALUE(2)))
                    .toResultList(ArticleEntity.class);

            Assertions.assertEquals(5, articleEntities.size());
        });
    }

    @Test
    @DisplayName("Where_Predictor_Less_Than_테스트")
    void Where_Predictor_Less_Than_테스트() {
        executeQuery(emf, em -> {
            saveDummyMember(em);
            saveDummyArticle(em);

            Table<ArticleEntity> ARTICLE = JPQL.TABLE(ArticleEntity.class);

            List<ArticleEntity> articleEntities = SQLContextFactory.<ArticleEntity>getContext(em)
                    .select(ARTICLE.all())
                    .from(ARTICLE)
                    .where(ARTICLE.col("id").lt(Column.VALUE(2)))
                    .toResultList(ArticleEntity.class);

            Assertions.assertEquals(1, articleEntities.size());
        });
    }

    @Test
    @DisplayName("Where_Predictor_Less_Than_And_Equal_테스트")
    void Where_Predictor_Less_Than_And_Equal_테스트() {
        executeQuery(emf, em -> {
            saveDummyMember(em);
            saveDummyArticle(em);

            Table<ArticleEntity> ARTICLE = JPQL.TABLE(ArticleEntity.class);

            List<ArticleEntity> articleEntities = SQLContextFactory.<ArticleEntity>getContext(em)
                    .select(ARTICLE.all())
                    .from(ARTICLE)
                    .where(ARTICLE.col("id").le(Column.VALUE(2)))
                    .toResultList(ArticleEntity.class);

            Assertions.assertEquals(2, articleEntities.size());
        });
    }

    @Test
    @DisplayName("Where_Predictor_In_테스트")
    void Where_Predictor_In_테스트() {
        executeQuery(emf, em -> {
            saveDummyMember(em);
            saveDummyArticle(em);

            Table<ArticleEntity> ARTICLE = JPQL.TABLE(ArticleEntity.class);

            List<ArticleEntity> articleEntities = SQLContextFactory.<ArticleEntity>getContext(em)
                    .select(ARTICLE.all())
                    .from(ARTICLE)
                    .where(
                            ARTICLE.col("id")
                                    .in(Column.VALUES(3, 5, 6))
                    )
                    .toResultList(ArticleEntity.class);

            Assertions.assertEquals(3, articleEntities.size());
        });
    }

    @Test
    @DisplayName("Where_Predictor_Not_In_테스트")
    void Where_Predictor_Not_In_테스트() {
        executeQuery(emf, em -> {
            saveDummyMember(em);
            saveDummyArticle(em);

            Table<ArticleEntity> ARTICLE = JPQL.TABLE(ArticleEntity.class);

            List<ArticleEntity> articleEntities = SQLContextFactory.<ArticleEntity>getContext(em)
                    .select(ARTICLE.all())
                    .from(ARTICLE)
                    .where(
                            ARTICLE.col("id")
                                    .notIn(Column.VALUES(3, 5, 6))
                    )
                    .toResultList(ArticleEntity.class);

            Assertions.assertEquals(3, articleEntities.size());
            Assertions.assertEquals(1, articleEntities.get(0).getId());
            Assertions.assertEquals(2, articleEntities.get(1).getId());
            Assertions.assertEquals(4, articleEntities.get(2).getId());
        });
    }

    @Test
    @DisplayName("Where_Predictor_Null_테스트")
    void Where_Predictor_Null_테스트() {
        executeQuery(emf, em -> {
            saveDummyMember(em);
            saveDummyArticle(em);

            Table<ArticleEntity> ARTICLE = JPQL.TABLE(ArticleEntity.class);

            List<ArticleEntity> articleEntities = SQLContextFactory.<ArticleEntity>getContext(em)
                    .select(ARTICLE.all())
                    .from(ARTICLE)
                    .where(ARTICLE.col("contents").isNull())
                    .toResultList(ArticleEntity.class);

            Assertions.assertEquals(1, articleEntities.size());
            Assertions.assertNull(articleEntities.get(0).getContents());
        });
    }

    @Test
    @DisplayName("Where_Predictor_Not_Null_테스트")
    void Where_Predictor_Not_Null_테스트() {
        executeQuery(emf, em -> {
            saveDummyMember(em);
            saveDummyArticle(em);

            Table<ArticleEntity> ARTICLE = JPQL.TABLE(ArticleEntity.class);

            List<ArticleEntity> articleEntities = SQLContextFactory.<ArticleEntity>getContext(em)
                    .select(ARTICLE.all())
                    .from(ARTICLE)
                    .where(ARTICLE.col("contents").isNotNull())
                    .toResultList(ArticleEntity.class);

            Assertions.assertEquals(5, articleEntities.size());
        });
    }

    @Test
    @DisplayName("Where_Predictor_LIKE_ANY_테스트")
    void Where_Predictor_LIKE_ANY_테스트() {
        executeQuery(emf, em -> {
            saveDummyMember(em);
            saveDummyArticle(em);

            Table<ArticleEntity> ARTICLE = JPQL.TABLE(ArticleEntity.class);

            List<ArticleEntity> articleEntities = SQLContextFactory.<ArticleEntity>getContext(em)
                    .select(ARTICLE.all())
                    .from(ARTICLE)
                    .where(ARTICLE.col("contents").like(Column.VALUE("내용")))
                    .toResultList(ArticleEntity.class);

            Assertions.assertEquals(4, articleEntities.size());
        });
    }

    @Test
    @DisplayName("Where_Predictor_LIKE_START_테스트")
    void Where_Predictor_LIKE_START_테스트() {
        executeQuery(emf, em -> {
            saveDummyMember(em);
            saveDummyArticle(em);

            Table<ArticleEntity> ARTICLE = JPQL.TABLE(ArticleEntity.class);
//            SQLContext<ArticleEntity> articleContext = SQLContextFactory.getContext(em);

//            articleContext.SELECT(ARTICLE.ALL());
//            articleContext.FROM(ARTICLE);
//            articleContext.WHERE(ARTICLE.COLUMN("contents").LIKE(Column.VALUE("내"), LikeType.START));
//            List<ArticleEntity> articleEntities = articleContext.toResultList(ArticleEntity.class);

            List<ArticleEntity> articleEntities = SQLContextFactory.<ArticleEntity>getContext(em)
                    .select(ARTICLE.all())
                    .from(ARTICLE)
                    .where(
                            ARTICLE.col("contents")
                                    .like(Column.VALUE("내"), LikeType.START))
                    .toResultList(ArticleEntity.class);

            Assertions.assertEquals(5, articleEntities.size());
        });
    }

    @Test
    @DisplayName("Where_Predictor_LIKE_END_테스트")
    void Where_Predictor_LIKE_END_테스트() {
        executeQuery(emf, em -> {
            saveDummyMember(em);
            saveDummyArticle(em);

            Table<ArticleEntity> ARTICLE = JPQL.TABLE(ArticleEntity.class);
            SQLContext<ArticleEntity> articleContext = SQLContextFactory.getContext(em);

//            articleContext.SELECT(ARTICLE.ALL());
//            articleContext.FROM(ARTICLE);
//            articleContext.WHERE(ARTICLE.COLUMN("contents").LIKE(Column.VALUE("5"), LikeType.END));
//            List<ArticleEntity> articleEntities = articleContext.toResultList(ArticleEntity.class);

            List<ArticleEntity> articleEntities = SQLContextFactory.<ArticleEntity>getContext(em)
                    .select(ARTICLE.all())
                    .from(ARTICLE)
                    .where(ARTICLE.col("contents").like(Column.VALUE(5), LikeType.END))
                    .toResultList(ArticleEntity.class);

            Assertions.assertEquals(2, articleEntities.size());
        });
    }

    @Test
    @DisplayName("Where_Predictor_Between_테스트")
    void Where_Predictor_Between_테스트() {
        executeQuery(emf, em -> {
            saveDummyMember(em);
            saveDummyArticle(em);

            Table<ArticleEntity> ARTICLE = JPQL.TABLE(ArticleEntity.class);

            List<ArticleEntity> articleEntities = SQLContextFactory.<ArticleEntity>getContext(em)
                    .select(ARTICLE.all())
                    .from(ARTICLE)
                    .where(ARTICLE.col("id").between(Column.VALUE(3), Column.VALUE(5)))
                    .toResultList(ArticleEntity.class);

            Assertions.assertEquals(3, articleEntities.size());
            Assertions.assertEquals(3, articleEntities.get(0).getId());
            Assertions.assertEquals(4, articleEntities.get(1).getId());
            Assertions.assertEquals(5, articleEntities.get(2).getId());
        });
    }

    @Test
    @DisplayName("Where_Predictor_Not_Between_테스트")
    void Where_Predictor_Not_Between_테스트() {
        executeQuery(emf, em -> {
            saveDummyMember(em);
            saveDummyArticle(em);

            Table<ArticleEntity> ARTICLE = JPQL.TABLE(ArticleEntity.class);

            List<ArticleEntity> articleEntities = SQLContextFactory.<ArticleEntity>getContext(em)
                    .select(ARTICLE.all())
                    .from(ARTICLE)
                    .where(ARTICLE.col("id").notBetween(Column.VALUE(3), Column.VALUE(5)))
                    .toResultList(ArticleEntity.class);

            Assertions.assertEquals(3, articleEntities.size());
            Assertions.assertEquals(1, articleEntities.get(0).getId());
            Assertions.assertEquals(2, articleEntities.get(1).getId());
            Assertions.assertEquals(6, articleEntities.get(2).getId());
        });
    }
}