package com.siju.ecommerce.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="app.pagination")
public record PaginationProperties(

    List<String> sliceEnabledFor

) 

{

}
