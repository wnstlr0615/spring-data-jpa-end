package study.spring.data.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.spring.data.jpa.MemberDto;
import study.spring.data.jpa.entity.Member;

import javax.persistence.QueryHint;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom{

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Override
    @EntityGraph(attributePaths={"team"})
    List<Member> findAll();

    @Query(name="Member.findByUsername")
    List<Member> findByUsername(@Param("username") String name);

    @Query(value="select m from Member m where m.username= :username and m.age= :age")
    List<Member> findByUsernameAndAge(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findByUsernameList(String username);

    @Query("select new study.spring.data.jpa.MemberDto(m.username, m.age) from Member m where m.username =:username")
    List<MemberDto> findMemberDtoByUsername(@Param("username") String username);

    @Query("select m from  Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    Page<Member> findByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age=m.age+1 where m.age>= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @QueryHints(value=@QueryHint(name="org.hibernate,readOnly", value="true"))
    Member findReadOnlyByUsername(String username);
}
