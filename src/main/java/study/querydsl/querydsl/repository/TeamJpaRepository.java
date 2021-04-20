package study.querydsl.querydsl.repository;

import org.springframework.stereotype.Repository;
import study.querydsl.querydsl.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

@Repository
public class TeamJpaRepository {
    @PersistenceContext
    EntityManager em;
    public Team save(Team team){
        em.persist(team);
        return team;
    }
    public Optional<Team> findById(Long teamId){
        return Optional.ofNullable(em.find(Team.class, teamId));
    }
    public List<Team> findAll(){
        return em.createQuery("select t from Team t", Team.class).getResultList();
    }
    public Long count(){
        return em.createQuery("select count(t) from Team t", Long.class).getSingleResult();
    }
    public void delete(Team team){
         em.remove(team);
    }
}
