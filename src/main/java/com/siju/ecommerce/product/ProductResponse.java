package com.siju.ecommerce.product;

import java.math.BigDecimal;

import com.siju.ecommerce.category.ProductCategory;


public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        Boolean active,
        ProductCategory category

)

{

}
