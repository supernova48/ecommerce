package com.siju.ecommerce.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    private final static Logger logger = LoggerFactory.getLogger(TestController.class);
    @GetMapping
    public String test() {
        logger.debug("Test endpoint accessed");
        return "Authenticated";
    }
}
