package study.querydsl.querydsl.repository;

import study.querydsl.querydsl.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
