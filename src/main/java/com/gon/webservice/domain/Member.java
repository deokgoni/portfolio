package com.gon.webservice.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;

    private String name;
    private int con;
    private String email;
    private String password;
    @Embedded
    private Address address;

    public Member(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public Member(String name, int con, String email, String password, Address address) {
        this.name = name;
        this.con = con;
        this.email = email;
        this.password = password;
        this.address = address;
    }

    //mappedBy 읽기 전용
    //해당 컬렉션을 꺼내서 persist하면 하이버네이트가 관리하는 collection으로 변경되기에
    //건들지 말고 필드값으로 사용..
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

}
