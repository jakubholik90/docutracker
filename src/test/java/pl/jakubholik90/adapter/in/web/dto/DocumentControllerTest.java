package pl.jakubholik90.adapter.in.web.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.jakubholik90.adapter.in.web.DocumentController;
import pl.jakubholik90.domain.model.Document;
import pl.jakubholik90.domain.model.DocumentStatus;
import pl.jakubholik90.domain.port.in.CreateDocumentDTO;
import pl.jakubholik90.domain.port.in.CreateDocumentUseCase;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest //spring adnotation for testing web controllers
public class DocumentControllerTest {

    private DocumentController documentController;

    @MockitoBean // mock for spring beans
    CreateDocumentUseCase createDocumentUseCase;

    @Autowired
    MockMvc mockMvc; // MockMvc = simulating HTTP request without starting a server

    @Test
    public void shouldCreateDocumentAndReturn201() throws Exception {
        // given
        Document mockDocument = Document.builder()
                .documentId(123)
                .fileName("test.pdf")
                .status(DocumentStatus.DRAFT)
                .build();
        when(createDocumentUseCase.createDocument(any(CreateDocumentDTO.class))).thenReturn(mockDocument);
        // when
        String jsonString = """
        {
            "fileName": "test.pdf",
                "projectId": 1,
                "initialRecipient": "SUBCONTRACTOR"
        }
        """;
        mockMvc.perform(
                post("/api/documents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
        )
        // then
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect((header().string("Location","/api/documents/123")))
                .andExpect(jsonPath("$.id").value(123))
                .andExpect(jsonPath("$.fileName").value("test.pdf"))
                .andExpect(jsonPath("$.status").value("DRAFT"));

        verify(createDocumentUseCase,times(1)).createDocument(any(CreateDocumentDTO.class));
    }


}
