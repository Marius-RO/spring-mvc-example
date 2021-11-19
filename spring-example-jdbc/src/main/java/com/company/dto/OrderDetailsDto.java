package com.company.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class OrderDetailsDto {
    @Size(min = 5, max = 100, message = "{com.company.dto.orderDetailsDto.fullName.sizeConstraint.message}")
    private String fullName;

    @Size(min = 5, max = 512, message = "{com.company.dto.orderDetailsDto.fullAddress.sizeConstraint.message}")
    private String fullAddress;
}
