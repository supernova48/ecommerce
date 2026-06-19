package com.siju.ecommerce.product;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductRequest(

    @NotBlank(message = "Product name is required")
    String name,
    
    String description,
    
    @NotNull(message = "Price is required")
    BigDecimal price,

    @NotNull(message = "Stock quantity is required")
    @PositiveOrZero(message = "Stock quantity must be zero or positive")
    Integer stockQuantity,

    @NotNull(message = "Active status is required")
    Boolean active

    
) 


{

}
