package bback.module.poqh3.jpql.functions.aggregations;

import bback.module.poqh3.FunctionTable;
import bback.module.poqh3.JPQL;
import bback.module.poqh3.SQLContextFactory;
import bback.module.poqh3.Table;
import bback.module.poqh3.target.domain.Member;
import bback.module.poqh3.target.entity.MemberEntity;
import bback.module.provider.EntityFactoryProvider;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

@DisplayName("JPQL_집계_함수_그룹")
class JPQLAggregationTest extends EntityFactoryProvider {

    private EntityManagerFactory emf;


    @BeforeEach
    void before() {
        this.emf = loadH2();
    }

    @Test
    @DisplayName("JPQL_COUNT_함수_테스트")
    void JPQL_COUNT_함수_테스트() {
        executeQuery(emf, em -> {
            saveDummyMember(em);
            saveDummyArticle(em);

            Table<MemberEntity> MEMBER = JPQL.TABLE(MemberEntity.class);
            FunctionTable func = JPQL.FUNCTIONS(em);

            List<Member> members = SQLContextFactory.<MemberEntity>getContext(em)
                    .select(MEMBER.all())
                    .select(func.year(func.currentDateTime()).as("year"))
                    .from(MEMBER)
                    .toResultList(Member.class);
                    ;

            System.out.println(members);


//            String query = "select new bback.module.poqh3.target.domain.Member(m.id, m.name, cast(count(a) as int))" +
//                    "from MemberEntity m join m.articles a group by m.id, m.name";
//
//            List<Member> members = em.createQuery(query, Member.class).getResultList();
//            System.out.println(members);
        });
    }
}
