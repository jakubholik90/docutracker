package pl.jakubholik90.adapter.in.web.dto;

import java.util.List;

public record DocumentPageResponse(
        List<DocumentResponse> content,
        int page,
        int size,
        int totalElements,
        int totalPages) {
}
