package bback.module;

import bback.module.poqh3.JPQL;
import bback.module.poqh3.SQLContext;
import bback.module.poqh3.SQLContextFactory;
import bback.module.poqh3.Table;
import bback.module.poqh3.target.entity.MemberEntity;
import bback.module.provider.EntityManagerProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MainTest extends EntityManagerProvider {


    @Test
    @DisplayName("method_chaining_test")
    void method_chaining_test() {
        executeQuery(em -> {
            SQLContext context = SQLContextFactory.getContext(em);
            Table<MemberEntity> MEMBER = JPQL.TABLE(MemberEntity.class);

//            context.select()
//                    .from(MEMBER)
//            ;
        });
    }
}