package bback.module.poqh3.mysql;

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

@DisplayName("MYSQL_의존_테스트")
class MysqlNativeTest extends EntityFactoryProvider {

    private EntityManagerFactory emf;

    @BeforeEach
    void before() {
        emf = loadMysql();
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
                    .offset(1)
                    .limit(2)
                    .toResultList(MemberEntity.class);
        });
    }


}
