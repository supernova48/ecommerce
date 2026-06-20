package com.siju.ecommerce.benchmark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.siju.ecommerce.product.ProductService;

@Component
public class QuickTimerRunner implements CommandLineRunner {

    private final ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(QuickTimerRunner.class);

    public QuickTimerRunner(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void run(String... args) throws Exception {
        Long id = 3L;

        // ---- FIRST CALL (SQL Database) ----
        long startTime = System.nanoTime();
        productService.getProductById(id);
        long endTime = System.nanoTime();
        
        double durationInMilliseconds = (endTime - startTime) / 1_000_000.0;
        logger.debug("⏱️ [CALL 1 - DB]: Time taken: {} ms", durationInMilliseconds);

        // ---- SECOND CALL (Redis Memory) ----
        startTime = System.nanoTime();
        productService.getProductById(id);
        endTime = System.nanoTime();

        durationInMilliseconds = (endTime - startTime) / 1_000_000.0;
        logger.debug("⚡ [CALL 2 - REDIS]: Time taken: {} ms", durationInMilliseconds);

        // ---- Third CALL (Redis Memory) ----
        startTime = System.nanoTime();
        productService.getProductById(id);
        endTime = System.nanoTime();

        durationInMilliseconds = (endTime - startTime) / 1_000_000.0;
        logger.debug("⚡ [CALL 3 - REDIS]: Time taken: {} ms", durationInMilliseconds);

    }
}