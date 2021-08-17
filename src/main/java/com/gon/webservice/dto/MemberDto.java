package com.gon.webservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

@Getter @Setter
public class MemberDto {

    @NotEmpty(message = "회원 이름은 필수입니다.")
    private String name;

    @NotNull(message = "연락처를 입력하세요.")
    private Integer con;
    
//    @NotNull(message = "직원 번호는 필수입니다.")
//    @Range(min = 1000, max = 9999)
//    private Integer empNum;

    @NotEmpty(message = "이메일은 필수입니다.")
    private String email;

    @NotNull(message = "비밀번호는 필수입니다.")
    private String password;

    private String zipcode;
    private String street;
    private String details;

}
