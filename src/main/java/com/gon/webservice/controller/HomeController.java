package com.gon.webservice.controller;

import com.gon.webservice.domain.Member;
import com.gon.webservice.login.SessionConst;
import com.gon.webservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    @RequestMapping("/")
    public String login(){
        return "index";
    }

    @RequestMapping("/home")
    public String home(HttpServletRequest request, Model model){

        //세션이 없으면 home
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "index";
        }
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "index";
        }
        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "home";
    }

}
