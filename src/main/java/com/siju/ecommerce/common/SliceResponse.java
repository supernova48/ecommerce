package com.siju.ecommerce.common;

import java.util.List;


public record SliceResponse<T>  (

    List<T> content,
    int pageNumber,
    int pageSize,
    boolean first,
    boolean last,
    boolean hasNext

) implements PaginationResponse<T>

{

}
