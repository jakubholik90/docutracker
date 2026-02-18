package pl.jakubholik90.domain.common;

import java.util.Objects;

public record PageRequest(int page, int size) {

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PageRequest that)) return false;
        return page() == that.page() && size() == that.size();
    }

    @Override
    public int hashCode() {
        return Objects.hash(page(), size());
    }
}
