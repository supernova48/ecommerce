package com.siju.ecommerce.product;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;


    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
     
        Product product = productMapper.toEntity(request);
        productRepository.save(product);
        return productMapper.toResponse(product);
    }


}
