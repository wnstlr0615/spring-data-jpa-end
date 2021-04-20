package study.querydsl.querydsl.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.querydsl.entity.Member;
import study.querydsl.querydsl.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUtil;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {
    @Autowired
    MemberJpaRepository memberJpaRepository;
    @PersistenceContext
    EntityManager em;
    @Autowired
    TeamJpaRepository teamRepository;

    @Test
    public void testMember() throws Exception{
        //given
        Member member = initMember("joon", 10);
        //when
        Member findMember = memberJpaRepository.find(member.getId());
        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }
    @Test
    public void basicCRUD(){
        Member member1 = initMember("member1", 15);
        Member member2 = initMember("member1", 20);
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

        //단건 조회 검증
        assertThat(member1).isEqualTo(findMember1);
        assertThat(member2).isEqualTo(findMember2);

        //카운트 검증
        Long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        //리스트 조회 검증
        List<Member> members = memberJpaRepository.findAll();
        assertThat(members.size()).isEqualTo(2);

        //삭제 검증
        memberJpaRepository.delete(member1);
        Long deleteCnt = memberJpaRepository.count();
        assertThat(deleteCnt).isEqualTo(1);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan(){
        Member m1 =initMember("AAA", 10);
        Member m2 = initMember("AAA", 20);

        List<Member> findMembers = memberJpaRepository.findByUsernameAndGreaterThan("AAA", 15);
        assertThat(findMembers.size()).isEqualTo(1);
        assertThat(findMembers.get(0).getUsername()).isEqualTo(m2.getUsername());
        assertThat(findMembers.get(0).getId()).isEqualTo(m2.getId());
    }

    @Test
    public void findByUsernameTest(){
        Member member1 = initMember("joon", 15);
        Member member2 = initMember("joon1", 15);

        List<Member> findMembers = memberJpaRepository.findByUsername("joon");

        assertThat(findMembers.size()).isEqualTo(1);
        assertThat(findMembers.get(0)).isEqualTo(member1);
        assertThat(findMembers.get(0).getUsername()).isEqualTo("joon");
    }
    @Transactional
    public Member initMember(String name, int age) {
        Member member = new Member(name, age);
        memberJpaRepository.save(member);
        return member;
    }

    @Test
    public void findByAgePaging() {
        Member member1 = initMember("joon1", 10);
        Member member2 = initMember("joon2", 10);
        Member member3 = initMember("joon3", 10);
        Member member4 = initMember("joon4", 40);

        List<Member> findMembers = memberJpaRepository.findByPage(10, 2, 1);
        assertThat(findMembers.size()).isEqualTo(2);
        assertThat(findMembers.get(0).getUsername()).isEqualTo(member2.getUsername());
        Long count = memberJpaRepository.countTatal(10);
        assertThat(count).isEqualTo(3);
    }

    @Test
    public void bulkUpdate() throws Exception{
        //given
        Member member1 = initMember("member1", 10);
        Member member2 = initMember("member2", 19);
        Member member3 = initMember("member3", 20);
        Member member4 = initMember("member4", 21);
        Member member5 = initMember("member5", 40);
        //when
        int resultCnt = memberJpaRepository.bulkAgePlus(20);
        em.flush();
        em.clear();
        Optional<Member> findMember = memberJpaRepository.findById(member3.getId());

        //then
        assertThat(resultCnt).isEqualTo(3);
        assertThat(findMember.get().getAge()).isEqualTo(21);
    }
    @Test
    @Description("지연 로딩으로 인한 N+1 문제를 fetch join으로 해결")
    public void findMemberFetchJoin() throws Exception{
        //given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = initMember("member1", 10);
        Member member2 = initMember("member2", 20);
        member1.setTeam(teamA);
        member2.setTeam(teamB);
        em.flush();
        em.clear();

        //when
        List<Member> members = memberJpaRepository.findMemberFetchJoin();

        //then
        PersistenceUtil util=em.getEntityManagerFactory().getPersistenceUnitUtil();
        System.out.println("isLoaded :"+util.isLoaded(members.get(0).getTeam()));
        for (Member member : members) {
            member.getTeam().getName();
        }
        System.out.println("isLoaded :"+util.isLoaded(members.get(0).getTeam()));

    }


}