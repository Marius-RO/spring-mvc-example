package com.company.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProfileDto {
    private String lastName;
    private String firstName;
    private AddressDto addressDto;
}
