package study.querydsl.querydsl.repository;

import org.springframework.stereotype.Repository;
import study.querydsl.querydsl.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {
    @PersistenceContext
    EntityManager em;

    public Member save(Member member){
        em.persist(member);
        return member;
    }
    public Optional<Member> findById(Long memberId){
        return Optional.ofNullable(em.find(Member.class, memberId));
    }
    public void delete(Member member){
        em.remove(member);
    }
    public List<Member> findAll(){
        return em.createQuery("select m from Member m ", Member.class).getResultList();
    }
    public Long count(){
        return em.createQuery("select count(m) from Member  m", Long.class).getSingleResult();
    }
    public Member find(Long memberId){
        return em.find(Member.class, memberId);
    }
    public List<Member> findByUsernameAndGreaterThan(String username, int age){
        return em.createQuery(
                "select m " +
                        "from Member m" +
                        " where m.username=:username" +
                        " and m.age>:age ", Member.class)
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }
    public List<Member> findByUsername(String username){
        return em.createNamedQuery("Member.findByUsername", Member.class)
                .setParameter("username", username)
                .getResultList();
    }
}
