package bback.module.poqh3.nativesql.oracle;

import bback.module.poqh3.Native;
import bback.module.poqh3.SQLContextFactory;
import bback.module.poqh3.Table;
import bback.module.poqh3.target.entity.MemberEntity;
import bback.module.provider.EntityFactoryProvider;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

@DisplayName("ORACLE_의존_테스트")
class OracleNativeTest extends EntityFactoryProvider {

    private EntityManagerFactory emf;

    @BeforeEach
    void before() {
        emf = loadOracle();
    }

    @Test
    @DisplayName("페이징_테스트")
    void 페이징_테스트() {
        executeQuery(emf, em -> {
            saveDummyMember(em);

            Table<MemberEntity> MEMBER = Native.TABLE(MemberEntity.class);

            List<MemberEntity> memberEntities = SQLContextFactory.<MemberEntity>getContext(em)
                    .select(MEMBER.all())
                    .from(MEMBER)
                    .limit(2)
                    .toResultList(MemberEntity.class);
        });
    }


}
