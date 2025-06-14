package com.ingemark.domain.pagination;

import java.util.List;

public class PaginatedResult<T> {
    private final List<T> items;
    private final long totalElements;
    private final int totalPages;

    public PaginatedResult(List<T> items, long totalElements, int totalPages) {
        this.items = items;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public List<T> getItems() {
        return items;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }
}