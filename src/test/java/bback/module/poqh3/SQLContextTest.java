package bback.module.poqh3;

import bback.module.poqh3.domain.Member;
import bback.module.poqh3.entity.ArticleEntity;
import bback.module.poqh3.entity.MemberEntity;
import bback.module.poqh3.exceptions.DMLValidationException;
import bback.module.poqh3.exceptions.TableIsOnlyAcceptEntityException;
import bback.module.provider.EntityManagerProvider;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class SQLContextTest extends EntityManagerProvider {


    @Test
    @DisplayName("엔티티가 아닌 Class 로 테이블을 추출 Exception 테스트")
    void 엔티티가_아닌_Class_로_테이블을_추출_Exception_테스트() {
        Assertions.assertThrowsExactly(
                TableIsOnlyAcceptEntityException.class
                , () -> {
                    Table memberTable = JPQL.TABLE(Member.class);
                }
        );
    }

    @Test
    @DisplayName("엔티티인 Class 로 테이블을 추출 정상 테스트")
    void 엔티티인_Class_로_테이블을_추출_정상_테스트() {
        Table memberTable = JPQL.TABLE(MemberEntity.class);
        Assertions.assertTrue(memberTable.isJpql());
    }

    @Test
    @DisplayName("JPQL 로 생성한 테이블을 Native 로 생성한 테이블을 조인 시 Exception 테스트")
    void JPQL_로_생성한_테이블을_Native_로_생성한_테이블을_조인_시_Exception_테스트() {
        Assertions.assertThrowsExactly(
                DMLValidationException.class
                , () -> {
                    executeQuery(em -> {
                        Table MEMBER = JPQL.TABLE(MemberEntity.class);
                        Table ARTICLE = Native.TABLE(ArticleEntity.class);

                        SQLContext<ArticleEntity> articleContext = SQLContextFactory.getContext(em, ArticleEntity.class);
                        articleContext.SELECT(ARTICLE.ALL());
                        articleContext.FROM(ARTICLE)
                                .JOIN(MEMBER).ON(ARTICLE.COLUMN("member_id").EQ(MEMBER.COLUMN("id")));
                    });
                }
        );
    }

    @Test
    @DisplayName("Member 저장 후 SQLContext 를 통해서 조회 테스트")
    void Member_저장_후_SQLContext_를_통해서_조회_테스트() {
        executeQuery(em -> {
            saveMember(em);

            Table MEMBER = JPQL.TABLE(MemberEntity.class);
            SQLContext<MemberEntity> memberContext = SQLContextFactory.getContext(em, MemberEntity.class);

            memberContext.SELECT(MEMBER.ALL());
            memberContext.FROM(MEMBER);

            List<MemberEntity> memberList = memberContext.toResultList();
            Assertions.assertEquals(2, memberList.size());
        });
    }

    @Test
    @DisplayName("Member 저장 후 SQLContext 를 통해서 조회 시 Order Id Desc 테스트")
    void Member_저장_후_SQLContext_를_통해서_조회_시_Order_Id_Desc_테스트() {
        executeQuery(em -> {
            saveMember(em);

            Table MEMBER = JPQL.TABLE(MemberEntity.class);
            SQLContext<MemberEntity> memberContext = SQLContextFactory.getContext(em, MemberEntity.class);

            memberContext.SELECT(MEMBER.ALL());
            memberContext.FROM(MEMBER);
            memberContext.ORDER(MEMBER.COLUMN("id")).DESC();

            List<MemberEntity> memberList = memberContext.toResultList();
            Assertions.assertEquals(2, memberList.size());
            Assertions.assertEquals("test2", memberList.get(0).getId());
        });
    }

    @Test
    @DisplayName("Member 저장 후 SQLContext 를 통해서 조회 시 Group By 테스트")
    void Member_저장_후_SQLContext_를_통해서_조회_시_Group_By_테스트() {
        executeQuery(em -> {
            saveMember(em);

            Table MEMBER = JPQL.TABLE(MemberEntity.class);
            SQLContext<MemberEntity> memberContext = SQLContextFactory.getContext(em, MemberEntity.class);

            memberContext.SELECT(MEMBER.COLUMN("name"));
            memberContext.FROM(MEMBER);
            memberContext.GROUP(MEMBER.COLUMN("name"));

            List<MemberEntity> memberList = memberContext.toResultList();
            Assertions.assertEquals(2, memberList.size());
            Assertions.assertNull(memberList.get(0).getId());
            Assertions.assertNull(memberList.get(1).getId());
            Assertions.assertEquals("테스터1", memberList.get(0).getName());
            Assertions.assertEquals("테스터2", memberList.get(1).getName());
        });
    }


    private void saveMember(EntityManager em) {
        MemberEntity dummy1 = new MemberEntity("test1", "테스터1");
        MemberEntity dummy2 = new MemberEntity("test2", "테스터2");
        MemberEntity dummy3 = new MemberEntity("test3", "테스터1");
        em.persist(dummy1);
        em.persist(dummy2);
        em.persist(dummy3);
        em.flush();
    }
}