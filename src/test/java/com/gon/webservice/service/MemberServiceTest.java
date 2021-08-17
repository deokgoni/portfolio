package com.gon.webservice.service;

import com.gon.webservice.domain.Address;
import com.gon.webservice.domain.Member;
import com.gon.webservice.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest //sprintBoot를 띄운 상태에서 실행하기 위해서
@Transactional //Test 끝나고 rollback하기 위해서
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void 회원가입(){
        //given
        Member member = new Member("kim",new Address("서울","양천","123"));

        //when
        Long savedId = memberService.join(member);
        Member findMember = memberRepository.findOne(savedId);

        //then
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void 중복회원예외() throws Exception{
        //given
        Member member1 = new Member("kim",new Address("서울1","양천1","123"));
        Member member2 = new Member("kim",new Address("서울2","양천2","123"));

        //when
        memberService.join(member1);

        try{
            memberService.join(member2);
        }catch(IllegalStateException e){
            return;
        }

        //then
        Assert.fail("예외가 발생해야 합니다.");
    }
}