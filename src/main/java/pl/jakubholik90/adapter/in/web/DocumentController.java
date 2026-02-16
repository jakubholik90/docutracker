package pl.jakubholik90.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.jakubholik90.adapter.in.web.dto.DocumentRequest;
import pl.jakubholik90.adapter.in.web.dto.DocumentResponse;
import pl.jakubholik90.domain.model.Document;
import pl.jakubholik90.domain.port.in.*;

import java.net.URI;
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
    public ResponseEntity<List<DocumentResponse>> getDocumentsByProjectId(@RequestParam int projectId) {
        ResponseEntity<List<DocumentResponse>> responseEntity;

        List<DocumentResponse> listOfDocuments = new ArrayList<>();

        List<Document> documentsByProjectId = getDocumentsByProjectIdUseCase.getDocumentsByProjectId(projectId);

        if (documentsByProjectId.isEmpty()) {
            responseEntity = ResponseEntity.notFound().build();
        } else {
            for (Document documentByProjectId : documentsByProjectId) {
                DocumentResponse response = new DocumentResponse(
                        documentByProjectId.getDocumentId(),
                        documentByProjectId.getFileName(),
                        documentByProjectId.getProjectId(),
                        documentByProjectId.getStatus(),
                        documentByProjectId.getCurrentRecipient(),
                        documentByProjectId.getLastStatusChange()
                );
                listOfDocuments.add(response);
            }
            responseEntity = ResponseEntity
                    .ok()
                    .body(listOfDocuments);
        }

        return responseEntity;
    }
}
