package com.gon.webservice.service;

import com.gon.webservice.domain.Member;
import com.gon.webservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     * @return null이면 로그인 실패
     */
    public Member login(String email, String password) {
        return memberRepository.findByLoginId(email)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }
}
