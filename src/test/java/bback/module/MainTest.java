package bback.module;

import bback.module.provider.EntityManagerProvider;

class MainTest extends EntityManagerProvider {


//    @Test
//    @DisplayName("MemberEntity 저장 테스트")
//    void MemberEntity_저장_테스트() {
//        executeQuery((em) -> {
//            MemberEntity temp1 = new MemberEntity("test1","테스터1");
//            em.persist(temp1);
//            em.flush();
//        });
//
//
//        executeQuery((em) -> {
//            MemberEntity foundTemp = em.find(MemberEntity.class, "test1");
//            Assertions.assertNotNull(foundTemp);
//            Assertions.assertEquals("test1", foundTemp.getId());
//        });
//    }
}