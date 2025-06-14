package com.ingemark.domain.pagination;

public class PaginationRequest {
    private final int page;
    private final int size;
    private final String sortBy;
    private final String sortDirection;

    public PaginationRequest(int page, int size, String sortBy, String sortDirection) {
        this.page = page;
        this.size = size;
        this.sortBy = sortBy;
        this.sortDirection = sortDirection;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public String getSortBy() {
        return sortBy;
    }

    public String getSortDirection() {
        return sortDirection;
    }
}
