package pl.jakubholik90.domain.common;

import java.util.List;
import java.util.Objects;

public record PageResult<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages) {

    public boolean hasNext() {
        return page < totalElements - 1;
    }
     public boolean hasPrevious() {
        return page > 0;
     }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PageResult<?> that)) return false;
        return page() == that.page() && size() == that.size() && totalPages() == that.totalPages() && totalElements() == that.totalElements() && Objects.equals(content(), that.content());
    }

    @Override
    public int hashCode() {
        return Objects.hash(content(), page(), size(), totalElements(), totalPages());
    }
}
