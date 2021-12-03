package com.company.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class ProductDto {

    public static final String MAX_PRODUCT_PRICE = "1000";

    @DecimalMin(value = "0.01", message = "{com.company.dto.productDto.price.decimalMinConstraint.message}")
    @DecimalMax(value = "999.99", message = "{com.company.dto.productDto.price.decimalMaxConstraint.message}")
    @Digits(integer = 4, fraction = 2, message = "{com.company.dto.productDto.price.digitsConstraint.message}")
    private BigDecimal price = new BigDecimal("0.0");

    @Min(value = 0, message = "{com.company.dto.productDto.stock.minConstraint.message}")
    @Max(value = 9999, message = "{com.company.dto.productDto.stock.maxConstraint.message}")
    @Digits(integer = 4, fraction = 0, message = "{com.company.dto.productDto.stock.digitsConstraint.message}")
    private int stock;

    @Size(min = 5, max = 60, message = "{com.company.dto.productDto.name.sizeConstraint.message}")
    private String name;

    @Size(min = 5, max = 500, message = "{com.company.dto.productDto.description.sizeConstraint.message}")
    private String description;

    private MultipartFile imageMultipartFile;
    private String imageBase64;

    @NotEmpty(message = "{com.company.dto.productDto.categoriesIds.notEmptyConstraint.message}")
    private int[] categoriesIds;

    boolean isAddedToCart;
}
