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
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Test
    public void testMember() throws Exception{
        //given
        Member member = new Member("joon");
        Member saveMember = memberRepository.save(member);
        //when
        Member findMember = memberRepository.findById(saveMember.getId()).get();
        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }
    @Test
    public void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member1");
        memberRepository.save(member1);
        memberRepository.save(member2);
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
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> findMembers =memberRepository .findByUsernameAndAgeGreaterThan("AAA", 15);
        assertThat(findMembers.size()).isEqualTo(1);
        assertThat(findMembers.get(0).getUsername()).isEqualTo(m2.getUsername());
        assertThat(findMembers.get(0).getId()).isEqualTo(m2.getId());
    }
    @Test
    public void findByUsernameTest(){
        Member member1 = new Member("joon", 15);
        Member member2 = new Member("sik", 15);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findMembers = memberRepository.findByUsername("joon");

        assertThat(findMembers.size()).isEqualTo(1);
        assertThat(findMembers.get(0)).isEqualTo(member1);
        assertThat(findMembers.get(0).getUsername()).isEqualTo("joon");
    }
}