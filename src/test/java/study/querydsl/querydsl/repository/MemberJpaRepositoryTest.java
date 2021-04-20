package study.querydsl.querydsl.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.querydsl.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {
    @Autowired
    MemberJpaRepository memberJpaRepository;
    @Test
    public void testMember() throws Exception{
        //given
        Member member = new Member("joon");
        Member saveMember = memberJpaRepository.save(member);
        //when
        Member findMember = memberJpaRepository.find(saveMember.getId());
        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }
    @Test
    public void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member1");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
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
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<Member> findMembers = memberJpaRepository.findByUsernameAndGreaterThan("AAA", 15);
        assertThat(findMembers.size()).isEqualTo(1);
        assertThat(findMembers.get(0).getUsername()).isEqualTo(m2.getUsername());
        assertThat(findMembers.get(0).getId()).isEqualTo(m2.getId());
    }

    @Test
    public void findByUsernameTest(){
        Member member1 = new Member("joon", 15);
        Member member2 = new Member("sik", 15);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> findMembers = memberJpaRepository.findByUsername("joon");

        assertThat(findMembers.size()).isEqualTo(1);
        assertThat(findMembers.get(0)).isEqualTo(member1);
        assertThat(findMembers.get(0).getUsername()).isEqualTo("joon");
    }


}