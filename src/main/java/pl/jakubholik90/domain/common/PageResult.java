package pl.jakubholik90.domain.common;

import java.util.List;

public record PageResult<T>(
        List<T> content,
        int page,
        int size,
        int totalElements,
        int totalPages) {

    public boolean hasNext() {
        return page < totalElements - 1;
    }
     public boolean hasPrevious() {
        return page > 0;
     }

}
