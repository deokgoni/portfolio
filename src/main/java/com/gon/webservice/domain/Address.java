package com.gon.webservice.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private String zipcode;
    private String street;
    private String details;

    public Address(String zipcode, String street, String details) {
        this.zipcode = zipcode;
        this.street = street;
        this.details = details;
    }
}
