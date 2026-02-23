package pl.jakubholik90.domain.model;

import lombok.Builder;

import java.time.LocalDateTime;

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
}
