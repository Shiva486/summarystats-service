package com.clipboardhealth.summarystatsservice.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class CommonFunctions {

    private static final Integer DEFAULT_PAGE_SIZE = 100;

    public static Pageable getPageable(Integer pageSize, Integer pageNumber, String sortColumn, String sortOrder) {
        Pageable pageable = null;
        pageNumber = pageNumber == null ? 0 : pageNumber-1;
        sortOrder = sortOrder == null ? Sort.Direction.ASC.name() : Sort.Direction.valueOf(sortOrder.toUpperCase()).name();

        if (pageSize != null && sortColumn == null) {
            pageable = PageRequest.of(pageNumber, pageSize);
        }
        else if (pageSize == null && sortColumn != null) {
            Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortColumn);
            pageable = PageRequest.of(0, DEFAULT_PAGE_SIZE, sort);
        }
        else if (pageSize != null) {
            Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortColumn);
            pageable = PageRequest.of(pageNumber, pageSize, sort);
        }

        return pageable;
    }
}
