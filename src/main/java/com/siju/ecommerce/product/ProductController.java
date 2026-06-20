package com.siju.ecommerce.product;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siju.ecommerce.category.ProductCategory;
import com.siju.ecommerce.common.PaginationResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Endpoints for managing the ecommerce product catalog")
public class ProductController {

    private final ProductService productService;
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ProductService.class);

    /*
     * @PostMapping
     * 
     * @ResponseStatus(HttpStatus.CREATED)
     * public ProductResponse createProduct(
     * 
     * @Valid @RequestBody ProductRequest request) {
     * 
     * return productService.createProduct(request);
     * 
     * }
     */

    
    
    
    
    @PostMapping
    @Operation(
        summary = "Create product", 
        description = """
        Accepts product payload validation and persists a brand new item 
        to product table. Returns the created product with a 201 Created 
        status.
        """
    )
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request) {

        ProductResponse productResponse = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }

    
    
    
    
    
    @Operation(summary = "Get products with pagination and sorting", description = """
        Retrieves a paginated list of products with optional sorting parameters.
        Clients can specify page number, page size, sorting field, and sorting direction.""")
    @GetMapping
    public ResponseEntity<PaginationResponse<ProductResponse>> getProducts(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        logger.debug("Entering @GetMapping");
        PaginationResponse<ProductResponse> paginationResponse = productService.getProducts(pageNumber, pageSize,
                sortBy, direction);
        return ResponseEntity.ok(paginationResponse);
    }

    
    
    
    
    @Operation(summary = "Get products by name with pagination and sorting", description = """
        Retrieves a paginated list of products that match the specified keyword with product names.
        Clients can specify page number, page size, sorting field, and sorting direction.""")
    @GetMapping(params = "keyword")
    public ResponseEntity<PaginationResponse<ProductResponse>> getProductByName(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        logger.debug("Entering @GetMapping(params=keyword)");
        PaginationResponse<ProductResponse> paginationResponse = productService.getProductsByName(keyword, pageNumber,
                pageSize, sortBy, direction);
        logger.debug("PaginationResponse: {}", paginationResponse);
        return ResponseEntity.ok(paginationResponse);
    }

    
    
    
    
    
    
    @Operation(summary = "Get product by ID", description = """
        Retrieves a single product by its unique identifier. Returns the product details if found, 
        or a 404 Not Found status if no product exists with the specified ID.""")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        logger.debug("Entering @GetMapping({})", id);
        ProductResponse productResponse = productService.getProductById(id);
        logger.debug("ProductResponse: {}", productResponse);
        return ResponseEntity.ok(productResponse);
    }

   
   
   
   
   
    @Operation(summary = "Update product by ID and web request body", description = """
        Updates the details of an existing product identified by its unique ID.
        Returns the updated product details if the update is successful, 
        or a 404 Not Found status if no product exists with the specified ID.""")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        logger.debug("Entering @PutMapping({})", id);
        ProductResponse productResponse = productService.updateProduct(id, request);
        logger.debug("Updated ProductResponse: {}", productResponse);
        return ResponseEntity.status(HttpStatus.OK).body(productResponse);
    }

    
    
    
    
    
    @Operation(summary = "Delete product by ID", description = """
        Deletes an existing product identified by its unique ID.
        Returns a 204 No Content status if the deletion is successful,
        or a 404 Not Found status if no product exists with the specified ID.""")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        logger.debug("Entering @DeleteMapping({})", id);
        productService.deleteProduct(id);
        logger.debug("Deleted product with id: {}", id);
        return ResponseEntity.noContent().build();
    }

    
    
    
    
    
    
    
    @Operation(summary = "Get products by category with pagination and sorting", description = """
        Retrieves a paginated list of products that belong to the specified category.
        Clients can specify page number, page size, sorting field, and sorting direction.""")
    @GetMapping("/category/{category}")
    public ResponseEntity<PaginationResponse<ProductResponse>> getProductsByCategory(
            @PathVariable ProductCategory category,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        logger.debug("Entering @GetMapping(/category/{})", category);
        PaginationResponse<ProductResponse> paginationResponse = productService.getProductsByCategory(category, pageNumber,
                pageSize, sortBy, direction);
        logger.debug("PaginationResponse: {}", paginationResponse);
        return ResponseEntity.ok(paginationResponse);
    }

}
