package study.spring.data.jpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.spring.data.jpa.MemberDto;
import study.spring.data.jpa.entity.Member;
import study.spring.data.jpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUtil;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;
    @Test
    public void testMember() throws Exception{
        //given
        Member member = initMember("joon", 10);
        //when
        Member findMember = memberRepository.findById(member.getId()).get();
        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD(){
        Member member1 = initMember("member1", 10);
        Member member2 = initMember("member1", 10);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        //단건 조회 검증
        assertThat(member1).isEqualTo(findMember1);
        assertThat(member2).isEqualTo(findMember2);

        //카운트 검증
        Long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //리스트 조회 검증
        List<Member> members = memberRepository.findAll();
        assertThat(members.size()).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        Long deleteCnt = memberRepository.count();
        assertThat(deleteCnt).isEqualTo(1);
    }
    @Test
    public void findByUsernameAndAgeGreaterThan(){
        Member m1 = initMember("AAA", 10);
        Member m2 = initMember("AAA", 20);


        List<Member> findMembers =memberRepository .findByUsernameAndAgeGreaterThan("AAA", 15);
        assertThat(findMembers.size()).isEqualTo(1);
        assertThat(findMembers.get(0).getUsername()).isEqualTo(m2.getUsername());
        assertThat(findMembers.get(0).getId()).isEqualTo(m2.getId());
    }
    @Test
    public void findByUsernameTest(){
        Member member1 = initMember("joon", 15);
        Member member2 = initMember("sik", 15);

        List<Member> findMembers = memberRepository.findByUsername("joon");

        assertThat(findMembers.size()).isEqualTo(1);
        assertThat(findMembers.get(0)).isEqualTo(member1);
        assertThat(findMembers.get(0).getUsername()).isEqualTo("joon");
    }
    @Test
    public void findByUsernameAndAgeTest(){
        Member member1 = initMember("joon", 15);
        Member member2 = initMember("joon", 20);

        List<Member> findMembers = memberRepository.findByUsernameAndAge("joon", 15);

        assertThat(findMembers.size()).isEqualTo(1);
        assertThat(findMembers.get(0)).isEqualTo(member1);
        assertThat(findMembers.get(0).getUsername()).isEqualTo("joon");
    }
    @Test
    public void findByUsernameListTest(){
        Member member1 = initMember("joon", 15);
        Member member2 = initMember("joon", 20);


        List<String> memberNames = memberRepository.findByUsernameList("joon");

        assertThat(memberNames.size()).isEqualTo(2);
        assertThat(memberNames.get(0)).isEqualTo("joon");
    }
    @Test
    public void findMemberDtoBuyUsername(){
        Member member1 = initMember("joon", 15);
        Member member2 = initMember("sik", 20);


        List<MemberDto> memberDtosNames = memberRepository.findMemberDtoByUsername("joon");

        assertThat(memberDtosNames.size()).isEqualTo(1);
        assertThat(memberDtosNames.get(0).getUsernae()).isEqualTo("joon");
        assertThat(memberDtosNames.get(0).getAge()).isEqualTo(15);
    }
    @Test
    public void findByNames(){
        initMember("joon", 15);
        initMember("sik", 20);

        List<Member> findMembers = memberRepository.findByNames(Arrays.asList("joon", "sik"));

        assertThat(findMembers.size()).isEqualTo(2);
    }
    @Transactional
    public Member initMember(String name, int age) {
        Member member = new Member(name, age);
        memberRepository.save(member);
        return member;
    }

    @Test
    public void page(){
        Member member1 = initMember("member1", 10);
        Member member2 = initMember("member2", 10);
        Member member3 = initMember("member3", 10);
        Member member4 = initMember("member4", 10);
        Member member5 = initMember("member5", 10);

        PageRequest pageRequest=PageRequest.of(0,3, Sort.by(Sort.Direction.DESC, "username"));
        Page<Member> page = memberRepository.findByAge(10, pageRequest);
        List<Member> content = page.getContent();

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.isLast()).isFalse();
        assertThat(page.hasNext()).isTrue();
    }
    @Test
    public void pageOfMemberDto(){
        Member member1 = initMember("member1", 10);
        Member member2 = initMember("member2", 10);
        Member member3 = initMember("member3", 10);
        Member member4 = initMember("member4", 10);
        Member member5 = initMember("member5", 10);

        PageRequest pageRequest=PageRequest.of(0,3, Sort.by(Sort.Direction.DESC, "username"));
        Page<Member> page = memberRepository.findByAge(10, pageRequest);
        Page<MemberDto> memberDtoPage = page.map(member -> new MemberDto(member.getUsername(), member.getAge()));
        List<MemberDto> content = memberDtoPage.getContent();

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.isLast()).isFalse();
        assertThat(page.hasNext()).isTrue();

        System.out.println(content.get(0));
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
        int resultCnt = memberRepository.bulkAgePlus(20);
        Optional<Member> findMember = memberRepository.findById(member3.getId());

        //then
        assertThat(resultCnt).isEqualTo(3);
        assertThat(findMember.get().getAge()).isEqualTo(21);
    }

    @Test
    @Description("지연 로딩으로 인한 N+1 문제 발생")
    public void findMemberLazy() throws Exception{
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
        List<Member> members = memberRepository.findAll();

        //then
        PersistenceUtil util=em.getEntityManagerFactory().getPersistenceUnitUtil();
        System.out.println("isLoaded :"+util.isLoaded(members.get(0).getTeam()));
        for (Member member : members) {
            member.getTeam().getName();
        }
        System.out.println("isLoaded :"+util.isLoaded(members.get(0).getTeam()));

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
        List<Member> members = memberRepository.findAll();

        //then
        PersistenceUtil util=em.getEntityManagerFactory().getPersistenceUnitUtil();
        System.out.println("isLoaded :"+util.isLoaded(members.get(0).getTeam()));
        for (Member member : members) {
            member.getTeam().getName();
        }
        System.out.println("isLoaded :"+util.isLoaded(members.get(0).getTeam()));

    }
    @Test
    public void findReadOnlyByUsername() throws Exception{
        //given
        Member member1 = initMember("member1", 10);
        em.flush();
        em.clear();
        //when
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");
        em.flush();
    }

    @Test
    public void customRepositoryTest(){
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
        List<Member> members = memberRepository.findMemberCustom();
        PersistenceUtil util=em.getEntityManagerFactory().getPersistenceUnitUtil();
        System.out.println(util.isLoaded(members.get(0).getTeam()));
    }
    @Test
    public void jpaEventBaseEntity() throws Exception{
        //given
        Member member = initMember("member1", 10);
        Thread.sleep(100);
        member.setUsername("member2");

        em.flush();
        em.clear();
        //when
        Optional<Member> findMember = memberRepository.findById(member.getId());
        //then
        System.out.println(findMember.get().getCreateDate());
        System.out.println(findMember.get().getLastModifiedDate());
        System.out.println(findMember.get().getCreatedBy());
        System.out.println(findMember.get().getLastModifiedBy());
    }



}