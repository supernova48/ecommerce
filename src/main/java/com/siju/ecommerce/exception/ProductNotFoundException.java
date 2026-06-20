package com.siju.ecommerce.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id)
    {
        super("Product with id: " + id + " not found");
    }

}
