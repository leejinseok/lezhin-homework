package com.lezhin.homework.api.presentation.common.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResponse<T> {

    private final PageInfo pageInfo;
    private final List<T> list;

    public PageResponse(final Page<T> page) {
        this.pageInfo = PageInfo.create(page);
        this.list = page.getContent();
    }

}
