package com.gon.webservice.service;

import com.gon.webservice.domain.Member;
import com.gon.webservice.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MemberService {

    //final : 컴파일 시점에 체크를 할 수 있음..
    private final MemberRepository memberRepository;

    //생성자 inject의 장점
    //1. memberRepository 필드변경 불가
    //2. Mock주입시 편함
    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원가입
     */
    @Transactional
    public Long join(Member member) throws IllegalStateException{
        memberRepository.save(member);
        return member.getId();
    }

    //회원 전체 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    //회원 조회
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

}
