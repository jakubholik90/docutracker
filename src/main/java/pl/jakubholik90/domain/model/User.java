package pl.jakubholik90.domain.model;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder
public class User {

    private final Long id;
    private final String email;
    private final String name;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return Objects.equals(getId(), user.getId()) && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getName(), user.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getName());
    }
}
