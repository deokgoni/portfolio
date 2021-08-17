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
        validateDuplicateMember(member);
        memberRepository.save(member);
        //persist된 상태이기 때문에 영속성컨텍스트(key= member_id / value=member)에서 해당 id값을 가져올 수 있다.
        //DB에서 가져올 필요가 없음..
        return member.getId();
    }

    //중복 회원 검증
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");//객체의 상태가 매소드 호출에는 부적절한 경우
        }
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
