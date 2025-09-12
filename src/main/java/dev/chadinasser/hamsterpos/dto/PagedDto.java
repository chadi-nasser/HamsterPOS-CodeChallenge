package dev.chadinasser.hamsterpos.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PagedDto<T> {
    private final List<T> content;
    private final int pageNumber;
    private final int pageSize;
    private final long totalElements;
    private final int totalPages;
    private final boolean last;

    public PagedDto(Page<T> content) {
        this.content = content.getContent();
        this.pageNumber = content.getNumber();
        this.pageSize = content.getSize();
        this.totalElements = content.getTotalElements();
        this.totalPages = content.getTotalPages();
        this.last = content.isLast();
    }
}
