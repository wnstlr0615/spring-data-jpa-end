package study.spring.data.jpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.spring.data.jpa.MemberDto;
import study.spring.data.jpa.entity.Member;
import study.spring.data.jpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;
    /*@PostConstruct
    public void init(){
        memberRepository.save(new Member("member1", 14));
        memberRepository.save(new Member("member1", 24));
        memberRepository.save(new Member("member3", 34));
        memberRepository.save(new Member("member4", 44));
        memberRepository.save(new Member("member5", 54));
    }*/
    @GetMapping("/members/v1/{id}")
    public MemberDto findMember(@PathVariable("id") Long id){
        Member findMember = memberRepository.findById(id).get();
        return new MemberDto(findMember.getUsername(), findMember.getAge());
    }
    @GetMapping("/members/v2/{id}")
    public MemberDto findMember(@PathVariable("id") Member member){
        return new MemberDto(member.getUsername(), member.getAge());
    }
    @GetMapping("/members")
    public Page<Member> list(@PageableDefault() Pageable pageable){
        return memberRepository.findAll(pageable);
    }
}
