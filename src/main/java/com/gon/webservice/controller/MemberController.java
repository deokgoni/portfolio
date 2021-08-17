package com.gon.webservice.controller;

import com.gon.webservice.domain.Address;
import com.gon.webservice.domain.Member;
import com.gon.webservice.dto.MemberDto;
import com.gon.webservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model){
        model.addAttribute("memberDto", new MemberDto());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberDto form, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            log.info("error = {}",bindingResult);
            return "members/createMemberForm";
        }

        Address address = new Address(form.getZipcode(), form.getStreet(), form.getDetails());
        Member member = new Member(form.getName(),form.getCon(), form.getEmail(), form.getPassword(), address);
        memberService.join(member);
        return "redirect:/home";
    }

    @GetMapping("/members")
    public String list(Model model){
        List<Member> members = memberService.findMembers();
        model.addAttribute("members",members);
        return "members/memberList";
    }

}
