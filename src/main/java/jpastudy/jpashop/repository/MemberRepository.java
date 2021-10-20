package jpastudy.jpashop.repository;

import jpastudy.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    //원래 import 는 @PersistenceContext 인데 Autowired 해줘도 상관없음
    //@PersistenceContext
    //private EntityManager em;
    //EntityManager 주입 : @RequiredArgsConstructor
    private final EntityManager em;

    //등록
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(Member member){
        em.persist(member);
    }

    //id 로 Member 1명 조회
    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    //Member 모두 조회
    public List<Member> findAll(){
        //TypeQuery 이지만 return 하는 결과가 List 이기 때문에 getResultList 해줘야 함
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    //name 으로 Member 조회
    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name ",Member.class)
                .setParameter("name",name) //TypeQuery
                .getResultList();
    }


}
