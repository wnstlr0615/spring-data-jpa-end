package study.querydsl.querydsl.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import study.querydsl.querydsl.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {
    @PersistenceContext
    EntityManager em;
    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m left join fetch m.team", Member.class).getResultList();
    }
}
