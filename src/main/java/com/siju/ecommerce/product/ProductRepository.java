package com.siju.ecommerce.product;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.siju.ecommerce.category.ProductCategory;

public interface ProductRepository extends JpaRepository<Product, Long> {

        Slice<Product> findBy(Pageable pageable);

        Page<Product> findByNameContainingIgnoreCase(
                        String keyword,
                        Pageable pageable);

        /*
         * Slice<Product> findSliceByNameContainingIgnoreCase(String keyword,
         * Pageable pageable);
         */

        // If you don't want to use long method name use @Query like below
        // for the above method, will give same result
        @Query("""
                        select p
                        from Product p
                        where lower(p.name) like lower(concat('%', :keyword, '%'))
                        """)
        Page<Product> search(
                        @Param("keyword") String keyword,
                        Pageable pageable);

        Page<Product> findByPriceBetween(
                        BigDecimal min,
                        BigDecimal max,
                        Pageable pageable);

        Optional<Product> findById(Long id);

        Page<Product> findByCategory(ProductCategory category, Pageable pageable);
        Slice<Product> findSliceByCategory(ProductCategory category, Pageable pageable);
}
