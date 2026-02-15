package pl.jakubholik90.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.jakubholik90.adapter.in.web.dto.DocumentRequest;
import pl.jakubholik90.adapter.in.web.dto.DocumentResponse;
import pl.jakubholik90.domain.model.Document;
import pl.jakubholik90.domain.port.in.CreateDocumentDTO;
import pl.jakubholik90.domain.port.in.CreateDocumentUseCase;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/documents")
public class DocumentController {

    private final CreateDocumentUseCase createDocumentUseCase;

    @PostMapping
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
}
