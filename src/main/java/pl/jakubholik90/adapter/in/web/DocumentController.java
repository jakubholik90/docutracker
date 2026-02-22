package pl.jakubholik90.adapter.in.web;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.jakubholik90.adapter.in.web.dto.*;
import pl.jakubholik90.domain.common.PageRequest;
import pl.jakubholik90.domain.common.PageResult;
import pl.jakubholik90.domain.model.Document;
import pl.jakubholik90.domain.model.DocumentStatus;
import pl.jakubholik90.domain.model.RecipientType;
import pl.jakubholik90.domain.model.StatusChangeEvent;
import pl.jakubholik90.domain.port.in.*;
import pl.jakubholik90.infrastructure.exception.DocumentException;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class DocumentController {

    private final CreateDocumentUseCase createDocumentUseCase;
    private final GetDocumentByIdUseCase getDocumentByIdUseCase;
    private final GetDocumentsByProjectIdUseCase getDocumentsByProjectIdUseCase;
    private final GetAllDocumentsUseCase getAllDocumentsUseCase;
    private final ChangeDocumentStatusUseCase changeDocumentStatusUseCase;
    private final GetDocumentStatusHistoryUseCase getDocumentStatusHistoryUseCase;

    @PostMapping("/api/documents")
    public ResponseEntity<DocumentResponse> createDocument(@RequestBody DocumentRequest documentRequest) {

        if (documentRequest.fileName().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        CreateDocumentDTO dto = new CreateDocumentDTO(
                documentRequest.fileName(),
                documentRequest.projectId(),
                documentRequest.initialRecipient());

        Document document = createDocumentUseCase.createDocument(dto);

        ResponseEntity<DocumentResponse> responseEntity;

        DocumentResponse response = new DocumentResponse(
                document.getDocumentId(),
                document.getFileName(),
                document.getProjectId(),
                document.getStatus(),
                document.getCurrentRecipient(),
                document.getLastStatusChange());

        responseEntity = ResponseEntity
                .created(URI.create("/api/documents/"+ document.getDocumentId()))
                .body(response);

        return responseEntity;
    }

    @GetMapping("api/documents/{id}")
    public ResponseEntity<DocumentResponse> getDocumentById(@PathVariable int id) {

        Optional<Document> documentById = getDocumentByIdUseCase.getDocumentById(id);

        ResponseEntity<DocumentResponse> responseEntity;

        if (documentById.isEmpty()) {
            responseEntity = ResponseEntity.notFound().build();
        } else {
            DocumentResponse response = new DocumentResponse(
                    documentById.get().getDocumentId(),
                    documentById.get().getFileName(),
                    documentById.get().getProjectId(),
                    documentById.get().getStatus(),
                    documentById.get().getCurrentRecipient(),
                    documentById.get().getLastStatusChange()
            );
            responseEntity = ResponseEntity
                    .ok()
                    .body(response);
        }

        return responseEntity;
    }

    @GetMapping("api/documents")
    public ResponseEntity<DocumentPageResponse> getAllDocumentsPaginated(
            @RequestParam(required = false) Integer projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        ResponseEntity<DocumentPageResponse> responseEntity;
        PageResult<Document> pageResult;
        List<Document> listDocuments = new ArrayList<>();

        if (projectId != null) {
            // getDocumentsByProjectUseCase
            pageResult = getDocumentsByProjectIdUseCase.getDocumentsByProjectId(projectId, new PageRequest(page, size));
        } else {
            // getAllDocumentsUseCase
            pageResult = getAllDocumentsUseCase.getAllDocuments(new PageRequest(page, size));
        }

        listDocuments = pageResult.content();
        List<DocumentResponse> listDocumentResponses = new ArrayList<>();
        for (Document document : listDocuments) {
            DocumentResponse response = new DocumentResponse(
                    document.getDocumentId(),
                    document.getFileName(),
                    document.getProjectId(),
                    document.getStatus(),
                    document.getCurrentRecipient(),
                    document.getLastStatusChange()
            );
            listDocumentResponses.add(response);
        }

        DocumentPageResponse documentPageResponse = new DocumentPageResponse(
                listDocumentResponses,
                page,
                size,
                pageResult.totalElements(),
                pageResult.totalPages());

        responseEntity = ResponseEntity
                .ok()
                .body(documentPageResponse);

        return responseEntity;
    }

    @PatchMapping("api/documents/{id}/status")
    public ResponseEntity<DocumentResponse> updateDocument(@PathVariable int id, @RequestBody StatusChangeEventRequest request) {
        ChangeDocumentStatusDTO changeStatusDTO = new ChangeDocumentStatusDTO(
                id,
                request.newStatus(),
                request.newRecipient(),
                request.reason(),
                "SYSTEM");

        ResponseEntity<DocumentResponse> responseEntity;

        try {
            Document document = changeDocumentStatusUseCase.changeDocumentStatus(changeStatusDTO);
            DocumentResponse documentResponse = new DocumentResponse(
                    document.getDocumentId(),
                    document.getFileName(),
                    document.getProjectId(),
                    document.getStatus(),
                    document.getCurrentRecipient(),
                    document.getLastStatusChange());

            responseEntity = ResponseEntity
                    .ok()
                    .body(documentResponse);
        } catch (DocumentException e) {
            responseEntity = ResponseEntity
                    .notFound()
                    .build();
        }
            return responseEntity;
    }

    @GetMapping("api/documents/{id}/history")
    public ResponseEntity<List<StatusChangeEventResponse>> getDocumentHistory(@PathVariable int id) {
        ResponseEntity<List<StatusChangeEventResponse>> returnEntity;
        Optional<Document> documentById = getDocumentByIdUseCase.getDocumentById(id);
        if (documentById.isEmpty()) {
            returnEntity = ResponseEntity
                    .notFound()
                    .build();
        } else {
            List<StatusChangeEvent> history = getDocumentStatusHistoryUseCase.getDocumentStatusHistory(id);
            List<StatusChangeEventResponse> historyResponse = history.stream()
                    .map(a -> new StatusChangeEventResponse(
                            a.getTimestamp(),
                            a.getFromStatus(),
                            a.getToStatus(),
                            a.getFromRecipient(),
                            a.getToRecipient(),
                            a.getChangedBy(),
                            a.getReason()))
                    .toList();

            returnEntity = ResponseEntity
                    .ok()
                    .body(historyResponse);
        }
        return returnEntity;
    }
}
