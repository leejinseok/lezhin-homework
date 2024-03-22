package com.lezhin.homework.api.presentation.common.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PageInfo {

    private int currentPage;
    private int totalPages;
    private int size;
    private long totalElements;
    private String sort;
    private Boolean last;

    public static PageInfo create(final Page page) {
        PageInfo pageInfo = new PageInfo();
        pageInfo.currentPage = page.getNumber();
        pageInfo.totalPages = page.getTotalPages();
        pageInfo.size = page.getSize();
        pageInfo.totalElements = page.getTotalElements();
        pageInfo.sort = page.getSort().toString();
        pageInfo.last = page.isLast();
        return pageInfo;
    }

}
