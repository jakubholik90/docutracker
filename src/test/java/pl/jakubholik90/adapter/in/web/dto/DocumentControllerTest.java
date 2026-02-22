package pl.jakubholik90.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.util.MultiValueMap;
import pl.jakubholik90.adapter.in.web.DocumentController;
import pl.jakubholik90.domain.common.PageRequest;
import pl.jakubholik90.domain.common.PageResult;
import pl.jakubholik90.domain.model.Document;
import pl.jakubholik90.domain.model.DocumentStatus;
import pl.jakubholik90.domain.model.RecipientType;
import pl.jakubholik90.domain.model.StatusChangeEvent;
import pl.jakubholik90.domain.port.in.*;
import pl.jakubholik90.infrastructure.exception.DocumentException;

import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.servlet.function.RequestPredicates.param;


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

    @MockitoBean
    ChangeDocumentStatusUseCase changeDocumentStatusUseCase;

    @MockitoBean
    GetDocumentStatusHistoryUseCase getDocumentStatusHistoryUseCase;


    @Autowired
    MockMvc mockMvc; // MockMvc = simulating HTTP request without starting a server

    @Test // testing CreateDocumentUseCase
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

    @Test // testing CreateDocumentUseCase
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

    @Test // testing GetDocumentByIdeUseCase
    public void shouldReturn404WhenNotFound() throws Exception {
        int nonExistentId = 99;
        when(getDocumentByIdUseCase.getDocumentById(nonExistentId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/documents/" + nonExistentId))
                .andExpect(status().isNotFound());

        verify(getDocumentByIdUseCase,times(1)).getDocumentById(nonExistentId);
    }

    @Test // testing GetDocumentsByProjectIdUseCase
    public void shouldReturn200AndListOfDocumentsByProjectId() throws Exception {
        int projectId = 1;

        Document mockDocument0 = Document.builder()
                .documentId(0)
                .projectId(1)
                .build();
        Document mockDocument1 = Document.builder()
                .documentId(1)
                .projectId(1)
                .build();

        List<Document> listOfMockedDocuments = new ArrayList<>();
        listOfMockedDocuments.add(mockDocument0);
        listOfMockedDocuments.add(mockDocument1);

        PageRequest pageRequest = new PageRequest(0,listOfMockedDocuments.size());

        PageResult<Document> pageResult = new PageResult<>(
                listOfMockedDocuments,
                0,
                2,
                2,
                1);

        when(getDocumentsByProjectIdUseCase.getDocumentsByProjectId(projectId, pageRequest)).thenReturn(pageResult);

        mockMvc.perform(get("/api/documents")
                        .param("projectId", "1")
                        .param("page","0")
                        .param("size",String.valueOf(listOfMockedDocuments.size())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content",hasSize(2)))
                .andExpect(jsonPath("$.content[0].projectId").value(projectId))
                .andExpect(jsonPath("$.content[0].id").value(greaterThanOrEqualTo(0)))
                .andExpect(jsonPath("$.content[1].projectId").value(projectId))
                .andExpect(jsonPath("$.content[1].id").value(greaterThanOrEqualTo(0)));

        verify(getDocumentsByProjectIdUseCase,times(1)).getDocumentsByProjectId(projectId,pageRequest);
        verify(getAllDocumentsUseCase,never()).getAllDocuments(pageRequest);
    }

    @Test // testing GetALlDocumentsUseCase
    public void shouldReturn200AndAllDocumentsInPage() throws Exception {
        int page = 1;
        int size = 2;
        long totalElements = 5;
        int totalPages = 3;

        PageRequest pageRequest = new PageRequest(page, size);

        Document mockDocument2 = Document.builder()
                .documentId(2)
                .build();
        Document mockDocument3 = Document.builder()
                .documentId(3)
                .build();

        List<Document> content = new ArrayList<>();
        content.add(mockDocument2);
        content.add(mockDocument3);

        PageResult<Document> pageResult = new PageResult<>(
                content,
                page,
                size,
                totalElements,
                totalPages);

        when(getAllDocumentsUseCase.getAllDocuments(pageRequest)).thenReturn(pageResult);

        mockMvc.perform(get("/api/documents")
                .param("page",String.valueOf(page))
                .param("size",String.valueOf(size)))
                .andExpect(status().isOk())
               //  .andExpect(jsonPath("$.content").value(projectId))
                .andExpect(jsonPath("$.page").value(page))
                .andExpect(jsonPath("$.size").value(size))
                .andExpect(jsonPath("$.totalElements").value(totalElements))
                .andExpect(jsonPath("$.totalPages").value(totalPages))
                .andExpect(jsonPath("$.content[0].id").value(mockDocument2.getDocumentId()))
                .andExpect(jsonPath("$.content[1].id").value(mockDocument3.getDocumentId()))
                .andExpect(jsonPath("$.content",hasSize(content.size())));

        verify(getAllDocumentsUseCase,times(1)).getAllDocuments(pageRequest);
        verify(getDocumentByIdUseCase,never()).getDocumentById(anyInt());
    }

    @Test // testing ChangeDocumentStatusUseCase
    public void shouldChangeStatusAndReturn200() throws Exception {
        //given
        Document documentNewStatus = Document.builder()
                .documentId(1)
                .fileName("test.pdf")
                .projectId(1)
                .status(DocumentStatus.AT_USER)
                .currentRecipient(RecipientType.USER)
                .lastStatusChange(LocalDateTime.of(1996,12,31,12,59,00))
                .history(List.of(
                        StatusChangeEvent.builder()
                                .timestamp(LocalDateTime.of(1996,12,31,12,59,00))
                                .fromStatus(DocumentStatus.DRAFT)
                                .toStatus(DocumentStatus.AT_USER)
                                .fromRecipient(RecipientType.SUBCONTRACTOR)
                                .toRecipient(RecipientType.USER)
                                .changedBy("SYSTEM") // placeholder
                                .reason("newChange")
                                .build()))
                .build();

        when(changeDocumentStatusUseCase.changeDocumentStatus(any(ChangeDocumentStatusDTO.class))).thenReturn(documentNewStatus);
        String jsonString = """
        {
            "newStatus": "AT_USER",
            "newRecipient": "USER",
            "reason": "newChange"
        }
        """;

        // when
        mockMvc.perform(
                        patch("/api/documents/{id}/status", documentNewStatus.getDocumentId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonString)
                )
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("AT_USER"))
                .andExpect(jsonPath("$.currentRecipient").value("USER"))
                .andExpect(jsonPath("$.lastStatusChange").exists())
                .andExpect(jsonPath("$.lastStatusChange").isNotEmpty());

    verify(changeDocumentStatusUseCase,times(1)).changeDocumentStatus(any(ChangeDocumentStatusDTO.class));
    }

    @Test // testing ChangeDocumentStatusUseCase
    public void shouldReturn404WhenDocumentNotFoundOnStatusChange() throws Exception {
        //given
        when(changeDocumentStatusUseCase.changeDocumentStatus(any(ChangeDocumentStatusDTO.class))).thenThrow(new DocumentException("Document not found"));
        String jsonString = """
        {
            "newStatus": "AT_USER",
            "newRecipient": "USER",
            "reason": "newChange",
            "changedBy": "SYSTEM"
        }
        """;

        // when
        mockMvc.perform(
                        patch("/api/documents/{id}/status", 999)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonString)
                )
                // then
                .andExpect(status().isNotFound());

        verify(changeDocumentStatusUseCase,times(1)).changeDocumentStatus(any(ChangeDocumentStatusDTO.class));
    }

    @Test // testing GetDocumentStatusHistoryUseCase
    public void shouldReturn200AndDocumentStatusHistory() throws Exception {
        //given
        int id = 1;

        StatusChangeEvent event0 = StatusChangeEvent.builder()
                .id(0L)
                .timestamp(LocalDateTime.now())
                .fromStatus(DocumentStatus.DRAFT)
                .toStatus(DocumentStatus.AT_USER)
                .fromRecipient(RecipientType.USER)
                .toRecipient(RecipientType.USER)
                .changedBy("SYSTEM")
                .reason("setup")
                .build();

        StatusChangeEvent event1 = StatusChangeEvent.builder()
                .id(1L)
                .timestamp(LocalDateTime.now())
                .fromStatus(event0.getToStatus())
                .toStatus(DocumentStatus.AT_SUBCONTRACTOR)
                .fromRecipient(event0.getToRecipient())
                .toRecipient(RecipientType.SUBCONTRACTOR)
                .changedBy("SYSTEM")
                .reason("toSubcontractor")
                .build();

        ArrayList<StatusChangeEvent> history = new ArrayList<>();
        history.add(event0);
        history.add(event1);

        when(getDocumentStatusHistoryUseCase.getDocumentStatusHistory(id)).thenReturn(history);

        // when
        mockMvc.perform(
                        get("/api/documents/{id}/history",id))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(history.size())))

                .andExpect(jsonPath("$[0].timestamp").isNotEmpty())
                .andExpect(jsonPath("$[0].fromStatus").value(event0.getFromStatus().name()))
                .andExpect(jsonPath("$[0].toStatus").value(event0.getToStatus().name()))
                .andExpect(jsonPath("$[0].fromRecipient").value(event0.getFromRecipient().name()))
                .andExpect(jsonPath("$[0].toRecipient").value(event0.getToRecipient().name()))
                .andExpect(jsonPath("$[0].changedBy").value(event0.getChangedBy()))
                .andExpect(jsonPath("$[0].reason").value(event0.getReason()))

                .andExpect(jsonPath("$[1].timestamp").isNotEmpty())
                .andExpect(jsonPath("$[1].fromStatus").value(event1.getFromStatus().name()))
                .andExpect(jsonPath("$[1].toStatus").value(event1.getToStatus().name()))
                .andExpect(jsonPath("$[1].fromRecipient").value(event1.getFromRecipient().name()))
                .andExpect(jsonPath("$[1].toRecipient").value(event1.getToRecipient().name()))
                .andExpect(jsonPath("$[1].changedBy").value(event1.getChangedBy()))
                .andExpect(jsonPath("$[1].reason").value(event1.getReason()))
                ;

        verify(getDocumentStatusHistoryUseCase,times(1)).getDocumentStatusHistory(id);
    }
}
