package pl.jakubholik90.adapter.in.web.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.jakubholik90.adapter.in.web.DocumentController;
import pl.jakubholik90.domain.model.Document;
import pl.jakubholik90.domain.model.DocumentStatus;
import pl.jakubholik90.domain.model.RecipientType;
import pl.jakubholik90.domain.port.in.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest //spring adnotation for testing web controllers
public class DocumentControllerTest {

    @MockitoBean // mock for spring beans
    CreateDocumentUseCase createDocumentUseCase;

    @MockitoBean
    GetAllDocumentsUseCase getAllDocumentsUseCase;

    @MockitoBean
    GetDocumentByIdUseCase getDocumentByIdUseCase;

    @MockitoBean
    GetDocumentsByProjectIdUseCase getDocumentsByProjectIdUseCase;

    @Autowired
    MockMvc mockMvc; // MockMvc = simulating HTTP request without starting a server

    @Test // testing if controller works
    public void shouldCreateDocumentAndReturn201() throws Exception {
        // given
        Document mockDocument = Document.builder()
                .documentId(123)
                .fileName("test.pdf")
                .status(DocumentStatus.DRAFT)
                .build();
        when(createDocumentUseCase.createDocument(any())).thenReturn(mockDocument);

        String jsonString = """
        {
            "fileName": "test.pdf"
        }
        """;

        // when
        mockMvc.perform(
                post("/api/documents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
        )
        // then
                .andExpect(status().isCreated())
                .andExpect(status().is(201))
                .andExpect(header().exists("Location"))
                .andExpect((header().string("Location","/api/documents/123")))
                .andExpect(jsonPath("$.id").value(123))
                .andExpect(jsonPath("$.fileName").value("test.pdf"))
                .andExpect(jsonPath("$.status").value("DRAFT"));

        verify(createDocumentUseCase,times(1)).createDocument(any(CreateDocumentDTO.class));
    }

    @Test // testing in controller returns error
    public void shouldReturn400WhenFileNameIsEmpty() throws Exception {
        // given
        String jsonRequest = """
                {
                  "fileName": ""
                }
                """;
        // when
        mockMvc.perform(
                post("/api/documents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
        )
        // then
        .andExpect(status().isBadRequest());

        verify(createDocumentUseCase,never()).createDocument(any());
    }

    @Test // testing GetDocumentByIdeUseCase
    public void shouldReturnDocumentByIdAndStatus200() throws Exception {
        int id = 1;
        Document mockDocument = Document.builder()
                .documentId(id)
                .fileName("test.pdf")
                .projectId(10)
                .status(DocumentStatus.AT_USER)
                .currentRecipient(RecipientType.CLIENT)
                .lastStatusChange(LocalDateTime.of(1990,12,31,23,59))
                .build();

        when(getDocumentByIdUseCase.getDocumentById(id)).thenReturn(Optional.of(mockDocument));

        mockMvc.perform(get("/api/documents/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fileName").value("test.pdf"))
                .andExpect(jsonPath("$.projectId").value(10))
                .andExpect(jsonPath("$.status").value("AT_USER"))
                .andExpect(jsonPath("$.currentRecipient").value("CLIENT"))
                .andExpect(jsonPath("$.lastStatusChange").value("1990-12-31T23:59:00"));

        verify(getDocumentByIdUseCase,times(1)).getDocumentById(id);
    }


}
