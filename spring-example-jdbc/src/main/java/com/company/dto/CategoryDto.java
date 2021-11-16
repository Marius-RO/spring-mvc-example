package com.company.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class CategoryDto {

    @Size(min = 1, max = 50, message = "{com.company.dto.categoryDto.name.sizeConstraint.message}")
    private String name;
}
