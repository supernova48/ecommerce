package com.siju.ecommerce.product;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.siju.ecommerce.category.ProductCategory;
import com.siju.ecommerce.common.PageResponse;
import com.siju.ecommerce.common.PaginationResponse;
import com.siju.ecommerce.common.SliceResponse;
import com.siju.ecommerce.config.PaginationProperties;
import com.siju.ecommerce.exception.ProductNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final PaginationProperties sliceProperties;
    public static final String PRODUCT = "product";
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {

        Product product = productMapper.toEntity(request);
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    public PaginationResponse<ProductResponse> getProducts(int pageNumber,
            int pageSize, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        if (useSlice(PRODUCT)) {
            logger.debug("slice used for {}", PRODUCT);
            Slice<Product> productSlice = productRepository.findBy(pageable);
            List<ProductResponse> content = productSlice.getContent()
                    .stream().map(productMapper::toResponse).toList();
            return new SliceResponse<>(content, pageNumber, pageSize, productSlice.isFirst(), productSlice.isLast(),
                    productSlice.hasNext());

        }
        logger.debug("slice NOT used for {}", PRODUCT);
        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductResponse> content = productPage.getContent().stream()
                .map(productMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages(),
                productPage.isFirst(),
                productPage.isLast());
    }

    public PaginationResponse<ProductResponse> getProductsByName(String keyword,
            int pageNumber, int pageSize, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Product> productPage = productRepository.findByNameContainingIgnoreCase(keyword, pageable);

        List<ProductResponse> content = productPage.getContent().stream()
                .map(productMapper::toResponse)
                .toList();

        return new PageResponse<>(content, productPage.getNumber(),
                productPage.getSize(), productPage.getTotalElements(),
                productPage.getTotalPages(), productPage.isFirst(),
                productPage.isLast());

    }

    @Cacheable(value = "products", key = "#id")
    public ProductResponse getProductById(Long id) {

        logger.debug("Fetching product with id: {}", id);
        Optional<Product> productOptional = productRepository.findById(id);

        Product product = productOptional.orElseThrow(() -> new ProductNotFoundException(id));
        return productMapper.toResponse(product);
    }

    @CacheEvict(value = "products", key = "#id")
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Optional<Product> productOptional = productRepository.findById(id);
        Product existingProduct = productOptional.orElseThrow(() -> new ProductNotFoundException(id));

        existingProduct.setName(request.name());
        existingProduct.setDescription(request.description());
        existingProduct.setPrice(request.price());

        existingProduct.setStockQuantity(request.stockQuantity());
        existingProduct.setActive(request.active());
        existingProduct.setCategory(request.category());
        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toResponse(updatedProduct);
    }

    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }

    public PaginationResponse<ProductResponse> getProductsByCategory(ProductCategory category, int pageNumber,
            int pageSize, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Product> productPage = productRepository.findByCategory(category, pageable);
        List<ProductResponse> content = productPage.getContent().stream().map(productMapper::toResponse).toList();     
        return new PageResponse<>(content, productPage.getNumber(),  
        productPage.getSize(), productPage.getTotalElements(), 
        productPage.getTotalPages(), productPage.isFirst(), 
        productPage.isLast());  
    }

    private boolean useSlice(String tableName) {
        logger.debug("Slice enabled for tables: {}", sliceProperties.sliceEnabledFor());
        return sliceProperties.sliceEnabledFor().contains(tableName);
    }
}
