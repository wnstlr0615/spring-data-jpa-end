package study.spring.data.jpa.repository;

import study.spring.data.jpa.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
